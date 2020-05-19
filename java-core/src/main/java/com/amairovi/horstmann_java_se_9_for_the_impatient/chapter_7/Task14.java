package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_7;

import java.util.AbstractList;
import java.util.RandomAccess;

import static java.util.function.UnaryOperator.identity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class Task14 {

    private static class Numbers extends AbstractList<Integer> implements RandomAccess {

        private final int N;


        private Numbers(int n) {
            N = n;
        }

        @Override
        public Integer get(int index) {
            if (index < 0 || index > N) {
                throw new IllegalArgumentException("List contains numbers from 0 to "+ N +" inclusively: index=" + index);
            }
            return index;
        }

        @Override
        public int size() {
            return N + 1;
        }

    }


    public static void main(String[] args) {
        int N = 4;
        Numbers numbers = new Numbers(N);

        assertThatThrownBy(numbers::clear).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> numbers.add(0, 10)).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> numbers.replaceAll(identity())).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> numbers.add(1)).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> numbers.sort(Integer::compareTo)).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(numbers::toArray).isInstanceOf(UnsupportedOperationException.class);

        assertThat(numbers.size()).isEqualTo(N + 1);
        for (int i = 0; i <= N; i++) {
            assertThat(numbers.get(i)).isEqualTo(i);
            assertThat(numbers.contains(i)).isTrue();
        }

        int count = 0;
        for (Integer i : numbers) {
            assertThat(i).isEqualTo(count);
            count++;
        }
    }

}
