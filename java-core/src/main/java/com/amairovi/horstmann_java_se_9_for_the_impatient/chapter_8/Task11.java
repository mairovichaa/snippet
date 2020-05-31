package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_8;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Task11 {

    private static List<String> strOfMaxLength(Stream<String> strs) {
        List<String> result = strs.collect(Collectors.toList());

        int maxLength = result.stream()
                .mapToInt(String::length)
                .max().orElse(0);

        result.removeIf(str -> str.length() == maxLength);

        return result;
    }

}
