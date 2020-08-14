package com.amairovi.se.concurrency;

public class Test1 {

    private static class TestRunnable implements Runnable {

        private final String id;
        private final Object monitor;


        private TestRunnable(String id, Object monitor) {
            this.id = id;
            this.monitor = monitor;
        }

        @Override
        public void run() {

            synchronized (monitor) {
                try {
                    monitor.wait();
                    System.out.println("id: " + id);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void main(String[] args) throws InterruptedException {
        Object monitor = new Object();

        for (int i = 0; i < 10; i++) {
            new Thread(new TestRunnable("" + i, monitor)).start();
        }

        Thread.sleep(1000);
        synchronized (monitor) {
            System.out.println("notifyAll");
            Thread.yield();

            monitor.notifyAll();
        }
    }

}
