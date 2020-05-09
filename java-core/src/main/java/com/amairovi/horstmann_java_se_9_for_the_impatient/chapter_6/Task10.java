package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_6;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;

import static java.util.Comparator.naturalOrder;

public class Task10 {


    public static <E extends Comparable<? super E>> E min(ArrayList<? extends E> arr) {
        return Collections.min(arr);
    }

    public static <E extends Comparable<? super E>> E max(ArrayList<? extends E> arr) {
        return Collections.max(arr);
    }

}
