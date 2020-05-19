package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_7;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Task7 {

    static Map<String, Integer> countWords(Path file) throws IOException {
        List<String> lines = Files.readAllLines(file);

        TreeMap<String, Integer> result = new TreeMap<>();
        for (String line : lines) {
            String[] words = line.split("\\s+");

            for (String word : words) {
                word = word.replaceAll("[^\\w]", "");
                Integer counter = result.getOrDefault(word, 0);
                result.put(word, counter + 1);
            }
        }

        return result;
    }

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("/Users/amairovi/Documents/projects/snippet/java-core/src/main/java/com/amairovi/horstmann_java_se_9_for_the_impatient/chapter_7/Task7.java");
        System.out.println(countWords(path));

    }

}
