package com.amairovi.reentrancy;

import com.amairovi.NotThreadSafe;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@NotThreadSafe
public class DefaultReentrancyHandler<T> implements ReentrancyHandler<T> {
    private final static Logger log = Logger.getLogger(DefaultReentrancyHandler.class.getName());

    private final ThreadLocal<Map<T, Integer>> reentrancyMap = ThreadLocal.withInitial(HashMap::new);

    @Override
    public void increase(T id) {
        log.log(Level.FINE, () -> "increase amount of reentrancy for " + id);
        getReentrancyMap().merge(id, 1, (oldVal, ignore) -> oldVal + 1);
    }

    @Override
    public boolean increaseIfPresent(T id) {
        log.log(Level.FINE, () -> "increase amount of reentrancy for " + id + " if it's present");
        Integer amountOfLocks = getReentrancyMap().get(id);
        if (amountOfLocks != null) {
            getReentrancyMap().put(id, amountOfLocks + 1);
            return true;
        }
        log.log(Level.FINE, () -> id + " is not present");
        return false;
    }

    @Override
    public int decrease(T id) {
        log.log(Level.FINE, () -> "decrease amount of reentrancy for " + id);
        Integer amountOfLocks = getReentrancyMap().get(id);
        int newAmountOfLocks = amountOfLocks - 1;
        if (newAmountOfLocks == 0) {
            getReentrancyMap().remove(id);
        } else {
            getReentrancyMap().put(id, newAmountOfLocks);
        }
        log.log(Level.FINE, () -> "new amount of reentrancy for " + id + " is " + newAmountOfLocks);
        return newAmountOfLocks;
    }

    @Override
    public Integer getReentrancyNumber(T id) {
        return getReentrancyMap().get(id);
    }

    private Map<T, Integer> getReentrancyMap() {
        return reentrancyMap.get();
    }
}
