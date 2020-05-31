package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class Task14 {

    private static <T> ArrayList<T> flatten1(Stream<ArrayList<T>> stream) {
        BinaryOperator<ArrayList<T>> op = (l1, l2) -> {
            l1.addAll(l2);
            return l1;
        };
        return stream.reduce(op).orElse(new ArrayList<>());
    }

    private static <T> ArrayList<T> flatten2(Stream<ArrayList<T>> stream) {
        BinaryOperator<ArrayList<T>> op = (l1, l2) -> {
            l1.addAll(l2);
            return l1;
        };
        return stream.reduce(new ArrayList<>(), op);
    }

    private static <T> ArrayList<T> flatten3(Stream<ArrayList<T>> stream) {
        BinaryOperator<ArrayList<T>> op = (l1, l2) -> {
            l1.addAll(l2);
            return l1;
        };
        return stream.reduce(new ArrayList<>(), op, op);
    }

    public static void main(String[] args) {
        ArrayList<Integer> l1 = new ArrayList<>();
        l1.addAll(Arrays.asList(1, 2, 3));
        ArrayList<Integer> l2 = new ArrayList<>();
        l2.addAll(Arrays.asList(4, 5, 6));

        Stream<ArrayList<Integer>> stream1 = Stream.of(new ArrayList<>(l1), new ArrayList<>(l2));
        ArrayList<Integer> flatten1 = flatten1(stream1);
        assertThat(flatten1).containsExactly(1, 2, 3, 4, 5, 6);

        Stream<ArrayList<Integer>> stream2 = Stream.of(new ArrayList<>(l1), new ArrayList<>(l2));
        ArrayList<Integer> flatten2 = flatten2(stream2);
        assertThat(flatten2).containsExactly(1, 2, 3, 4, 5, 6);

        Stream<ArrayList<Integer>> stream3 = Stream.of(new ArrayList<>(l1), new ArrayList<>(l2));
        ArrayList<Integer> flatten3 = flatten3(stream3);
        assertThat(flatten3).containsExactly(1, 2, 3, 4, 5, 6);

    }

}
