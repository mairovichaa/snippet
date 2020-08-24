package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_10;

import java.util.Arrays;

public class Task24 {

    public static class Stack {
        private Object[] values = new Object[10];
        private int size;

        public void push(Object newValue) {
            synchronized (values) {
                if (size == values.length) {
                    // this assignment breaks synchronization
                    // concurrent thread could enter this synchronized section as monitor is the different
                    values = Arrays.copyOf(values, 2 * size);
                }

                // the following two lines could be executed concurrently
                values[size] = newValue;
                size++;
            }
        }
    }
}
