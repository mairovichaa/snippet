package com.amairovi.goetz_concurrency_in_practice.chapter_7;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.SECONDS;

public class LogServiceWithExecutor {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final PrintWriter writer;

    public LogServiceWithExecutor(Writer writer) {
        this.writer = new PrintWriter(writer);
    }

    private void start() {
    }

    public void stop() throws InterruptedException {
        try {
            executor.shutdown();
            executor.awaitTermination(3, SECONDS);
        } finally {
            writer.close();
        }
    }

    public void log(String msg) {
        try {
            executor.execute(() -> writer.println(msg));
        } catch (RejectedExecutionException ignored) {

        }
    }

}
