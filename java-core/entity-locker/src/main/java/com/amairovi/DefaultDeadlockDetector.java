package com.amairovi;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultDeadlockDetector<T> implements DeadlockDetector<T> {

    private final Map<Thread, T> threadToEntityForWhichLockAcquiringInProcessIfAny = new ConcurrentHashMap<>();
    private final Map<Thread, Set<T>> threadToOwnedEntities = new ConcurrentHashMap<>();
    private final Map<T, Thread> acquiredEntityToOwnerThread = new ConcurrentHashMap<>();

    @Override
    public void check(T id, Thread thread) {
        if (!threadToOwnedEntities.containsKey(thread)) {
            return;
        }

        Set<T> entitiesOwnedByCurrentThread = threadToOwnedEntities.get(thread);
        Thread threadToCheck = acquiredEntityToOwnerThread.get(id);
        while (threadToCheck != null) {
            T entityWhichThreadToCheckTriesToLock = threadToEntityForWhichLockAcquiringInProcessIfAny.get(threadToCheck);

            // There is no deadlock.
            // Other thread simply locks the entity, which current thread tries to lock.
            if (entityWhichThreadToCheckTriesToLock == null) {
                return;
            }

            // The other thread tries to lock the entity, which current thread has already locked.
            // Meantime, current thread tries to lock the entity, which the other thread has already locked.
            // There is a deadlock.
            if (entitiesOwnedByCurrentThread.contains(entityWhichThreadToCheckTriesToLock)) {
                // TODO: add info about deadlock (path)
                throw new DeadlockDetectedException();
            }

            threadToCheck = acquiredEntityToOwnerThread.get(entityWhichThreadToCheckTriesToLock);
        }
    }

    @Override
    public void addLockOwning(T id, Thread thread) {
        acquiredEntityToOwnerThread.put(id, thread);
        if (threadToOwnedEntities.containsKey(thread)) {
            threadToOwnedEntities.get(thread).add(id);
        } else {
            Set<T> acquiredEntities = new HashSet<>();
            acquiredEntities.add(id);
            threadToOwnedEntities.put(thread, acquiredEntities);
        }
    }

    @Override
    public void removeLockOwning(T id, Thread thread) {
        acquiredEntityToOwnerThread.remove(id);
        threadToOwnedEntities.get(thread).remove(id);
    }

    @Override
    public void addLockAcquiring(T id, Thread thread) {
        threadToEntityForWhichLockAcquiringInProcessIfAny.put(thread, id);
    }

    @Override
    public void removeLockAcquiring(T id, Thread thread) {
        threadToEntityForWhichLockAcquiringInProcessIfAny.remove(thread);
    }
}
