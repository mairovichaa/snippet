package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_8;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Task9 {

    private static List<String> with5DistinctVowels(Stream<String> strs) {
        return strs.filter(Task9::contains5DistinctVowels)
                .collect(Collectors.toList());
    }

    private static boolean contains5DistinctVowels(String str) {

        return str.toLowerCase()
                .chars()
                .distinct()
                .filter(c -> c == 'e' || c == 'u' || c == 'i' || c == 'o' || c == 'a' || c =='y')
                .count() == 5;
    }
}
