package com.amairovi;

import java.time.Duration;
import java.time.Instant;

import static java.util.concurrent.TimeUnit.SECONDS;

public class ThrottleWrapper {

    public static Runnable throttle(Runnable r, Duration time) {
        return new Runnable() {

            private Instant lastCallAt;

            @Override
            public void run() {
                Instant previousCallAt = lastCallAt;
                lastCallAt = Instant.now();

                if (previousCallAt == null
                        || Duration.between(previousCallAt, lastCallAt).compareTo(time) >= 0
                ) {
                    r.run();
                }
            }
        };

    }

    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = () -> System.out.println(Instant.now() + " - called");
        Runnable throttled = throttle(runnable, Duration.ofSeconds(1));

        for (int i = 0; i < 10; i++) {
            throttled.run();
        }

        // the first one will be executed
        // others will be skipped due to throttling
        SECONDS.sleep(2);

        for (int i = 0; i < 10; i++) {
            throttled.run();
        }
    }

}
