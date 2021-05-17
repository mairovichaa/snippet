package com.amairovi;

import java.util.Set;

public class DefaultDeadlockDetector<T> implements DeadlockDetector<T> {

    @Override
    public void check(T id, Thread thread, LockingContext<? extends T> context) {
        if (!context.threadToOwnedEntities.containsKey(thread)) {
            return;
        }

        Set<? extends T> entitiesOwnedByCurrentThread = context.threadToOwnedEntities.get(thread);
        Thread threadToCheck = context.acquiredEntityToOwnerThread.get(id);
        while (threadToCheck != null) {
            T entityWhichThreadToCheckTriesToLock = context.threadToEntityForWhichLockAcquiringInProcessIfAny.get(threadToCheck);

            // Needed entity has been acquired by threadToCheck, which currently tries to acquire global lock.
            // Current thread will wait for needed entity to be freed and threadToCheck will wait until current thread
            // frees all acquired locks => deadlock.
            if (context.threadsTryingToAcquireGlobalLock.contains(threadToCheck)) {
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

            threadToCheck = context.acquiredEntityToOwnerThread.get(entityWhichThreadToCheckTriesToLock);
        }
    }

}
