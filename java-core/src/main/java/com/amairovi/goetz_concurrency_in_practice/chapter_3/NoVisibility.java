package com.amairovi.goetz_concurrency_in_practice.chapter_3;

public class NoVisibility {

    private static boolean ready;
    private static int number;

    public static class ReaderThread extends Thread {

        @Override
        public void run() {
            while (!ready) {
                Thread.yield();
            }
            System.out.println(number);
        }

    }

    public static void main(String[] args) {
        new ReaderThread().start();
        number = 42;
        ready = true;
        System.out.println("here");
    }

}
