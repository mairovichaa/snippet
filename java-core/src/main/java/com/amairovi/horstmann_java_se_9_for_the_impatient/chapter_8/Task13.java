package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_8;

import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class Task13 {

    private static class ZipSupplier<T> implements Supplier<T> {

        private final Iterator<T> firstIt;
        private final Iterator<T> secondIt;
        private boolean turnOfFirst = true;

        private ZipSupplier(Stream<T> first, Stream<T> second) {
            this.firstIt = first.iterator();
            this.secondIt = second.iterator();
        }


        @Override
        public T get() {
            if (turnOfFirst) {
                turnOfFirst = false;
                return firstIt.hasNext() ? firstIt.next() : null;
            } else {
                turnOfFirst = true;
                return secondIt.hasNext() ? secondIt.next() : null;
            }
        }

    }

    private static <T> Stream<T> zip(Stream<T> first, Stream<T> second) {
        ZipSupplier<T> supplier = new ZipSupplier<>(first, second);
        return Stream.generate(supplier);
    }

    public static void main(String[] args) {
        Stream<Integer> first = Stream.generate(() -> 2);
        Stream<Integer> second = Stream.of(3, 5);

        List<Integer> result = zip(first, second)
                .limit(8)
                .collect(Collectors.toList());

        assertThat(result).containsExactly(2, 3, 2, 5, 2, null, 2, null);
    }

}
