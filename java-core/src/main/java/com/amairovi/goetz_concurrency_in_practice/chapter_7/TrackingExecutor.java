package com.amairovi.goetz_concurrency_in_practice.chapter_7;

import java.util.*;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class TrackingExecutor extends AbstractExecutorService {

    private final ExecutorService executor;
    private final Set<Runnable> tasksCancelledAtShutdown =
            Collections.synchronizedSet(new HashSet<>());

    public TrackingExecutor(ExecutorService executor) {
        this.executor = executor;
    }

    public List<Runnable> getCancelledTasks() {
        if (!executor.isTerminated()) {
            throw new IllegalStateException("Executor hasn't been cancelled yet.");
        }

        return new ArrayList<>(tasksCancelledAtShutdown);
    }

    @Override
    public void shutdown() {
        executor.shutdown();

    }

    @Override
    public List<Runnable> shutdownNow() {
        return executor.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return executor.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return executor.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return executor.awaitTermination(timeout, unit);
    }

    @Override
    public void execute(Runnable runnable) {
        executor.execute(() -> {
            try {
                runnable.run();
            } finally {
                if (isShutdown()
                        // runnable has to preserve interruption status

                        // race condition
                        // could be false-positive - shut down took place between
                        // the last instruction of the task executes and
                        // the pool records the task as complete
                        // So either tasks have to be idempotent
                        // or application has to handle such situation itself
                        && Thread.currentThread().isInterrupted()) {
                    tasksCancelledAtShutdown.add(runnable);
                }
            }
        });
    }

}
