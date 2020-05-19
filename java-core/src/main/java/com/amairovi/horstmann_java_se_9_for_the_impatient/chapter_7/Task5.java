package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_7;

import java.util.*;

public class Task5 {

    public static void swap(List<?> list, int i, int j) {
        if (list instanceof RandomAccess) {
            usualSwap(list, i, j);
        } else {
            sequentialSwap(list, i, j);

        }
    }

    private static <E> void sequentialSwap(List<E> list, int i, int j) {
        ListIterator<E> iterator1 = list.listIterator();
        ListIterator<E> iterator2 = list.listIterator();
        for (int k = 0; k < i; k++) {
            iterator1.next();
            iterator2.next();
        }

        for (int k = 0; k < j - i; k++) {
            iterator2.next();
        }

        E swap = iterator1.next();
        iterator1.set(iterator2.next());
        iterator2.set(swap);
    }

    private static <E> void sequentialSwap2(List<E> list, int i, int j) {
        ListIterator<E> iterator = list.listIterator(i);
        E first = iterator.next();
        for (int k = i + 1; k < j; k++) {
            iterator.next();
        }
        E second = iterator.next();
        iterator.set(first);
        for (int k = j; k >= i; k--) {
            iterator.previous();
        }
        iterator.set(second);
    }

    private static <E> void usualSwap(List<E> list, int i, int j) {
        E swap = list.get(i);
        list.set(i, list.get(j));
        list.set(j, swap);
    }

    public static void main(String[] args) {
        LinkedList<Integer> objects = new LinkedList<>();

        for (int i = 0; i < 10; i++) {
            objects.add(i);
        }

        swap(objects, 0, 1);

        System.out.println(objects);
    }

}
