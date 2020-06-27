package com.amairovi.micrometer;

import com.sun.net.httpserver.HttpServer;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Random;

public class DistributionSummary1 {

    public static void main(String[] args) {
        PrometheusMeterRegistry prometheusRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

        DistributionSummary summary = DistributionSummary
                .builder("response.size")
                .description("a description of what this summary does")
                .baseUnit("bytes")
                .tags("region", "test")
                .publishPercentiles(0.5, 0.7, 0.9)
                .sla(1000, 10000, 100000)
                .scale(100) // measurement will be multiplied by 1000
                .register(prometheusRegistry);

        Random random = new Random();
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/prometheus", httpExchange -> {

                int amount = random.nextInt(1000);
                System.out.println(amount);
                summary.record(amount);
                String response = prometheusRegistry.scrape();
                httpExchange.sendResponseHeaders(200, response.getBytes().length);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            });

            new Thread(server::start).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
