package com.amairovi.goetz_concurrency_in_practice.chapter_7;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class IndexingService {

    private static final File POISON = new File("");
    private final IndexerThread consumer = new IndexerThread();
    private final CrawlerThread producer = new CrawlerThread();

    private final BlockingQueue<File> queue = new LinkedBlockingDeque<>();
    private final File root;

    public IndexingService(File root) {
        this.root = root;
    }

    public void start() {
        producer.start();
        consumer.start();
    }

    public void stop() {
        producer.interrupt();
    }

    public void awaitTermination() throws InterruptedException {
        consumer.join();
    }


    private class CrawlerThread extends Thread {

        @Override
        public void run() {
            try {
                crawl(root);
            } catch (InterruptedException e) {
                /* fall through */
            } finally {
                while (true) {
                    try {
                        queue.put(POISON);
                        break;
                    } catch (InterruptedException e1) {
                        /* retry */
                    }
                }
            }
        }

        private void crawl(File root) throws InterruptedException {
            /* crawl */
            // check if interrupted from time to time
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
        }

    }

    private class IndexerThread extends Thread {

        @Override
        public void run() {
            try {
                while (true) {
                    File file = queue.take();
                    if (file == POISON) {
                        break;
                    } else {
                        indexFile(file);
                    }
                }
            } catch (InterruptedException consumed) {

            }
        }

        private void indexFile(File file) {
            /* index file */
        }

    }

}
