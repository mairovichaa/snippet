package com.amairovi;

import java.util.concurrent.TimeUnit;

public interface EntityLocker<T> {
    void lock(T id);

    boolean lock(T id, long timeout, TimeUnit unit);

    void unlock(T id);
}
