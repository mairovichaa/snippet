package com.amairovi.goetz_concurrency_in_practice.chapter_7;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class WebCrawler {

    private volatile TrackingExecutor executor;

    private final Set<URL> urlsToCrawl = new HashSet<>();

    public synchronized void start() {
        executor = new TrackingExecutor(Executors.newCachedThreadPool());
        for (URL url : urlsToCrawl) {
            submitCrawlTask(url);
        }
        urlsToCrawl.clear();
    }

    public synchronized void stop() throws InterruptedException {
        try {
            saveUncrawled(executor.shutdownNow());
            if (executor.awaitTermination(3, TimeUnit.SECONDS)) {
                saveUncrawled(executor.getCancelledTasks());
            }
        } finally {
            executor = null;
        }
    }

    protected abstract List<URL> processPage(URL url);

    private void saveUncrawled(List<Runnable>
                                       uncrawled) {
        for (Runnable task : uncrawled) {
            CrawlTask crawlTask = (CrawlTask) task;
            urlsToCrawl.add(crawlTask.getPage());
        }
    }

    private void submitCrawlTask(URL u) {
        executor.execute(new CrawlTask(u));
    }

    private class CrawlTask implements Runnable {

        private final URL url;

        private CrawlTask(URL url) {
            this.url = url;
        }

        @Override
        public void run() {
            for (URL link : processPage(url)) {
                if (Thread.currentThread().isInterrupted()) {
                    return;
                }
                submitCrawlTask(link);
            }
        }

        public URL getPage() {
            return url;
        }

    }

}
