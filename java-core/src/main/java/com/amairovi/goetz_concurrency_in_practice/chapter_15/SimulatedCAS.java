package com.amairovi.goetz_concurrency_in_practice.chapter_15;

import com.amairovi.goetz_concurrency_in_practice.GuardedBy;
import com.amairovi.goetz_concurrency_in_practice.ThreadSafe;

@ThreadSafe
public class SimulatedCAS {
    @GuardedBy("this")
    private int value;

    public synchronized int get() {
        return value;
    }

    public synchronized boolean compareAndSet(int expectedValue, int newValue) {
        return expectedValue == compareAndSwap(expectedValue, newValue);
    }

    // suppose that method is called twice with
    // 2 4 and 1 4 and initial value == 2
    // first call will result in 2, which means that it successfully pass,
    // but another one will result in 4, which means a failure as 1 is expected as initial value
    // so there will be a need to run 4 7
    public synchronized int compareAndSwap(int expectedValue, int newValue) {
        int oldValue = value;
        if (value == expectedValue) {
            value = newValue;
        }
        return oldValue;
    }

    // suppose that method is called twice with
    // 2 4 and 1 4 and initial value == 2
    // it doesn't matter in which order method will be called both will result in 4
    // in such case user of this method won't be able to verify if value has been updated
    public synchronized int invalidCompareAndSwap(int expectedValue, int newValue) {
        if (value == expectedValue) {
            value = newValue;
        }
        return value;
    }
}
