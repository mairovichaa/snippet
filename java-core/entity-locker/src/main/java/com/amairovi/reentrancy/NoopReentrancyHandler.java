package com.amairovi.reentrancy;

public class NoopReentrancyHandler<T> implements ReentrancyHandler<T> {
    @Override
    public void increase(T id) {
    }

    @Override
    public boolean increaseIfPresent(T id) {
        return false;
    }

    @Override
    public int decrease(T id) {
        return 0;
    }

    @Override
    public Integer getReentrancyNumber(T id) {
        throw new UnsupportedOperationException();
    }
}
