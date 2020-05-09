package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_6;

import java.util.ArrayList;
import java.util.List;

public class Task6 {

    static <E> void append1(List<E> l1, List<E> l2) {
        l1.addAll(l2);
    }

    static <E> void append2(List<? super E> l1, List<E> l2) {
        l1.addAll(l2);
    }

    static <E> void append3(List<E> l1, List<? extends E> l2) {
        l1.addAll(l2);
    }

    static <E> void append4(List<? super E> l1, List<? extends E> l2) {
        l1.addAll(l2);
    }

    public static void main(String[] args) {
        ArrayList<Object> lo1 = new ArrayList<>();
        ArrayList<Object> lo2 = new ArrayList<>();
        ArrayList<String> ls1 = new ArrayList<>();
        ArrayList<String> ls2 = new ArrayList<>();

        append1(lo1, lo2);
        append1(ls1, ls2);
//        should work
//        append1(lo1, ls1);


        append2(lo1, lo2);
        append2(ls1, ls2);
        append2(lo1, ls1);

        append3(lo1, lo2);
        append3(ls1, ls2);
        append3(lo1, ls1);


        append4(lo1, lo2);
        append4(ls1, ls2);
        append4(lo1, ls1);
    }

}
