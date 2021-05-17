package com.amairovi;

import com.amairovi.reentrancy.DefaultReentrancyHandler;
import com.amairovi.reentrancy.NoopReentrancyHandler;
import com.amairovi.reentrancy.ReentrancyHandler;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Collections.emptySet;

public class DefaultEntityLocker<T> implements EntityLocker<T> {
    private final static Logger log = Logger.getLogger(DefaultEntityLocker.class.getName());

    private final ReentrancyHandler<? super T> reentrancyHandler;
    private final DeadlockDetector<? super T> deadLockDetector;
    private final LockingContext<T> context = new LockingContext<>();

    private DefaultEntityLocker(ReentrancyHandler<? super T> reentrancyHandler) {
        this.reentrancyHandler = reentrancyHandler;
        deadLockDetector = new DefaultDeadlockDetector<>();
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    // TODO cover with tests different configuration
    public static class Builder<T> {

        private ReentrancyHandler<? super T> reentrancyHandler = new DefaultReentrancyHandler<>();

        // TODO think about interaction with other configs, for example, lock detection
        public Builder<T> withReentrancy(boolean reentrancy) {
            if (reentrancy) {
                reentrancyHandler = new DefaultReentrancyHandler<>();
            } else {
                reentrancyHandler = new NoopReentrancyHandler<>();
            }
            return this;
        }

        public Builder<T> withReentrancyHandler(ReentrancyHandler<? super T> reentrancyHandler) {
            Objects.requireNonNull(reentrancyHandler);
            this.reentrancyHandler = reentrancyHandler;
            return this;
        }

        public DefaultEntityLocker<T> build() {
            return new DefaultEntityLocker<>(reentrancyHandler);
        }

    }

    @Override
    public boolean lock(final T id, final long timeout, final TimeUnit unit) {
        log.log(Level.FINE, () -> "trying to lock " + id + " with timeout");
        Instant shouldStopLockingAt = Instant.now().plus(unit.toNanos(timeout), ChronoUnit.NANOS);
        return lock(id, shouldStopLockingAt);
    }

    @Override
    // TODO think about adding reaction to interruption
    public void lock(final T id) {
        lock(id, null);
    }

    private boolean lock(final T id, final Instant shouldStopLockingAt) {
        log.log(Level.FINE, () -> "trying to lock " + id);

        if (context.threadWithGlobalLock != Thread.currentThread()) {
            while (context.isGlobalLockAcquired.get()) {
                deadLockDetector.check(id, Thread.currentThread(), context);
            }
        }

        context.addLockAcquiring(id, Thread.currentThread());
        boolean result;
        try {
            while (true) {
                Boolean previous = context.locked.putIfAbsent(id, Boolean.TRUE);

                if (previous == null) {
                    reentrancyHandler.increase(id);
                    context.addLockOwning(id, Thread.currentThread());
                    log.log(Level.FINE, () -> "lock for " + id + " is free");
                    result = true;
                    break;
                } else {
                    if (reentrancyHandler.increaseIfPresent(id)) {
                        result = true;
                        break;
                    }

                    if (hasWaitingTimeElapsed(shouldStopLockingAt)) {
                        log.log(Level.FINE, () -> "timeout has elapsed. Stop trying to lock " + id);
                        result = false;
                        break;
                    }
                    log.log(Level.FINE, "lock for " + id + " is not free");

                    if (shouldStopLockingAt == null) {
                        deadLockDetector.check(id, Thread.currentThread(), context);
                    }
                }

            }
        } finally {
            context.removeLockAcquiring(Thread.currentThread());
        }
        return result;
    }

    private boolean hasWaitingTimeElapsed(Instant shouldStopLockingAt) {
        return shouldStopLockingAt != null && Instant.now().isAfter(shouldStopLockingAt);
    }

    @Override
    public void unlock(final T id) {
        log.log(Level.FINE, () -> "unlocking " + id);
        Boolean isLocked = context.locked.get(id);
        if (isLocked == null) {
            log.log(Level.INFO, () -> "trying to unlock not locked " + id);
            return;
        }

        if (reentrancyHandler.decrease(id) == 0) {
            context.removeLockOwning(id, Thread.currentThread());
            context.locked.remove(id);
        }
    }

    @Override
    public void lockGlobal() {
        log.log(Level.FINE, "lock global");

        Thread currentThread = Thread.currentThread();

        if (context.threadWithGlobalLock == currentThread) {
            log.log(Level.FINE, "global lock has been already acquired by this thread, increase reentrancy");
            context.globalLockReentrancy.getAndIncrement();
            return;
        }

        if (context.isGlobalLockAcquired.get()) {
            log.log(Level.FINE, "global lock has been already acquired by another thread");
            if (context.threadToOwnedEntities.containsKey(currentThread)) {
                log.log(Level.FINE, "current thread owns some entities, " +
                        "it means that global locking of the another thread still is in progress, " +
                        "so there is a deadlock as neither current nor another thread " +
                        "won't be able to acquire global lock");
                // TODO: add message about global deadlock
                throw new DeadlockDetectedException();
            }

        }

        acquireGlobalLock(currentThread);
    }

    private void waitUntilAllNonGlobalAcquiredLocksAreFreed(Thread currentThread) {
        while (true) {
            int amountOfLockedByCurrentThread = context.threadToOwnedEntities.getOrDefault(currentThread, emptySet())
                    .size();
            if (amountOfLockedByCurrentThread == context.totalAmountOfLocked.get()) {
                break;
            }
        }
    }

    private void waitUntilAllNonGlobalLockAcquiringInProcessFinish() {
        while (context.totalAmountOfAcquiresInProcess.get() != 0) ;
    }

    private void acquireGlobalLock(Thread currentThread) {
        while (context.isGlobalLockAcquired.getAndSet(true)) ;

        log.log(Level.FINE, "global lock is marked as acquired");

        context.addGlobalLockAcquiring(currentThread);

        waitUntilAllNonGlobalLockAcquiringInProcessFinish();
        waitUntilAllNonGlobalAcquiredLocksAreFreed(currentThread);

        log.log(Level.FINE, "global lock is acquired");
        context.globalLockReentrancy.getAndIncrement();
        context.threadWithGlobalLock = currentThread;

        context.removeGlobalLockAcquiring(Thread.currentThread());
    }

    @Override
    public void unlockGlobal() {
        log.log(Level.FINE, "unlock global");
        if (!context.isGlobalLockAcquired.get()) {
            log.log(Level.INFO, "trying to unlock not locked global");
            return;
        }

        if (context.globalLockReentrancy.decrementAndGet() == 0) {
            log.log(Level.FINE, "free global lock");
            context.threadWithGlobalLock = null;
            context.isGlobalLockAcquired.set(false);
        }
    }
}
