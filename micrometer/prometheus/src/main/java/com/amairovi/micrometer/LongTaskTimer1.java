package com.amairovi.micrometer;

import com.sun.net.httpserver.HttpServer;
import io.micrometer.core.instrument.LongTaskTimer;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LongTaskTimer1 {

    public static void main(String[] args) {
        PrometheusMeterRegistry prometheusRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

        LongTaskTimer longTaskTimer = LongTaskTimer
                .builder("long.task.timer")
                .description("a description of what this timer does")
                .tags("region", "test")
                .register(prometheusRegistry);

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.submit(() -> {
            longTaskTimer.record(() -> {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        });

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/prometheus", httpExchange -> {

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
