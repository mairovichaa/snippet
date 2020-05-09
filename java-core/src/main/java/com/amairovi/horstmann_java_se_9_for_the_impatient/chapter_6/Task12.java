package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_6;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Task12 {
    public static <T> void minmax(List<T> elements, Comparator<? super T> comp, List<? super T> result){
        T min = Collections.min(elements, comp);
        result.add(min);
        T max = Collections.max(elements, comp);
        result.add(max);
    }
}
