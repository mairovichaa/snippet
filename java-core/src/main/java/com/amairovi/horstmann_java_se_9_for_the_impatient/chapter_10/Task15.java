package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_10;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class Task15 {

    @RequiredArgsConstructor
    public static class Producer extends SimpleFileVisitor<Path> {
        private final Executor service;
        final Map<String, Long> result = new ConcurrentHashMap<>();
        final AtomicInteger amountOfProcessed = new AtomicInteger();
        int amountOfFiles = 0;

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
            if (attr.isRegularFile()) {
                amountOfFiles++;
                service.execute(
                        () -> {
                            try {
                                Files.readAllLines(file)
                                        .stream()
                                        .flatMap(str -> Arrays.stream(str.split("\\s+")))
                                        .filter(str -> str.matches("[a-zA-Z0-9]+"))
                                        .collect(groupingBy(identity(), counting()))
                                        .forEach((k, v) -> result.merge(k, v, Long::sum));
                                amountOfProcessed.incrementAndGet();
                                if (amountOfProcessed.get() == amountOfFiles) {
                                    synchronized (this) {
                                        notify();
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                );
            }
            return CONTINUE;
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(availableProcessors + 1);

        Path path = Paths.get("<path to folder>");
        Producer producer = new Producer(executorService);
        Files.walkFileTree(path, producer);

        synchronized (producer) {
            while (producer.amountOfProcessed.get() != producer.amountOfFiles) {
                producer.wait();
            }
        }

        System.out.println(producer.result);
        executorService.shutdown();
    }
}
