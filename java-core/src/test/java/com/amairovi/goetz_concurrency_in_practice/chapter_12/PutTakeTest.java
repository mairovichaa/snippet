package com.amairovi.goetz_concurrency_in_practice.chapter_12;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PutTakeTest {

    private static final ExecutorService pool = Executors.newCachedThreadPool();

    private class PutTakeTestInner {

        private final AtomicInteger putSum = new AtomicInteger(0);
        private final AtomicInteger takeSum = new AtomicInteger(0);
        private final CyclicBarrier barrier;
        private final BoundedBuffer<Integer> underTest;
        private final int nTrials, nPairs;

        private PutTakeTestInner(int capacity, int nPairs, int nTrials) {
            this.underTest = new BoundedBuffer<>(capacity);
            this.nPairs = nPairs;
            this.nTrials = nTrials;
            this.barrier = new CyclicBarrier(nPairs * 2 + 1);

        }

        private void test() {
            try {
                for (int i = 0; i < nPairs; i++) {
                    pool.execute(new Producer());
                    pool.execute(new Consumer());
                }
                barrier.await(10, TimeUnit.SECONDS); // wait for all threads to be ready
                barrier.await(10, TimeUnit.SECONDS); // wait for all threads to finish
                assertEquals(putSum.get(), takeSum.get());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        class Producer implements Runnable {

            @Override
            public void run() {
                try {
                    int seed = (this.hashCode() ^ (int) System.nanoTime());
                    int sum = 0;
                    barrier.await();
                    for (int i = 0; i < nTrials; i++) {
                        underTest.put(seed);
                        sum += seed;
                        seed = xorShift(seed);
                    }
                    putSum.getAndAdd(sum);
                    barrier.await();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

        }

        class Consumer implements Runnable {

            @Override
            public void run() {
                try {
                    barrier.await();
                    int sum = 0;
                    for (int i = 0; i < nTrials; i++) {
                        sum += underTest.take();
                    }
                    takeSum.getAndAdd(sum);
                    barrier.await();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

        }

    }


    @Test
    @Timeout(10)
    void testBB() {
        new PutTakeTestInner(10, 10, 100000).test();
        pool.shutdown();
    }


    static int xorShift(int y) {
        y ^= (y << 6);
        y ^= (y >>> 21);
        y ^= (y << 7);
        return y;
    }

}
