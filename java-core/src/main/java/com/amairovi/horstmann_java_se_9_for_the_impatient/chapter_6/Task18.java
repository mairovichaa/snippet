package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_6;

import java.util.Arrays;
import java.util.function.IntFunction;

import static org.assertj.core.api.Assertions.assertThat;

public class Task18 {

    public static <T> T[] repeat(int n, T obj, IntFunction<T[]> constr) {
        T[] apply = constr.apply(n);
        Arrays.fill(apply, obj);
        return apply;
    }

    public static <T> T[] repeat2(int n, T obj, IntFunction<?> constr) {
        Object temp = constr.apply(n);
        if (temp instanceof int[] && obj instanceof Integer) {
            Integer[] integers = new Integer[n];
            Arrays.fill(integers, obj);

            return (T[]) integers;
        }

        T[] result = (T[]) temp;
        Arrays.fill(result, obj);
        return result;
    }

    public static void main(String[] args) {
        IntFunction<int[]> c1 = int[]::new;
        Integer[] integers = repeat2(5, 42, c1);
        assertThat(integers).containsExactly(42, 42, 42, 42, 42);


        IntFunction<Object[]> c2 = Object[]::new;
        Object expected = new Object();
        Object[] objects = repeat2(5, expected, c2);
        assertThat(objects).containsExactly(expected, expected, expected, expected, expected);
    }

}
