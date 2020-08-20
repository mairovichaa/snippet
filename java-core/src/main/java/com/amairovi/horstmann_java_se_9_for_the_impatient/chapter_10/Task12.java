package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_10;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class Task12 {


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
        final BlockingQueue<Map<String, Long>> occurrencesQueue = new LinkedBlockingDeque<>();

        public void add(Path path) throws InterruptedException {
            queue.put(path);
        }

        Path poll() throws InterruptedException {
            return queue.take();
        }

        void finish() throws InterruptedException {
            queue.put(FINISH);
        }

        public void addToOccurrences(Map<String, Long> map) throws InterruptedException {
            occurrencesQueue.put(map);
        }

        public Map<String, Long> pollFromOccurrences() throws InterruptedException {
            return occurrencesQueue.take();
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

                    Map<String, Long> result = Files.readAllLines(path)
                            .stream()
                            .flatMap(str -> Arrays.stream(str.split("\\s+")))
                            .filter(str -> !str.isEmpty())
                            .collect(groupingBy(identity(), counting()));

                    searcher.addToOccurrences(result);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @RequiredArgsConstructor
    public static class OccurrenceConsumer implements Runnable {
        private final Searcher searcher;

        @Override
        public void run() {
            while (true) {
                try {
                    Map<String, Long> occurrences = searcher.pollFromOccurrences();

                    if (path == Searcher.FINISH) {
                        return;
                    }

                    Map<String, Long> result = Files.readAllLines(path)
                            .stream()
                            .flatMap(str -> Arrays.stream(str.split("\\s+")))
                            .filter(str -> !str.isEmpty())
                            .collect(groupingBy(identity(), counting()));

                    searcher.addToOccurrences(result);
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

        System.out.println(searcher.occurrencesQueue);
    }
}
