package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_8;

import java.util.stream.Stream;

public class Task10 {

    private static double averageLength(Stream<String> strs) {
        return strs.mapToInt(String::length).average().orElse(0);
    }

}
