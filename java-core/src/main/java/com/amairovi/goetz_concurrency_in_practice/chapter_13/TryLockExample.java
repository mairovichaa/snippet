package com.amairovi.goetz_concurrency_in_practice.chapter_13;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class TryLockExample {
    static class Account {
        Lock lock = new ReentrantLock();

        public long getBalance() {
            return Long.MAX_VALUE;
        }

        public void debit(long amount) {
        }

        public void credit(long amount) {
        }

    }

    private final Random random = new Random();

    boolean transferMoney(Account from, Account to, long amount, long timeout, TimeUnit unit) throws InterruptedException {
        long fixedDelay = 100;
        long randMod = 1000;
        long stopTime = System.nanoTime() + unit.toNanos(timeout);
        while (true) {
            if (from.lock.tryLock()) {
                try {
                    if (to.lock.tryLock()) {
                        try {
                            if (from.getBalance() < amount) {
                                throw new IllegalStateException();
                            } else {
                                from.debit(amount);
                                to.credit(amount);
                                return true;
                            }
                        } finally {
                            to.lock.unlock();
                        }
                    }
                } finally {
                    from.lock.unlock();
                }
            }
            if (System.nanoTime() < stopTime) {
                return false;
            }
            NANOSECONDS.sleep(fixedDelay + random.nextLong() % randMod);
        }
    }

    boolean transferMoneyWithUtilityMethod(Account from, Account to, long amount, long timeout, TimeUnit unit) throws InterruptedException {
        long fixedDelay = 100;
        long randMod = 1000;
        long stopTime = System.nanoTime() + unit.toNanos(timeout);

        Supplier<Boolean> transfer = () -> {
            if (from.getBalance() < amount) {
                throw new IllegalStateException();
            } else {
                from.debit(amount);
                to.credit(amount);
                return true;
            }
        };

        while (true) {
            boolean result = exec(from.lock, () -> exec(to.lock, transfer));

            if (result) {
                return true;
            }

            if (System.nanoTime() < stopTime) {
                return false;
            }
            NANOSECONDS.sleep(fixedDelay + random.nextLong() % randMod);
        }
    }

    private boolean exec(Lock lock, Supplier<Boolean> runnable) {
        if (lock.tryLock()) {
            try {
                return runnable.get();
            } finally {
                lock.unlock();
            }
        }
        return false;
    }


}
