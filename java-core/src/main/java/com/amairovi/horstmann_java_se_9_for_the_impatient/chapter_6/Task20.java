package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_6;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class Task20 {

    @SafeVarargs
    private static final <T> T[] repeat(int n, T... objs) {
        int objLength = objs.length;
        T[] result = Arrays.copyOf(objs, n * objLength);

        for (int i = 0; i < objLength; i++) {
            T obj = objs[i];
            for (int j = 0; j < n; j++) {
                result[i + j * objLength] = obj;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        String[] repeat = repeat(3, "pam");
        assertThat(repeat).containsExactly("pam", "pam", "pam");

        String[] repeat2 = repeat(3, "p1", "p2", "p3");
        assertThat(repeat2).containsExactly("p1", "p2", "p3", "p1", "p2", "p3", "p1", "p2", "p3");
    }


}
