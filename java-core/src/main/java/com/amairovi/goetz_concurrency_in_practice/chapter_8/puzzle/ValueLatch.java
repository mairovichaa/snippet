package com.amairovi.goetz_concurrency_in_practice.chapter_8.puzzle;

import com.amairovi.goetz_concurrency_in_practice.ThreadSafe;
import com.amairovi.goetz_concurrency_in_practice.GuardedBy;

import java.util.concurrent.CountDownLatch;

@ThreadSafe
public class ValueLatch<T> {

    @GuardedBy("this")
    private T value = null;
    private final CountDownLatch done = new CountDownLatch(1);

    public synchronized void setValue(T newValue) {
        if (!isSet()) {
            value = newValue;
            done.countDown();
        }
    }

    public boolean isSet() {
        return done.getCount() == 0;
    }

    public T getValue() throws InterruptedException {
        done.await();
        synchronized (this) {
            return value;
        }
    }

}
