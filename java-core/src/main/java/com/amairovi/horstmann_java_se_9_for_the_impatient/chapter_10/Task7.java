package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_10;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Task7 {

    static class CustomReader {
        ConcurrentHashMap<String, Set<File>> result = new ConcurrentHashMap<>();

        public void read(File file) throws IOException {
            Path path = Paths.get(file.toURI());
            Files.readAllLines(path)
                    .stream()
                    .flatMap(str -> Arrays.stream(str.split("\\s+")))
                    .filter(str -> !str.isEmpty())
                    .forEach(str -> {

                        // no need to create HashSet when it's not needed
                        result.computeIfAbsent(str, key -> new HashSet<>());
                        result.computeIfPresent(str, (key, set) -> {
                            set.add(file);
                            return set;
                        });

                        // or

//                        there is a need to use concurrent thread-safe implementation of set
//                        as in this case update isn't atomic
//                        result.computeIfAbsent(str, key -> ConcurrentHashMap.newKeySet());
//                        result.get(str).add(file);
                    });
        }
    }

    public static void main(String[] args) throws IOException {
        Collection<File> files = Files.list(Paths.get("<path to folder>"))
                .map(Path::toFile)
                .collect(Collectors.toList());

        CustomReader reader = new CustomReader();
        files.stream()
                .parallel()
                .forEach(f -> {
                    try {
                        reader.read(f);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        System.out.println(reader.result);
    }
}
