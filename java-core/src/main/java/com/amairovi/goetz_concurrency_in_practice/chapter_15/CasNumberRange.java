package com.amairovi.goetz_concurrency_in_practice.chapter_15;

import com.amairovi.goetz_concurrency_in_practice.Immutable;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.atomic.AtomicReference;

public class CasNumberRange {
    @Immutable
    @RequiredArgsConstructor
    private static class IntPair {
        // Invariant: lower <= upper
        final int lower;
        final int upper;
    }

    private final AtomicReference<IntPair> values = new AtomicReference<>(new IntPair(0, 0));

    public int getLower() {
        return values.get().lower;
    }

    public int getUpper() {
        return values.get().upper;
    }

    public void setLower(int newLower) {
        while (true) {
            IntPair oldValue = values.get();
            if (newLower > oldValue.upper) {
                throw new IllegalArgumentException("Can't set lower to " + newLower + "( > upper )");
            }
            IntPair newValue = new IntPair(newLower, oldValue.upper);
            if (values.compareAndSet(oldValue, newValue)) {
                return;
            }
        }
    }

    // similarly for setUpper

}
