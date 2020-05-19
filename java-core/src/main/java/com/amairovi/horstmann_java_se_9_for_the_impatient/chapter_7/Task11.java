package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_7;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Task11 {

    static void shuffle(List<String> words) {
        List<String> wordsWithoutFirstAndLast = words.subList(1, words.size() - 1);
        Collections.shuffle(wordsWithoutFirstAndLast);
    }


    public static void main(String[] args) {
        List<String> strings = Arrays.asList("1", "2", "3", "4", "5", "6");
        shuffle(strings);
        System.out.println(strings);
    }

}
