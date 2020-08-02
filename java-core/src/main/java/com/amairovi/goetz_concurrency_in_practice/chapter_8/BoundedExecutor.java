package com.amairovi.goetz_concurrency_in_practice.chapter_8;

import com.amairovi.goetz_concurrency_in_practice.ThreadSafe;

import java.util.concurrent.*;

@ThreadSafe
public class BoundedExecutor {

    private final Executor executor;
    private final Semaphore semaphore;

    public BoundedExecutor(Executor executor, Semaphore semaphore) {
        this.executor = executor;
        this.semaphore = semaphore;
    }

    public void submitTask(final Runnable command) throws InterruptedException {
        // block when queue is full
        semaphore.acquire();
        try {
            executor.execute(() -> {
                try {
                    command.run();
                } finally {
                    semaphore.release();
                }
            });
        } catch (RejectedExecutionException e) {
            semaphore.release();
        }

    }

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        Semaphore semaphore = new Semaphore(8);

        // it allows 8 tasks to be queued
        new BoundedExecutor(executor, semaphore);

    }

}
