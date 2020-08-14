package com.amairovi.goetz_concurrency_in_practice.chapter_12;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestingThreadFactoryTest {


    static class TestingThreadFactory implements ThreadFactory {

        public final AtomicInteger numCreated = new AtomicInteger();

        private final ThreadFactory factory = Executors.defaultThreadFactory();

        @Override
        public Thread newThread(Runnable r) {
            numCreated.incrementAndGet();
            return factory.newThread(r);
        }

    }


    @Test
    void testPoolExpansion() throws InterruptedException {
        int MAX_SIZE = 10;
        TestingThreadFactory threadFactory = new TestingThreadFactory();
        ExecutorService executor = Executors.newFixedThreadPool(MAX_SIZE, threadFactory);
        for (int i = 0; i < 10 * MAX_SIZE; i++) {
            executor.execute(() -> {
                try {
                    Thread.sleep(Long.MAX_VALUE);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        for (int i = 0; i < 20 && threadFactory.numCreated.get() < MAX_SIZE; i++) {
            Thread.sleep(100);
        }

        assertEquals(threadFactory.numCreated.get(), MAX_SIZE);
        executor.shutdownNow();
    }

}
