package com.amairovi.goetz_concurrency_in_practice.chapter_7;

public class ShutDownHookExample {

    private static class Clazz1 {

        public void start() {
            Runtime.getRuntime()
                    .addShutdownHook(new Thread(() -> System.out.println("shutting down Clazz1")));
        }

    }

    private static class Clazz2 {

        public void start() {
            Runtime.getRuntime()
                    .addShutdownHook(new Thread(() -> System.out.println("shutting down Clazz2")));
        }

    }

    public static void main(String[] args) {
        new Clazz1().start();
        new Clazz2().start();
//        will prevent shutdown hooks' execution
//        Runtime.getRuntime().halt(2);
        System.out.println("main is at the end of its execution");
    }
}
