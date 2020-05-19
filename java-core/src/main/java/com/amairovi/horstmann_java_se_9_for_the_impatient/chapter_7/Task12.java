package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_7;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Task12 {

    static void shuffle(List<String> words) {
        Collections.shuffle(words);

        String sentence = String.join(" ", words);
        sentence = Character.toUpperCase(sentence.charAt(0)) + sentence.substring(1) + ".";

        System.out.println(sentence);
    }


    public static void main(String[] args) {
        List<String> strings = Arrays.asList("a", "b", "c", "d", "e", "f");
        shuffle(strings);
        System.out.println(strings);
    }

}
