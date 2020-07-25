package com.amairovi.goetz_concurrency_in_practice.chapter_5;

import java.util.concurrent.BlockingQueue;

public class RestoreInterruptStatus implements Runnable{

    BlockingQueue<Integer> queue;

    @Override
    public void run() {

        try {
            queue.take();
        } catch (InterruptedException e) {
            // restore interrupted status
            Thread.currentThread().interrupt();
        }
    }

}
