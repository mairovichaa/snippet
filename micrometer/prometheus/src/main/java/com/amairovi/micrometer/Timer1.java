package com.amairovi.micrometer;

import com.sun.net.httpserver.HttpServer;
import io.micrometer.core.instrument.Timer;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class Timer1 {


    public static void main(String[] args) {
        PrometheusMeterRegistry prometheusRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

        Timer timer = Timer
                .builder("my.timer")
                .description("a description of what this timer does") // optional
                .tags("region", "test") // optional
                .register(prometheusRegistry);

        Timer timer2 = Timer
                .builder("my.timer-2")
                .description("a description of what this timer does") // optional
                .tags("tag1", "tag2") // optional
                .register(prometheusRegistry);

        Timer timer3 = Timer
                .builder("my.timer-3")
                .register(prometheusRegistry);

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/prometheus", httpExchange -> {

                timer.record(10, TimeUnit.MICROSECONDS);
                timer2.record(10, TimeUnit.SECONDS);
                timer3.record(() -> System.out.println("running"));
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
