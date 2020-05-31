package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_8;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class Task12 {

    public static <T> boolean isFinite(Stream<T> stream) {
        return stream.count() != -1;
    }

    public static void main(String[] args) {
        assertThat(isFinite(Stream.of(1, 2, 3, 4))).isTrue();
//      will work forever
//      assertThat(isFinite(Stream.generate(() -> 1))).isFalse();
    }

}
