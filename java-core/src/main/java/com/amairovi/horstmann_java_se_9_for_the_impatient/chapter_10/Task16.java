package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_10;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class Task16 {

    @RequiredArgsConstructor
    public static class Producer extends SimpleFileVisitor<Path> {
        final List<Path> files = new ArrayList<>();

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
            if (attr.isRegularFile()) {
                files.add(file);
            }

            return CONTINUE;
        }

    }

    public static void main(String[] args) throws IOException {
        Producer producer = new Producer();
        Files.walkFileTree(Paths.get("<path to folder>"), producer);

        Map<String, Long> result = producer.files.stream()
                .parallel()
                .flatMap(f -> {
                    try {
                        return Files.lines(f);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return Stream.empty();
                    }
                })
                .flatMap(str -> Arrays.stream(str.split("\\s+")))
                .filter(str -> str.matches("[a-zA-Z0-9]+"))
                .collect(groupingBy(identity(), counting()));

        System.out.println(result);
    }
}
