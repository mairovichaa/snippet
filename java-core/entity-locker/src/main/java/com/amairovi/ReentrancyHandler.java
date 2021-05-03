package com.amairovi;

public interface ReentrancyHandler<T> {

    void increase(T id);

    boolean increaseIfPresent(T id);

    int decrease(T id);

    Integer getReentrancyNumber(T id);
}
