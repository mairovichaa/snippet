package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_7;

import java.util.*;

public class Task8 {

    static Map<String, Set<Integer>> findNumberOfLinesWhereWordsPresent(List<String> lines) {
        TreeMap<String, Set<Integer>> result = new TreeMap<>();

        for (int lineNumber = 0; lineNumber < lines.size(); lineNumber++) {
            String line = lines.get(lineNumber);
            String[] words = line.split("\\s+");

            for (String word : words) {
                word = word.replaceAll("[^\\w]", "");
                Set<Integer> lineNumbers = result.getOrDefault(word, new HashSet<>());
                lineNumbers.add(lineNumber);
                result.put(word, lineNumbers);
            }
        }

        return result;
    }

}
