package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_6;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Task1 {

    @AllArgsConstructor
    @NoArgsConstructor
    public static class Stack<E> {

        private List<E> array = new ArrayList<>();

        public void push(E elem) {
            array.add(elem);
        }

        public boolean isEmpty() {
            return array.isEmpty();
        }

        public E pop() {
            int size = array.size();

            if (size == 0) {
                throw new NoSuchElementException("Stack is empty");
            }
            int indexOfTheLastElement = size - 1;
            return array.get(indexOfTheLastElement);
        }

    }

}
