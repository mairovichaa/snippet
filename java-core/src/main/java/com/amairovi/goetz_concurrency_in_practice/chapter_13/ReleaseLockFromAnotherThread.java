package com.amairovi.goetz_concurrency_in_practice.chapter_13;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReleaseLockFromAnotherThread {
    public static void main(String[] args) {
        Lock lock = new ReentrantLock();

        lock.lock();

        new Thread(() -> {
            try {
                lock.unlock();
            } catch (IllegalMonitorStateException e) {
                System.out.println("Caught exception");
                e.printStackTrace();
            }
        }).start();

    }
}
