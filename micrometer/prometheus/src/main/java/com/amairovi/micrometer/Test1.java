package com.amairovi.micrometer;

import com.sun.net.httpserver.HttpServer;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Test1 {

    public static void main(String[] args) {
        PrometheusMeterRegistry prometheusRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

        prometheusRegistry.config().commonTags("common.tag", "common.tag.sample");
        prometheusRegistry.config().commonTags("common.tag2", "common.tag.sample");
        prometheusRegistry.config().commonTags("common.tag3", "common.tag.sample");
        prometheusRegistry.config().meterFilter(MeterFilter.denyNameStartsWith("ignor"));
        Counter test = prometheusRegistry.counter("test");
        Counter ignore = prometheusRegistry.counter("ignore");
        List<String> list = prometheusRegistry.gauge("listGauge", Collections.emptyList(), new ArrayList<>(), List::size);

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/prometheus", httpExchange -> {

                test.increment();
                ignore.increment();
                String response = prometheusRegistry.scrape();
                list.add("str");
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
