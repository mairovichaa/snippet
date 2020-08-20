package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_10;

import java.util.Random;
import java.util.concurrent.atomic.LongAccumulator;

public class Task10 {
    public static void main(String[] args) {
        LongAccumulator minAccumulator = new LongAccumulator(Math::min, Long.MAX_VALUE);

        long[] longs = new Random()
                .longs()
                .limit(1000)
                .toArray();


        LongAccumulator maxAccumulator = new LongAccumulator(Math::max, Long.MIN_VALUE);

        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;

        for (int i = 0; i < longs.length; i++) {
            long number = longs[i];

            minAccumulator.accumulate(number);
            maxAccumulator.accumulate(number);

            min = Math.min(min, number);
            max = Math.max(max, number);
        }

        System.out.println(minAccumulator.get());
        System.out.println(min);

        System.out.println(maxAccumulator.get());
        System.out.println(max);

    }
}
