package com.amairovi;

public interface DeadlockDetector<T> {

    void check(T id, Thread thread, LockingContext<? extends T> context);
}
