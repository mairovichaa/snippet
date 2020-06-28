package com.amairovi.micrometer;

import io.micrometer.core.annotation.Timed;
import org.springframework.stereotype.Service;

import static com.amairovi.micrometer.MetricsConfiguration.DISTRIBUTION_SUMMARY_PRECONFIGURED_NAME;
import static com.amairovi.micrometer.MetricsConfiguration.TIMER_PRECONFIGURED_NAME;

@Service
public class TimedService {

    @Timed("time.timedService.m1")
    public void m1() {
        System.out.println("here m1");
    }

    @Timed(value = "time.timedService.m2", percentiles = {0.5, 0.7, 0.9, 0.95})
    public void m2() {
        System.out.println("here m2");
    }


    @Timed(TIMER_PRECONFIGURED_NAME)
    // it doesn't store measurement for created in MetricsConfiguration class timer
    // actually, it doesn't store anything
    public void m3() {
        System.out.println("here m3");
    }


    @Timed(DISTRIBUTION_SUMMARY_PRECONFIGURED_NAME)
    // it doesn't work properly - distribution summary is ignored.
    // Timer is used instead
    public void m4() {
        System.out.println("here m4");
    }

}
