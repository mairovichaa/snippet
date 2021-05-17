package com.amairovi;

import java.util.Set;

public class DefaultDeadlockDetector<T> implements DeadlockDetector<T> {

    @Override
    public void check(T id, Thread thread, LockingData<? extends T> lockingData) {
        if (!lockingData.threadToOwnedEntities.containsKey(thread)) {
            return;
        }

        Set<? extends T> entitiesOwnedByCurrentThread = lockingData.threadToOwnedEntities.get(thread);
        Thread threadToCheck = lockingData.acquiredEntityToOwnerThread.get(id);
        while (threadToCheck != null) {
            T entityWhichThreadToCheckTriesToLock = lockingData.threadToEntityForWhichLockAcquiringInProcessIfAny.get(threadToCheck);

            // Needed entity has been acquired by threadToCheck, which currently tries to acquire global lock.
            // Current thread will wait for needed entity to be freed and threadToCheck will wait until current thread
            // frees all acquired locks => deadlock.
            if (lockingData.threadsTryingToAcquireGlobalLock.contains(threadToCheck)) {
                // TODO: add info about deadlock (path)
                throw new DeadlockDetectedException();
            }

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

            threadToCheck = lockingData.acquiredEntityToOwnerThread.get(entityWhichThreadToCheckTriesToLock);
        }
    }

}
