package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_10;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class Task14 {

    @RequiredArgsConstructor
    public static class Producer extends SimpleFileVisitor<Path> {
        private final CompletionService<Map<String, Long>> service;
        int amountOfFiles = 0;

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
            System.out.println("visitFile " + file);
            if (attr.isRegularFile()) {
                amountOfFiles++;
                service.submit(
                        () -> Files.readAllLines(file)
                                .stream()
                                .flatMap(str -> Arrays.stream(str.split("\\s+")))
                                .filter(str -> str.matches("[a-zA-Z0-9]+"))
                                .collect(groupingBy(identity(), counting()))
                );
            }
            return CONTINUE;
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(availableProcessors + 1);
        ExecutorCompletionService<Map<String, Long>> service = new ExecutorCompletionService<>(executorService);

        Path path = Paths.get("<path to folder>");
        Producer producer = new Producer(service);
        Files.walkFileTree(path, producer);

        Map<String, Long> result = new HashMap<>();

        for (int i = 0; i < producer.amountOfFiles; i++) {
            Future<Map<String, Long>> future = service.take();
            Map<String, Long> map = future.get();
            map.forEach((k, v) -> result.merge(k, v, Long::sum));
        }

        System.out.println(result);
        executorService.shutdown();
    }
}
