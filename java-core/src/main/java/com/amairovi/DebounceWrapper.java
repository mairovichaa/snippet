package com.amairovi;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.time.Duration.between;
import static java.util.concurrent.TimeUnit.SECONDS;

public class DebounceWrapper {

    private final static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public static Runnable debounce(Runnable r, Duration time) {

        return new Runnable() {

            private Instant lastCallAt;
            private Future<?> lastCall;

            @Override
            public void run() {
                Instant previousCallAt = lastCallAt;
                lastCallAt = Instant.now();

                if (previousCallAt != null
                        && between(previousCallAt, lastCallAt).compareTo(time) < 0) {
                    lastCall.cancel(false);
                }

                lastCall = executor.schedule(r, time.toMillis(), TimeUnit.MILLISECONDS);
            }
        };
    }

    public static void shutdown() {
        executor.shutdown();
    }

    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = () -> System.out.println(Instant.now() + " - called");
        Runnable throttled = debounce(runnable, Duration.ofSeconds(1));

        for (int i = 0; i < 10; i++) {
            throttled.run();
        }
        // only the last one has to be evaluated
        // others have to be cancelled
        SECONDS.sleep(2);

        for (int i = 0; i < 10; i++) {
            throttled.run();
        }

        shutdown();
    }

}
