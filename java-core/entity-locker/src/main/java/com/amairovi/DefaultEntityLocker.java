package com.amairovi;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Collections.emptySet;

public class DefaultEntityLocker<T> implements EntityLocker<T> {
    private final static Logger log = Logger.getLogger(DefaultEntityLocker.class.getName());

    private final ReentrancyHandler<? super T> reentrancyHandler;
    private final DeadlockDetector<? super T> deadLockDetector;
    private final Map<T, Boolean> locked = new ConcurrentHashMap<>();
    private final AtomicBoolean isGlobalLockAcquired = new AtomicBoolean(false);
    private volatile Thread threadWithGlobalLock;
    private final AtomicInteger globalLockReentrancy = new AtomicInteger();
    private final Map<Thread, Set<T>> threadToOwnedEntities = new ConcurrentHashMap<>();
    private final AtomicInteger totalAmountOfLocked = new AtomicInteger();
    private final AtomicInteger totalAmountOfAcquiresInProcess = new AtomicInteger();

    public DefaultEntityLocker(ReentrancyHandler<? super T> reentrancyHandler) {
        this.reentrancyHandler = reentrancyHandler;
        deadLockDetector = new DefaultDeadlockDetector<>();
    }

    @Override
    public boolean lock(final T id, final long timeout, final TimeUnit unit) {
        log.log(Level.FINE, "trying to lock " + id + " with timeout");
        Instant shouldStopLockingAt = Instant.now().plus(unit.toNanos(timeout), ChronoUnit.NANOS);
        return lock(id, shouldStopLockingAt);
    }

    @Override
    // TODO think about adding reaction to interruption
    public void lock(final T id) {
        lock(id, null);
    }

    private boolean lock(final T id, final Instant shouldStopLockingAt) {
        log.log(Level.FINE, "trying to lock " + id);

        if (threadWithGlobalLock != Thread.currentThread()) {
            while (isGlobalLockAcquired.get()) {
                deadLockDetector.check(id, Thread.currentThread());
            }
        }

        deadLockDetector.addLockAcquiring(id, Thread.currentThread());
        totalAmountOfAcquiresInProcess.getAndIncrement();

        boolean result;
        try {
            while (true) {
                Boolean previous = locked.putIfAbsent(id, Boolean.TRUE);

                if (previous == null) {
                    reentrancyHandler.increase(id);
                    addLockOwning(id, Thread.currentThread());
                    deadLockDetector.addLockOwning(id, Thread.currentThread());
                    log.log(Level.FINE, "lock for " + id + " is free");
                    result = true;
                    break;
                } else {
                    if (reentrancyHandler.increaseIfPresent(id)) {
                        result = true;
                        break;
                    }
                    if (shouldStopLockingAt != null && Instant.now().isAfter(shouldStopLockingAt)) {
                        log.log(Level.FINE, "timeout has elapsed. Stop trying to lock " + id);
                        result = false;
                        break;
                    }
                    log.log(Level.FINE, "lock for " + id + " is not free");

                    if (shouldStopLockingAt == null) {
                        deadLockDetector.check(id, Thread.currentThread());
                    }
                }
            }
        } finally {
            totalAmountOfAcquiresInProcess.decrementAndGet();
            deadLockDetector.removeLockAcquiring(id, Thread.currentThread());
        }
        return result;
    }

    @Override
    public void unlock(final T id) {
        log.log(Level.FINE, "unlocking " + id);
        Boolean isLocked = locked.get(id);
        if (isLocked == null) {
            log.log(Level.INFO, "trying to unlock not locked " + id);
            return;
        }

        if (reentrancyHandler.decrease(id) == 0) {
            deadLockDetector.removeLockOwning(id, Thread.currentThread());
            removeLockOwning(id, Thread.currentThread());
            locked.remove(id);
        }
    }

    @Override
    public void lockGlobal() {
        log.log(Level.FINE, "lock global");

        Thread currentThread = Thread.currentThread();
        if (isGlobalLockAcquired.getAndSet(true)) {
            if (threadWithGlobalLock == currentThread) {
                log.log(Level.FINE, "global lock has been already acquired by this thread, increase reentrancy");
                globalLockReentrancy.getAndIncrement();
                return;
            } else {
                log.log(Level.FINE, "global lock has been already acquired by another thread");
                if (threadToOwnedEntities.containsKey(currentThread)) {
                    log.log(Level.FINE, "current thread owns some entities, " +
                            "it means that global locking of the another thread still is in progress, " +
                            "so there is a deadlock as neither current nor another thread " +
                            "won't be able to acquire global lock");
                    // TODO: add message about global deadlock
                    throw new DeadlockDetectedException();
                } else {
                    while (isGlobalLockAcquired.getAndSet(true) && threadWithGlobalLock != currentThread) ;
                }
            }
        }
        log.log(Level.FINE, "global lock is marked as acquired");

        deadLockDetector.addGlobalLockAcquiring(currentThread);

        while (totalAmountOfAcquiresInProcess.get() != 0);

        while (true) {
            int amountOfLockedByCurrentThread = threadToOwnedEntities.getOrDefault(currentThread, emptySet())
                    .size();
            if (amountOfLockedByCurrentThread == totalAmountOfLocked.get()) {
                break;
            }
        }

        log.log(Level.FINE, "global lock is acquired");
        globalLockReentrancy.getAndIncrement();
        threadWithGlobalLock = currentThread;

        deadLockDetector.removeGlobalLockAcquiring(Thread.currentThread());
    }

    @Override
    public void unlockGlobal() {
        log.log(Level.FINE, "unlock global");
        if (!isGlobalLockAcquired.get()) {
            log.log(Level.INFO, "trying to unlock not locked global");
            return;
        }

        if (globalLockReentrancy.decrementAndGet() == 0) {
            log.log(Level.FINE, "free global lock");
            threadWithGlobalLock = null;
            isGlobalLockAcquired.set(false);
        }
    }

    private void addLockOwning(T id, Thread thread) {
        totalAmountOfLocked.getAndIncrement();
        if (threadToOwnedEntities.containsKey(thread)) {
            threadToOwnedEntities.get(thread).add(id);
        } else {
            Set<T> acquiredEntities = new HashSet<>();
            acquiredEntities.add(id);
            threadToOwnedEntities.put(thread, acquiredEntities);
        }
    }

    private void removeLockOwning(T id, Thread thread) {
        totalAmountOfLocked.getAndDecrement();
        threadToOwnedEntities.get(thread).remove(id);
    }
}
