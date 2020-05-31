package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_8;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

public class Task7 {

    private static boolean isAlphabetic(String str) {
        return str.codePoints()
                .allMatch(Character::isAlphabetic);
    }

    private static List<String> first100Words(Stream<String> file) {
        return file.filter(Task7::isAlphabetic)
                .limit(100)
                .collect(Collectors.toList());
    }


    private static List<String> mostFrequent10(Stream<String> file) {
        return file.filter(Task7::isAlphabetic)
                .map(String::toLowerCase)
                .collect(groupingBy(Function.identity(), counting()))
                .entrySet()
                .stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                //another way
//                .sorted(Comparator.<Map.Entry<String, Long>>comparingLong(Map.Entry::getValue).reversed())
                // problem
//                .sorted(Comparator.comparingLong(Map.Entry::getValue).reversed())
                .limit(10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        List<String> strings = Arrays.asList("aa", "aa", "aa", "aa", "aa", "bbb", "bbb");

        System.out.println(mostFrequent10(strings.stream()));
    }

}
