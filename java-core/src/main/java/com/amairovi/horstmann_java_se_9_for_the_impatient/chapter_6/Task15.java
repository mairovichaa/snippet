package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_6;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Task15 {

    public static <T, R> List<R> map(List<T> list, Function<T, R> function){
        List<R> result = new ArrayList<>();
        for (T elem: list) {
            R newElem = function.apply(elem);
            result.add(newElem);
        }
        return result;
    }
}
