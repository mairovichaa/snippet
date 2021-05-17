package com.amairovi;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

class GlobalLock {
    private final AtomicBoolean isGlobalLockAcquired = new AtomicBoolean(false);
    private volatile Thread threadWithGlobalLock;
    private final AtomicInteger globalLockReentrancy = new AtomicInteger();

    void lock() {

    }

    void unlock() {

    }
}
