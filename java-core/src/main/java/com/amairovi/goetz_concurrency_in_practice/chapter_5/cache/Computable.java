package com.amairovi.goetz_concurrency_in_practice.chapter_5.cache;

public interface Computable<A, V> {

    V compute(A arg) throws InterruptedException;
}
