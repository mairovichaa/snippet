package com.amairovi.goetz_concurrency_in_practice.chapter_5;

import java.util.concurrent.CountDownLatch;

public class TestHarness {

    public static long timeTasks(int nThreads, final Runnable task) throws InterruptedException {
        final CountDownLatch startGate = new CountDownLatch(1);
        final CountDownLatch endGate = new CountDownLatch(nThreads);

        for (int i = 0; i < nThreads; i++) {
            Thread t = new Thread(() -> {
                try {
                    startGate.await();
                    try {
                        task.run();
                    } finally {
                        endGate.countDown();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            t.start();
        }

        long startAt = System.nanoTime();

        startGate.countDown();
        endGate.await();

        long endAt = System.nanoTime();

        return endAt - startAt;
    }

    public static void main(String[] args) throws InterruptedException {
        timeTasks(10, () -> System.out.println(Thread.currentThread().getName()));
    }

}
