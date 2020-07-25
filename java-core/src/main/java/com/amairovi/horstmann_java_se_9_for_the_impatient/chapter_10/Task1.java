package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_10;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Task1 {

    private static List<String> find(Path dir, String word) {
        try {
            return Files.list(dir)
                    // parallel doesn't work - always the same order
                    // but in fact fork join is used for the execution
                    // check the snippet below
                    .parallel()
                    .peek(Task1::printFilename)
                    .filter(p -> Files.isRegularFile(p))
                    .filter(p -> containsWord(p, word))
                    .map(Path::toFile)
                    .map(File::getName)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//
//        try {
//            List<Path> list = Files.list(dir).collect(toList());
//            return list.parallelStream()
//                    .peek(Task1::printFilename)
//                    .filter(p -> Files.isRegularFile(p))
//                    .filter(p -> containsWord(p, word))
//                    .map(Path::toFile)
//                    .map(File::getName)
//                    .collect(toList());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

    }

    private static Optional<String> findOne(Path dir, String word) {
        try {
            return Files.list(dir)
                    // parallel doesn't work - always the same order
                    // but in fact fork join is used for the execution
                    // check the snippet below
                    .parallel()
                    .peek(Task1::printFilename)
                    .filter(p -> Files.isRegularFile(p))
                    .filter(p -> containsWord(p, word))
                    .map(Path::toFile)
                    .map(File::getName)
                    .findFirst();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    // method to verify if ordering changes due to concurrency
    private static void printFilename(Path path) {
        System.out.println(path.toFile().getName());
    }

    private static boolean containsWord(Path p, String word) {
        try {
            return Files.lines(p)
                    .anyMatch(line -> line.contains(word));
        } catch (IOException e) {
            return false;
        }
    }

    private final static String DIR = "";

    public static void main(String[] args) {
        Path path = Paths.get(DIR);
        List<String> list = find(path, "Pattern");
        System.out.println(list);
    }

}
