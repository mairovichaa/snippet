package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_7;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ToArrayTest {

    public static void main(String[] args) {
        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 5);
        assertThatThrownBy(() -> integers.add(6))
                .isInstanceOf(UnsupportedOperationException.class);
    }

}
