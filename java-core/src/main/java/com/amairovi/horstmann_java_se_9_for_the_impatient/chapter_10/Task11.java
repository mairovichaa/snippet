package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_10;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import static java.nio.file.FileVisitResult.CONTINUE;

public class Task11 {

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
        private final Set<String> keyWords;
        private final Path FINISH;
        private final BlockingQueue<Path> queue;

        @Override
        public void run() {
            while (true) {
                try {
                    Path path = queue.take();

                    if (path == FINISH) {
                        return;
                    }
                    boolean match = Files.readAllLines(path)
                            .stream()
                            .anyMatch(str -> keyWords.stream().anyMatch(str::contains));
                    if (match) {
                        System.out.println(path);
                    }

                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        HashSet<String> keyWords = new HashSet<>();
        keyWords.add("DeadlockBetweenCooperating");

        BlockingQueue<Path> queue = new LinkedBlockingDeque<>();
        Path poisonPill = Paths.get("");

        int availableProcessors = Runtime.getRuntime().availableProcessors();

        int amountOfConsumers = availableProcessors + 1;
        for (int i = 0; i < amountOfConsumers; i++) {
            new Thread(new Consumer(keyWords, poisonPill, queue)).start();
        }

        Producer producer = new Producer(queue);
        Files.walkFileTree(Paths.get("<path to folder>"), producer);

        for (int i = 0; i < amountOfConsumers; i++) {
            queue.put(poisonPill);
        }

    }
}
