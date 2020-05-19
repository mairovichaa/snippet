package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_7;

import java.util.Map;

public class Task9 {

    public static void inc_1(Map<String, Integer> counters, String key) {
        if (counters.containsKey(key)) {
            Integer counter = counters.get(key);
            counters.put(key, counter + 1);
        } else {
            counters.put(key, 1);
        }
    }

    public static void inc_2(Map<String, Integer> counters, String key) {
        Integer counter = counters.get(key);

        if (counter != null) {
            counters.put(key, counter + 1);
        } else {
            counters.put(key, 1);

        }
    }

    public static void inc_3(Map<String, Integer> counters, String key) {
        Integer counter = counters.getOrDefault(key, 0);

        counters.put(key, counter + 1);
    }

    public static void inc_4(Map<String, Integer> counters, String key) {
        counters.putIfAbsent(key, 0);
        Integer counter = counters.get(key);

        counters.put(key, counter + 1);
    }

}
