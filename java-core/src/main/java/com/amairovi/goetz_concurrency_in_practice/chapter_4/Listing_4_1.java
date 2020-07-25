package com.amairovi.goetz_concurrency_in_practice.chapter_4;

public class Listing_4_1 {

    private static class Counter {

        private long value = 0;

        public synchronized long getValue() {
            return value;
        }

        public synchronized long increment() {
            if (value == Long.MAX_VALUE) {
                throw new IllegalStateException("counter overflow");
            }
            return ++value;
        }

    }

}
