package com.amairovi;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultEntityLocker<T> implements EntityLocker<T> {
    private final static Logger log = Logger.getLogger(DefaultEntityLocker.class.getName());

    private final ReentrancyHandler<? super T> reentrancyHandler;
    private final Map<T, Boolean> locked = new ConcurrentHashMap<>();

    public DefaultEntityLocker(ReentrancyHandler<? super T> reentrancyHandler) {
        this.reentrancyHandler = reentrancyHandler;
    }

    @Override
    public boolean lock(final T id, final long timeout, final TimeUnit unit) {
        log.log(Level.FINE, "trying to lock " + id + " with timeout");
        Instant shouldStopLockingAt = Instant.now().plus(unit.toNanos(timeout), ChronoUnit.NANOS);
        return lock(id, shouldStopLockingAt);
    }

    @Override
    public void lock(final T id) {
        lock(id, null);
    }

    private boolean lock(final T id, final Instant shouldStopLockingAt) {
        log.log(Level.FINE, "trying to lock " + id);
        while (true) {
            Boolean previous = locked.putIfAbsent(id, Boolean.TRUE);

            if (previous == null) {
                reentrancyHandler.increase(id);
                log.log(Level.FINE, "lock for " + id + " is free");
                return true;
            } else {
                if (reentrancyHandler.increaseIfPresent(id)) {
                    return true;
                }
                if (shouldStopLockingAt != null && Instant.now().isAfter(shouldStopLockingAt)) {
                    log.log(Level.FINE, "timeout has elapsed. Stop trying to lock " + id);
                    return false;
                }
                log.log(Level.FINE, "lock for " + id + " is not free");
            }
        }
    }

    @Override
    public void unlock(final T id) {
        log.log(Level.FINE, "unlocking " + id);
        Boolean isLocked = locked.get(id);
        if (isLocked == null) {
            log.log(Level.INFO, "trying to unlock not locked " + id);
            return;
        }

        if (reentrancyHandler.decrease(id) == 0) {
            locked.remove(id);
        }
    }
}
