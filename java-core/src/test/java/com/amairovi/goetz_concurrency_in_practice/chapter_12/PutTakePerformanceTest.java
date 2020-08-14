package com.amairovi.goetz_concurrency_in_practice.chapter_12;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PutTakePerformanceTest {

    private static final ExecutorService pool = Executors.newCachedThreadPool();

    private static class PutTakeTestInner {

        private final AtomicInteger putSum = new AtomicInteger(0);
        private final AtomicInteger takeSum = new AtomicInteger(0);
        private final CyclicBarrier barrier;
        private final BarrierTimer timer;
        private final BoundedBuffer<Integer> underTest;
        private final int nTrials, nPairs;

        private PutTakeTestInner(int capacity, int nPairs, int nTrials) {
            this.underTest = new BoundedBuffer<>(capacity);
            this.nPairs = nPairs;
            this.nTrials = nTrials;
            timer = new BarrierTimer();
            this.barrier = new CyclicBarrier(nPairs * 2 + 1, timer);
        }

        private void test() {
            try {
                timer.clear();
                for (int i = 0; i < nPairs; i++) {
                    pool.execute(new Producer());
                    pool.execute(new Consumer());
                }
                barrier.await(); // wait for all threads to be ready
                barrier.await(); // wait for all threads to finish

                long nsPerItem = timer.getTime() / (nPairs * nTrials);
                System.out.println("Throughput: " + nsPerItem + " ns/item");

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

        class BarrierTimer implements Runnable {

            private boolean started;
            private long startTime, endTime;

            @Override
            public void run() {
                long t = System.nanoTime();
                if (!started) {
                    started = true;
                    startTime = t;
                } else {
                    endTime = t;
                }
            }

            public synchronized void clear() {
                started = false;
            }

            public synchronized long getTime() {
                return endTime - startTime;
            }

        }

    }


    @Test
    @Timeout(10)
    void testBB() {
        new PutTakeTestInner(10, 10, 100000).test();
        pool.shutdown();
    }


    @Test
    void test2() throws InterruptedException {
        int ptp = 100_000; // trials per thread
        for (int cap = 10; cap <= 1000; cap *= 10) {
            System.out.println("Capacity: " + cap);
            for (int pairs = 1; pairs <= 128; pairs *= 2) {
                PutTakeTestInner t = new PutTakeTestInner(cap, pairs, ptp);
                System.out.println("Pairs: " + pairs + "\t");
                t.test();
                System.out.println("\t");
                Thread.sleep(1000);
                t.test();
                System.out.println();
                Thread.sleep(1000);
            }

        }
        pool.shutdown();
    }


    static int xorShift(int y) {
        y ^= (y << 6);
        y ^= (y >>> 21);
        y ^= (y << 7);
        return y;
    }

}

