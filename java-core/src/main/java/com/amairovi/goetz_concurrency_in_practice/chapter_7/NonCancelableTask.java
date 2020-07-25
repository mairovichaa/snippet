package com.amairovi.goetz_concurrency_in_practice.chapter_7;

import java.util.concurrent.BlockingQueue;

public class NonCancelableTask {

    public Object getNextObject(BlockingQueue<Object> queue) {
        boolean interrupted = false;

        try {
            while (true) {
                try {
                    return queue.take();
                } catch (InterruptedException e) {
                    interrupted = true;
                }
            }
        } finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

}
