package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_6.section_6;

import java.lang.reflect.Array;

import static org.assertj.core.api.Assertions.assertThat;

public class GenericRepeat {

    public static void main(String[] args) {
        Long[] repeat = repeat(3, 3L);

        assertThat(repeat).containsExactly(3L, 3L, 3L);
    }

    public static <T> T[] repeat(int size, T obj) {
        Class<?> aClass = obj.getClass();
        T[] array = (T[]) Array.newInstance(aClass, size);
        for (int i = 0; i < size; i++) {
            array[i] = obj;
        }
        return array;
    }

}
