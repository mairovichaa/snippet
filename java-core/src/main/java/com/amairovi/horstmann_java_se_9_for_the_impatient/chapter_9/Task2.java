package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_9;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class Task2 {

    private static void m(Path path) {
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            Map<String, Set<Integer>> numberOfLinesWhereWordsPresent = findNumberOfLinesWhereWordsPresent(lines);

            List<String> result = numberOfLinesWhereWordsPresent.entrySet()
                    .stream()
                    .sorted(Comparator.comparing(Map.Entry::getKey))
                    .map(e -> e.getKey() + " : " + e.getValue().stream().sorted().map(Object::toString).collect(joining(", ")))
                    .collect(toList());

            String nameWithTxtExt = path.toFile().getName();

            String name = nameWithTxtExt.substring(0, nameWithTxtExt.lastIndexOf("."));

            Path output = path.getParent().resolve(name + ".toc");
            Files.write(output, result, StandardCharsets.UTF_8);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Set<Integer>> findNumberOfLinesWhereWordsPresent(List<String> lines) {
        TreeMap<String, Set<Integer>> result = new TreeMap<>();

        for (int lineNumber = 0; lineNumber < lines.size(); lineNumber++) {
            String line = lines.get(lineNumber);
            String[] words = line.split("\\s+");

            for (String word : words) {
                Set<Integer> lineNumbers = result.getOrDefault(word, new HashSet<>());
                lineNumbers.add(lineNumber + 1);
                result.put(word, lineNumbers);
            }
        }

        return result;
    }

    public static void main(String[] args) {
        Path path = Paths.get("");
        m(path);
    }

}
