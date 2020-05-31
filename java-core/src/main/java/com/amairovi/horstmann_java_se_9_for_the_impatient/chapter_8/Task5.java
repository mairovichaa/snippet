package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_8;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Task5 {

    static Stream<String> codePoints(String s) {
        List<String> result = new ArrayList<>();

        int i = 0;
        while (i < s.length()) {
            int j = s.offsetByCodePoints(i, 1);
            result.add(s.substring(i, j));
            i = j;
        }
        return result.stream();
    }


    static Stream<String> codePoints2(String s) {
        int length = s.length();

        return IntStream.iterate(0, i ->
                i < length ? s.offsetByCodePoints(i, 1) : i)
                .limit(length)
                .filter(pos -> pos < length)
                .mapToObj(i -> s.substring(i, s.offsetByCodePoints(i, 1)));
    }

    public static void main(String[] args) {

        Stream<String> abcde = codePoints("abcde");
        abcde.forEach(System.out::println);
        System.out.println();

        abcde = codePoints2("abcde");
        abcde.forEach(System.out::println);
        System.out.println();
    }


}
