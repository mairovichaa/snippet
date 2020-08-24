package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_10;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class Task27 {

    private static class CancelableFuture<T> extends CompletableFuture<T> {
        private volatile Thread thread;

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            System.out.println("cancel is called");
            if (thread != null && mayInterruptIfRunning) {
                System.out.println("interrupt thread");
                thread.interrupt();
            }
            return super.cancel(mayInterruptIfRunning);
        }
    }

    public static <T> CompletableFuture<T> supplyAsync(Supplier<T> action, Executor exec) {
        CancelableFuture<T> future = new CancelableFuture<>();

        exec.execute(() -> {
            future.thread = Thread.currentThread();
            try {
                T result = action.get();
                future.complete(result);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });

        return future;
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        AtomicBoolean flag = new AtomicBoolean();
        CompletableFuture<Integer> future = supplyAsync(() -> {
            while (!flag.get()){

            }
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("I'm interrupted");
            }
            return 10;
        }, executor);

        future.cancel(true);
        Thread.sleep(1000);
        flag.set(true);
        executor.shutdown();


//        Completable future isn't lazy
//        System.out.println("point 1");
//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//            System.out.println("point 2");
//            return 12;
//        });
//        Thread.sleep(10000);
//        System.out.println("point 3");
//        future.thenAccept(num -> {
//            System.out.println("point 4");
//            System.out.println(num);
//        });
//        Thread.sleep(10000);
//        System.out.println("point 5");
    }
}
