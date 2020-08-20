package com.amairovi.goetz_concurrency_in_practice.chapter_14;

import com.amairovi.goetz_concurrency_in_practice.ThreadSafe;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

@ThreadSafe
public class OneShotLatch {
    private static final int IGNORED = 0;

    private final Sync sync = new Sync();

    public void signal() {
        sync.releaseShared(IGNORED);
    }

    public void await() throws InterruptedException {
        sync.acquireSharedInterruptibly(IGNORED);
    }


    private class Sync extends AbstractQueuedSynchronizer {
        @Override
        protected int tryAcquireShared(int ignored) {
            // Succeed if latch is open (state == 1), else fail
            return getState() == 1 ? 1 : 0;
        }

        @Override
        protected boolean tryReleaseShared(int ignored) {
            setState(1);
            return true;
        }
    }

    @RequiredArgsConstructor
    public static class Test implements Runnable{
        private final int num;
        private final OneShotLatch latch;

        @Override
        public void run() {
            try {
                latch.await();
                System.out.println(num);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        OneShotLatch latch = new OneShotLatch();

        new Thread(new Test(1, latch)).start();
        new Thread(new Test(2, latch)).start();
        new Thread(new Test(3, latch)).start();
        new Thread(new Test(4, latch)).start();

        latch.signal();
    }
}
