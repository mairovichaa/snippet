package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_7;

import java.util.Collections;
import java.util.List;

public class Task18 {

    public static void main(String[] args) {
        // raw type
        List emptyList = Collections.EMPTY_LIST;

        //warning
        List<Integer> l = Collections.EMPTY_LIST;
        List<Integer> l2 = Collections.emptyList();

    }
}
