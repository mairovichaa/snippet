package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_6;


import java.util.Comparator;
import java.util.List;

import static com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_6.Task12.minmax;

public class Task13 {

    public static <T> void maxmin(List<T> elements, Comparator<? super T> comp, List<? super T> result) {
        minmax(elements, comp, result);

        swap(result, 0, 1);
    }

    public static void swap(List<?> elements, int idx1, int idx2) {
//      ? swap =  elements.get(idx2) is not valid
        swapHelper(elements, idx1, idx2);
    }

    public static <T> void swapHelper(List<T> elements, int idx1, int idx2) {
        T elem1 = elements.set(idx1, elements.get(idx2));
        elements.set(idx2, elem1);
    }

}
