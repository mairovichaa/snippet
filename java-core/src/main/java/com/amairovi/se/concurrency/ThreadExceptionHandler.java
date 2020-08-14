package com.amairovi.se.concurrency;

public class ThreadExceptionHandler {

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            throw new RuntimeException("exception 1");
        });
        thread1.setUncaughtExceptionHandler((t, e) -> {
            System.out.println("Thread: " + t.getName());
            e.printStackTrace(System.out);
        });
        thread1.start();
        thread1.join();

        System.out.println();

        Thread thread2 = new Thread(() -> {
            throw new RuntimeException("exception 1");
        });
        thread2.setUncaughtExceptionHandler((t, e) -> {
            System.out.println("Thread: " + t.getName());
            System.out.println("There is an exception, but I will swallow it, " +
                    "so information about it will be lost.");
        });
        thread2.start();
        thread2.join();
    }


}
