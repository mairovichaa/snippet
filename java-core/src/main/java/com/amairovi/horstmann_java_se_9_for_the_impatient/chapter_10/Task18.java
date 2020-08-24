package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_10;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.nio.file.FileVisitResult.CONTINUE;

public class Task18 {

    public static long count = 0;

    @RequiredArgsConstructor
    public static class Producer extends SimpleFileVisitor<Path> {

        final List<Thread> threads = new ArrayList<>();
        private final Lock lock = new ReentrantLock();

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
            if (attr.isRegularFile()) {
                Thread thread = new Thread(() -> {
                    try {
                        long amountOfWords = Files.readAllLines(file)
                                .stream()
                                .flatMap(str -> Arrays.stream(str.split("\\s+")))
                                .filter(str -> str.matches("[a-zA-Z0-9]+"))
                                .count();
                        lock.lock();
                        try {
                            count += amountOfWords;
                        } finally {
                            lock.unlock();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                thread.start();
                threads.add(thread);
            }
            return CONTINUE;
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Producer producer = new Producer();
        Files.walkFileTree(Paths.get("<path to folder>"), producer);

        for (int i = 0; i < producer.threads.size(); i++) {
            producer.threads.get(i).join();
        }
        System.out.println(count);

    }
}
