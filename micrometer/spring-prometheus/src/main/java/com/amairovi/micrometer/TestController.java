package com.amairovi.micrometer;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final MeterRegistry meterRegistry;
    private final TimedService timedService;
    private final PrometheusMeterRegistry prometheusMeterRegistry;

    public TestController(MeterRegistry meterRegistry, TimedService timedService) {
        this.meterRegistry = meterRegistry;
        this.timedService = timedService;
        this.prometheusMeterRegistry = MetricsConfiguration.customPrometheusMeterRegistry;
    }


    @GetMapping("/test")
    public String test() {
        Counter test = prometheusMeterRegistry.counter("test");
        test.increment();

        timedService.m1();
        timedService.m2();
        timedService.m3();
        timedService.m4();

        return prometheusMeterRegistry.scrape().replaceAll("\\n", "<br>");
    }

    @GetMapping("/metrics")
    public String metrics() {
        return meterRegistry instanceof PrometheusMeterRegistry ? ((PrometheusMeterRegistry) meterRegistry).scrape() : "";
    }

}
