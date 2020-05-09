package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_6;

import java.util.ArrayList;

public class Task9 {

    private static class Pair<E extends Comparable<? super E>> {

        private final E first;
        private final E second;

        private Pair(E first, E second) {
            this.first = first;
            this.second = second;
        }
    }

    public static <E extends Comparable<? super E>> Pair<E> first(ArrayList<? extends E> e) {
        E first = e.get(0);
        E last = e.get(e.size() - 1);
        return new Pair<>(first, last);
    }

}
