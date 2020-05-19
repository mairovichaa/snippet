package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_7;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Task6 {

    static void method(Map<String, Set<Integer>> param) {
    }


    static <T extends Set<Integer>> void method2(Map<String, T> param) {
        Set<Integer> integers = param.get("");
        integers.add(2);
    }

    public static void main(String[] args) {
        HashMap<String, HashSet<Integer>> map = new HashMap<>();
//        Impossible as HashMap<String, HashSet<Integer>> isn't Map<String, Set<Integer>>
//        method(map);
        method2(map);

    }

}
