package com.amairovi;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultEntityLocker<T> implements EntityLocker<T> {
    private final static Logger log = Logger.getLogger(DefaultEntityLocker.class.getName());

    private final Map<T, Boolean> locked = new ConcurrentHashMap<>();

    @Override
    public void lock(final T id) {
        log.log(Level.FINE, "trying to lock " + id);
        while (true) {
            Boolean previous = locked.putIfAbsent(id, Boolean.TRUE);

            if (previous == null) {
                log.log(Level.FINE, "lock for " + id + " is free");
                return;
            } else {
                log.log(Level.FINE, "lock for " + id + " is not free");
            }
        }
    }

    @Override
    public void unlock(final T id) {
        log.log(Level.FINE, "unlocking " + id);
        locked.remove(id);
    }
}
