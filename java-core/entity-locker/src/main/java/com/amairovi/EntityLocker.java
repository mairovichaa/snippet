package com.amairovi;

public interface EntityLocker<T> {
    void lock(T id);
    void unlock(T id);
}
