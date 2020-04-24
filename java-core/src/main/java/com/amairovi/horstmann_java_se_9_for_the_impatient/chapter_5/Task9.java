package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_5;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.locks.ReentrantLock;

public class Task9 {

    @RequiredArgsConstructor
    public static class ReentrantLockAutoCloseable implements AutoCloseable {

        private final ReentrantLock lock;

        @Override
        public void close() {
            lock.unlock();
        }

    }

    public static ReentrantLockAutoCloseable lock(ReentrantLock lock) {
        lock.lock();
        return new ReentrantLockAutoCloseable(lock);
    }

}
