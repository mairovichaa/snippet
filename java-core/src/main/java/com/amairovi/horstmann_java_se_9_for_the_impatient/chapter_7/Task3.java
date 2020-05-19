package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_7;

import java.util.HashSet;
import java.util.Set;

public class Task3 {

    public static void main(String[] args) {
        Set<Object> set1 = new HashSet<>();
        Set<Object> set2 = new HashSet<>();


        Set<Object> union = new HashSet<>(set1);
        union.addAll(set2);

        Set<Object> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        union.addAll(set2);


        Set<Object> difference = new HashSet<>(set1);
        difference.addAll(set2);
        difference.removeAll(intersection);
    }

}
