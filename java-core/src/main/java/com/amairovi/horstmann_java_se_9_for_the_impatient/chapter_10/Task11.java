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
    public static class Producer
            extends SimpleFileVisitor<Path> {

        private final Searcher searcher;

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
            if (attr.isRegularFile()) {
                try {
                    searcher.add(file);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return CONTINUE;
        }

        public void finish() throws InterruptedException {
            searcher.finish();
        }
    }

    @RequiredArgsConstructor
    private static class Searcher {
        static Path FINISH = Paths.get("");

        final Set<String> keyWords;
        final BlockingQueue<Path> queue = new LinkedBlockingDeque<>();

        public void add(Path path) throws InterruptedException {
            queue.put(path);
        }

        public void finish() throws InterruptedException {
            queue.put(FINISH);
        }

        public Path poll() throws InterruptedException {
            return queue.take();
        }


    }

    @RequiredArgsConstructor
    public static class Consumer implements Runnable {
        private final Searcher searcher;

        @Override
        public void run() {
            while (true) {
                try {
                    Path path = searcher.poll();

                    if (path == Searcher.FINISH) {
                        return;
                    }
                    boolean match = Files.readAllLines(path)
                            .stream()
                            .anyMatch(str -> searcher.keyWords.stream().anyMatch(str::contains));
                    if (match) {
                        System.out.println(path);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        HashSet<String> set = new HashSet<>();
        set.add("DeadlockBetweenCooperating");


        Searcher searcher = new Searcher(set);

        for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
            new Thread(new Consumer(searcher)).start();
        }

        Producer producer = new Producer(searcher);
        Files.walkFileTree(Paths.get("E:\\projects\\snippets\\java-core\\src\\main\\java\\com\\amairovi"), producer);

        for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
            producer.finish();
        }

    }
}
