package com.amairovi;

public interface DeadlockDetector<T> {
    void check(T id, Thread thread);

    void addLockOwning(T id, Thread thread);

    void removeLockOwning(T id, Thread thread);

    void addLockAcquiring(T id, Thread thread);

    void removeLockAcquiring(T id, Thread thread);
}
