package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_10;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class Task12 {

    @RequiredArgsConstructor
    public static class Producer extends SimpleFileVisitor<Path> {
        private final BlockingQueue<Path> queue;

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
            if (attr.isRegularFile()) {
                try {
                    queue.put(file);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return CONTINUE;
        }

    }

    @RequiredArgsConstructor
    public static class Consumer implements Runnable {
        private final Path poisonPill;
        private final BlockingQueue<Path> paths;
        private final BlockingQueue<Map<String, Long>> occurrences;

        @Override
        public void run() {
            while (true) {
                try {
                    Path path = paths.take();

                    if (path == poisonPill) {
                        return;
                    }

                    Map<String, Long> result = Files.readAllLines(path)
                            .stream()
                            .flatMap(str -> Arrays.stream(str.split("\\s+")))
                            .filter(str -> str.matches("[a-zA-Z0-9]+"))
                            .collect(groupingBy(identity(), counting()));

                    occurrences.put(result);
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @RequiredArgsConstructor
    public static class OccurrenceConsumer implements Runnable {
        private final Map<String, Long> poisonPill;
        private final BlockingQueue<Map<String, Long>> queue;
        private final Map<String, Long> result = new HashMap<>();

        @Override
        public void run() {
            while (true) {
                try {
                    Map<String, Long> occurrences = queue.take();

                    if (occurrences == poisonPill) {
                        return;
                    }

                    occurrences.forEach((key, value) -> result.merge(key, value, Long::sum));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        BlockingQueue<Path> pathsQueue = new LinkedBlockingDeque<>();
        Path poisonPillForPathQueue = Paths.get("");

        BlockingQueue<Map<String, Long>> occurrencesQueue = new LinkedBlockingDeque<>();
        Map<String, Long> poisonPillForOccurrencesQueue = new HashMap<>();

        OccurrenceConsumer occurrenceConsumer = new OccurrenceConsumer(poisonPillForOccurrencesQueue, occurrencesQueue);
        Thread occurrencesCounterThread = new Thread(occurrenceConsumer);
        occurrencesCounterThread.start();

        int availableProcessors = Runtime.getRuntime().availableProcessors();

        int amountOfConsumers = availableProcessors + 1;
        Thread[] consumers = new Thread[amountOfConsumers];
        for (int i = 0; i < amountOfConsumers; i++) {
            Thread thread = new Thread(new Consumer(poisonPillForPathQueue, pathsQueue, occurrencesQueue));
            thread.start();
            consumers[i] = thread;
        }

        Producer producer = new Producer(pathsQueue);
        Files.walkFileTree(Paths.get("<path to folder>"), producer);

        for (int i = 0; i < amountOfConsumers; i++) {
            pathsQueue.put(poisonPillForPathQueue);
        }

        for (int i = 0; i < consumers.length; i++) {
            consumers[i].join();
        }

        occurrencesQueue.put(poisonPillForOccurrencesQueue);
        occurrencesCounterThread.join();
        System.out.println(occurrenceConsumer.result);
    }
}
