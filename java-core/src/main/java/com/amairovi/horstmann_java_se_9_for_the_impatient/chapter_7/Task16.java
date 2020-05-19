package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_7;


import com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_7.Task13.Cache;
import com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_7.Task15.Numbers;

import java.util.AbstractList;
import java.util.RandomAccess;
import java.util.function.UnaryOperator;

class Task16 {

    private static class NumbersCached extends AbstractList<Integer> implements RandomAccess {

        private final Numbers numbers;
        private final Cache<Integer, Integer> cache;

        private NumbersCached(UnaryOperator<Integer> func, int cacheSize) {
            numbers = new Numbers(func);
            cache = new Cache<>(cacheSize);
        }

        @Override
        public Integer get(int index) {
            if (cache.containsKey(index)) {
                return cache.get(index);
            } else {
                Integer value = numbers.get(index);
                cache.put(index, value);
                return value;
            }
        }

        @Override
        public int size() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {
        NumbersCached numbers = new NumbersCached(x -> x * x, 2);

        System.out.println(numbers.get(10));
        System.out.println(numbers.get(10));
        System.out.println(numbers.get(20));
        System.out.println(numbers.get(20));
        System.out.println(numbers.get(30));
        System.out.println(numbers.get(20));
    }

}
