package com.amairovi.goetz_concurrency_in_practice.chapter_10;

import static java.util.concurrent.TimeUnit.SECONDS;

public class LeftRightDeadlock {

    private final Object left = new Object();
    private final Object right = new Object();

    public void leftRight() {
        synchronized (left) {
            synchronized (right) {

            }
        }
    }

    public void rightLeft() {
        synchronized (right) {
            synchronized (left) {

            }
        }
    }

    // deadlock sample
    public static void main(String[] args) {
        Object o1 = new Object();
        Object o2 = new Object();

        acquireLocks(o1, o2);
        acquireLocks(o2, o1);
    }

    private static void acquireLocks(Object lock1, Object lock2) {
        Runnable runnable = () -> {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + ": Trying to acquire lock 1");
            synchronized (lock1) {
                System.out.println(threadName + ": Lock 1 is acquired");
                try {
                    SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(threadName + ": Trying to acquire lock 2");
                synchronized (lock2) {
                    System.out.println(threadName + ": Lock 2 is acquired");
                }
            }
        };

        new Thread(runnable).start();
    }

}
