package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_6;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

class Task2 {

    static class ObjectArrayStack<E> {

        private Object[] array;
        private int size;
        private int currentPos;

        ObjectArrayStack() {
            array = new Object[0];
            size = 0;
            currentPos = 0;
        }


        ObjectArrayStack(List<E> list) {
            array = list.toArray();
            size = array.length;
            currentPos = array.length;
        }

        void push(E elem) {
            if (currentPos == size) {
                if (size == 0) {
                    size = 1;
                }
                array = Arrays.copyOf(array, size * 2);
            }

            array[currentPos] = elem;
            currentPos++;
        }

        boolean isEmpty() {
            return size == 0;
        }

        @SuppressWarnings("unchecked")
        E pop() {
            if (currentPos == 0) {
                throw new NoSuchElementException("Stack is empty");
            }
            int indexOfTheLastElement = currentPos - 1;


            return (E) array[indexOfTheLastElement];
        }

        Object[] getArray() {
            return array;
        }

    }

    static class GenericArrayStack<E> {

        private E[] array;
        private int size;
        private int currentPos;


        @SuppressWarnings("unchecked")
        GenericArrayStack() {
            array = (E[]) new Object[0];
            size = 0;
            currentPos = 0;
        }

        @SuppressWarnings("unchecked")
        GenericArrayStack(List<E> list) {
            array = (E[]) list.toArray();
            size = array.length;
            currentPos = array.length;
        }

        void push(E elem) {
            if (currentPos == size) {
                if (size == 0) {
                    size = 1;
                }
                array = Arrays.copyOf(array, size * 2);
            }

            array[currentPos] = elem;
            currentPos++;
        }

        boolean isEmpty() {
            return size == 0;
        }

        E pop() {
            if (currentPos == 0) {
                throw new NoSuchElementException("Stack is empty");
            }
            int indexOfTheLastElement = currentPos - 1;


            return array[indexOfTheLastElement];
        }

        E[] getArray() {
            return array;
        }

    }

}
