package com.amairovi.goetz_concurrency_in_practice.chapter_5;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProducerConsumerPattern {

    private static class Producer implements Runnable {

        private final BlockingQueue<Integer> queue;
        private final Random random;

        private Producer(BlockingQueue<Integer> queue) {
            this.queue = queue;
            random = new Random();
        }

        @Override
        public void run() {
            while (true) {
                try {
                    boolean b = random.nextBoolean();
                    if (b) {
                        int next = random.nextInt();
                        System.out.println("Put: " + next);
                        queue.put(next);
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private static class Consumer implements Runnable {

        private final BlockingQueue<Integer> queue;

        private Consumer(BlockingQueue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Integer next = queue.take();
                    System.out.println("Take: " + next);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();


        int amountOfProducers = 3;
        for (int i = 0; i < amountOfProducers; i++) {
            new Thread(new Producer(queue)).start();
        }

        int amountOfConsumers = 1;
        for (int i = 0; i < amountOfConsumers; i++) {
            new Thread(new Consumer(queue)).start();
        }
    }

}
