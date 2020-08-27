package com.amairovi.goetz_concurrency_in_practice.chapter_15.random;

public abstract class PseudoRandom {

    protected int calculateNext(int seed) {
        return seed + 10;
    }
}
