package com.amairovi.goetz_concurrency_in_practice.chapter_15;

import com.amairovi.goetz_concurrency_in_practice.ThreadSafe;

@ThreadSafe
public class CasCounter {
    private SimulatedCAS value;

    public int getValue() {
        return value.get();
    }

    public int inc() {
        int v;
        do {
            v = value.get();
        } while (v != value.compareAndSwap(v, v + 1));
        return v + 1;
    }
}
