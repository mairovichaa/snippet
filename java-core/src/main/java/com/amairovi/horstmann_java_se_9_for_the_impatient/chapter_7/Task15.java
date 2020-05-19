package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_7;

import java.util.AbstractList;
import java.util.RandomAccess;
import java.util.function.UnaryOperator;

class Task15 {

    static class Numbers extends AbstractList<Integer> implements RandomAccess {

        private final UnaryOperator<Integer> func;

        Numbers(UnaryOperator<Integer> func) {
            this.func = func;
        }

        @Override
        public Integer get(int index) {
            return func.apply(index);
        }

        @Override
        public int size() {
            throw new UnsupportedOperationException();
        }

    }

}
