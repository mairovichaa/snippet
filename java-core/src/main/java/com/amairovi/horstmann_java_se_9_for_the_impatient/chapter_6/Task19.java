package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_6;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class Task19 {

    public static <T> ArrayList<T> repeat(int n, T obj) {
        ArrayList<T> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            result.add(obj);
        }
        return result;
    }

    public static void main(String[] args) {
        ArrayList<String> repeat = repeat(1, "123");
        String[] construct = Task21.construct(10, repeat.get(0));

        assertThat(construct[0]).isEqualTo("123");
    }

}
