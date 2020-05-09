package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_6;

import java.util.ArrayList;
import java.util.Collections;

public class Task11 {


    private static class Pair<E extends Comparable<? super E>> {

        private final E first;
        private final E second;

        private Pair(E first, E second) {
            this.first = first;
            this.second = second;
        }

    }

    public static <E extends Comparable<? super E>> Pair<E> minMax(ArrayList<? extends E> arr) {
        E max = Collections.max(arr);
        E min = Collections.min(arr);
        return new Pair<>(min, max);
    }

}
