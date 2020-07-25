package com.amairovi.goetz_concurrency_in_practice.chapter_7;

import java.util.concurrent.*;

import static com.amairovi.goetz_concurrency_in_practice.chapter_5.LaunderThrowable.launderThrowable;

public class TimedRun {

    private static final ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

    public static void timedRun(Runnable r, long timeout, TimeUnit unit) {
        final Thread taskThread = Thread.currentThread();
        // it will be evaluated in certain amount of time anyway,
        // which means that taskThread will be interrupted even in case of successful end of r
        service.schedule(taskThread::interrupt, timeout, unit);

        // task can have no interruption policy,
        // so taskThread::interrupt won't have any effect on its execution
        r.run();
    }

    public static void timedRun2(Runnable r, long timeout, TimeUnit unit) throws InterruptedException {
        class RethrowableTask implements Runnable {

            private volatile Throwable t;

            @Override
            public void run() {
                try {
                    // task can have no interruption policy,
                    // so taskThread::interrupt won't have any effect on its execution
                    r.run();
                } catch (Throwable t) {
                    this.t = t;
                }
            }

            void rethrow() {
                if (t != null) {
                    throw launderThrowable(t);
                }
            }

        }

        RethrowableTask task = new RethrowableTask();
        final Thread taskThread = new Thread(task);
        taskThread.start();

        service.schedule(taskThread::interrupt, timeout, unit);

        // we don't know if control was returned because the thread
        // exited normally or because the join timed out
        taskThread.join(unit.toMillis(timeout));
        task.rethrow();
    }

    public static void timedRun3(Runnable r, long timeout, TimeUnit unit) throws InterruptedException {
        Future<?> task = service.submit(r);
        try {
            task.get(timeout, unit);
        } catch (TimeoutException e){
            // task will be cancelled below
        } catch (ExecutionException e){
            // exception thrown in task; rethrow
            throw launderThrowable(e.getCause());
        } finally {
            // Harmless if task already completed
            task.cancel(true); // interrupt if running
        }


    }
}
