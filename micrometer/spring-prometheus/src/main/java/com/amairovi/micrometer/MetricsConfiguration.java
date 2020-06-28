package com.amairovi.micrometer;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.Timer;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfiguration {

    public static PrometheusMeterRegistry customPrometheusMeterRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

    @Bean
    public TimedAspect timedAspect() {
        return new TimedAspect(customPrometheusMeterRegistry);
    }

    public final static String TIMER_PRECONFIGURED_NAME = "my.timer";
    public final static String DISTRIBUTION_SUMMARY_PRECONFIGURED_NAME = "my.distribution_summary";

    static {
        Timer.builder(TIMER_PRECONFIGURED_NAME)
                .publishPercentiles(0.5, 0.7, 0.9, 0.95)
                .description("a description of what this timer does")
                .tags("region", "test")
                .register(customPrometheusMeterRegistry);

        DistributionSummary
                .builder(DISTRIBUTION_SUMMARY_PRECONFIGURED_NAME)
                .tags("region", "test")
                .publishPercentiles(0.5, 0.7, 0.9)
                .scale(100) // measurement will be multiplied by 100
                .register(customPrometheusMeterRegistry);
    }

}
