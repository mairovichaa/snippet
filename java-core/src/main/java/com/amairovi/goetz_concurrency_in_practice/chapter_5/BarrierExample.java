package com.amairovi.goetz_concurrency_in_practice.chapter_5;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class BarrierExample {

    private static class Cycle implements Runnable {

        private final CyclicBarrier barrier;

        private String name;

        private int cycleNum = 1;

        private Cycle(CyclicBarrier barrier) {
            this.barrier = barrier;
        }


        @Override
        public void run() {
            this.name = Thread.currentThread().getName();
            while (true) {
                try {
                    Thread.sleep(1000);
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }

                System.out.println(name + ": cycle #" + cycleNum);
                cycleNum++;
            }
        }

    }

    private static class CycleAnnouncer implements Runnable {

        private int cycleNum = 1;


        @Override
        public void run() {
            System.out.println("cycle #" + cycleNum + " begins");
            cycleNum++;
        }

    }

    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(5, new CycleAnnouncer());
        for (int i = 0; i < 5; i++) {
            new Thread(new Cycle(barrier)).start();
        }
    }

}
