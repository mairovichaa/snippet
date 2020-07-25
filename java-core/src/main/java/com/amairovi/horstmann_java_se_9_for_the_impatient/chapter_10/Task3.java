package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_10;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Task3 {

    private static Optional<String> find(Path dir, String word) {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        try {
            List<SearchWordTask> tasks = Files.list(dir)
                    .map(p -> new SearchWordTask(p, word))
                    .collect(Collectors.toList());

            Path result = executorService.invokeAny(tasks);

            return Optional.of(result.toFile().getName());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            return Optional.empty();
        } finally {
            executorService.shutdown();
        }
    }

    @RequiredArgsConstructor
    private static class SearchWordTask implements Callable<Path> {

        private final Path p;
        private final String word;

        @Override
        public Path call() throws Exception {
            List<String> lines = Files.readAllLines(p);

            for (String line : lines) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Search for " + p.toFile().getName() + " was interrupted");
                    return null;
                }

                if (line.contains(word)) {
                    return p;
                }
            }

            throw new IllegalArgumentException("word '" + word + "' was not found");
        }

    }

    private final static String DIR = "";

    public static void main(String[] args) {
        Path path = Paths.get(DIR);
        String word = "Pattern";
        Optional<String> filename = find(path, word);

        if (filename.isPresent()) {
            System.out.println(filename.get());
        } else {
            System.out.println("No file containing word '" + word + "' was found in " + DIR);
        }
    }

}
