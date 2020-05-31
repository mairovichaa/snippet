package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_8;

import java.util.stream.LongStream;

public class Task4 {

    private static LongStream randoms(long seed, long a, long c, long m) {
        return LongStream.iterate(seed, val -> (a + val + c) % m);
    }

    public static void main(String[] args) {
        LongStream randoms = randoms(2, 25214903917L, 11, (long) Math.pow(2, 48));

        randoms.limit(10)
                .forEach(System.out::println);
    }

}
