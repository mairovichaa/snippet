package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_6;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class Task21 {


    @SafeVarargs
    public static <T> T[] construct(int size, T... empty) {
        return Arrays.copyOf(empty, size);
    }


    public static void main(String[] args) {
//        ArrayList<String>[] arrayLists = new ArrayList<String>[100];
        List<String>[] result = Task21.construct(10);
        assertThat(result.length).isEqualTo(10);

        result[0] = new ArrayList<>();
        result[0].add("test string");

        assertThat(result[0].get(0)).isEqualTo("test string");


        Pair<String, String>[] result2 = Task21.construct(10);
        assertThat(result2.length).isEqualTo(10);

    }

    private static class Pair<T, T1> {
    }
}
