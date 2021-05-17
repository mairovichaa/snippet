package com.amairovi;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

class LockingContext<T>{
    final Map<Thread, T> threadToEntityForWhichLockAcquiringInProcessIfAny = new ConcurrentHashMap<>();
    final Map<Thread, Set<T>> threadToOwnedEntities = new ConcurrentHashMap<>();
    final Map<T, Thread> acquiredEntityToOwnerThread = new ConcurrentHashMap<>();
    final Set<Thread> threadsTryingToAcquireGlobalLock = Collections.newSetFromMap(new ConcurrentHashMap<>());
    final AtomicInteger totalAmountOfLocked = new AtomicInteger();
    final AtomicInteger totalAmountOfAcquiresInProcess = new AtomicInteger();

    final Map<T, Boolean> locked = new ConcurrentHashMap<>();
    final AtomicBoolean isGlobalLockAcquired = new AtomicBoolean(false);
    volatile Thread threadWithGlobalLock;
    final AtomicInteger globalLockReentrancy = new AtomicInteger();


    public void addLockOwning(T id, Thread thread) {
        totalAmountOfLocked.getAndIncrement();
        acquiredEntityToOwnerThread.put(id, thread);
        if (threadToOwnedEntities.containsKey(thread)) {
            threadToOwnedEntities.get(thread).add(id);
        } else {
            Set<T> acquiredEntities = new HashSet<>();
            acquiredEntities.add(id);
            threadToOwnedEntities.put(thread, acquiredEntities);
        }
    }

    public void removeLockOwning(T id, Thread thread) {
        totalAmountOfLocked.getAndDecrement();
        acquiredEntityToOwnerThread.remove(id);
        threadToOwnedEntities.get(thread).remove(id);
    }

    public void addLockAcquiring(T id, Thread thread) {
        totalAmountOfAcquiresInProcess.getAndIncrement();
        threadToEntityForWhichLockAcquiringInProcessIfAny.put(thread, id);
    }

    public void removeLockAcquiring(Thread thread) {
        totalAmountOfAcquiresInProcess.decrementAndGet();
        threadToEntityForWhichLockAcquiringInProcessIfAny.remove(thread);
    }

    public void addGlobalLockAcquiring(Thread thread) {
        threadsTryingToAcquireGlobalLock.add(thread);
    }

    public void removeGlobalLockAcquiring(Thread thread) {
        threadsTryingToAcquireGlobalLock.remove(thread);
    }
}
