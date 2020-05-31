package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_8;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Task3 {

    public static void main(String[] args) {
        int[] values = {1, 4, 9, 16};

        Stream<int[]> stream = Stream.of(values);

        IntStream intStream = IntStream.of(values);
    }

}
