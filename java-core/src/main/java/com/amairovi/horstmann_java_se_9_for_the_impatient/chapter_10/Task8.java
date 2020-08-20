package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_10;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class Task8 {

    public static void main(String[] args) {
        Random random = new Random();

        ConcurrentHashMap<String, Long> map = new ConcurrentHashMap<>();

        AtomicLong atomicLong = new AtomicLong(Long.MIN_VALUE);
        random.longs()
                .limit(1_000_000)
                .forEach(num -> {
                    map.put("key_" + num, num);
                    long newMax = atomicLong.get() < num ? num : atomicLong.get();
                    atomicLong.set(newMax);
                });

        Map.Entry<String, Long> result = map.reduceEntries(16, (e1, e2) -> e1.getValue() > e2.getValue() ? e1 : e2);

        System.out.println("The biggest is " + result.getKey() + " (" + result.getValue() + ")");
        System.out.println("Real " + atomicLong.get());
    }
}
