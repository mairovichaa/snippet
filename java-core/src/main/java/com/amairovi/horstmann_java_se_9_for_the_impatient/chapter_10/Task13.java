package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_10;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.concurrent.*;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class Task13 {

    @RequiredArgsConstructor
    public static class Producer extends SimpleFileVisitor<Path> {
        private final List<Callable<Map<String, Long>>> tasks;

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
            if (attr.isRegularFile()) {
                tasks.add(() -> Files.readAllLines(file)
                        .stream()
                        .flatMap(str -> Arrays.stream(str.split("\\s+")))
                        .filter(str -> str.matches("[a-zA-Z0-9]+"))
                        .collect(groupingBy(identity(), counting())));
            }

            return CONTINUE;
        }

    }

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        List<Callable<Map<String, Long>>> tasks = new ArrayList<>();

        int availableProcessors = Runtime.getRuntime().availableProcessors();

        Producer producer = new Producer(tasks);
        Files.walkFileTree(Paths.get("<path to folder>"), producer);

        ExecutorService executorService = Executors.newFixedThreadPool(availableProcessors + 1);
        List<Future<Map<String, Long>>> futures = executorService
                .invokeAll(tasks);

        Map<String, Long> result = new HashMap<>();
        for (Future<Map<String, Long>> f : futures) {
            Map<String, Long> occurrences = f.get();
            occurrences.forEach((k, v) -> result.merge(k, v, Long::sum));
        }

        System.out.println(result);
        executorService.shutdown();
    }
}
