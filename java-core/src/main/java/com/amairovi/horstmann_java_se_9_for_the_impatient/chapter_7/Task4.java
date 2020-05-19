package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_7;

import java.util.*;

public class Task4 {


    public static <E> void problem(List<E> list, E target) {
        for (E e : list) {
            if (e.equals(target)) {
                list.remove(e); // throws ConcurrentModificationException if list contains target
            }
        }
    }
    public static void main(String[] args) {
        ArrayList<Object> list = new ArrayList<>();
        Object target = new Object();
        list.add(target);
        problem(list, target);
    }

    public static <E> void iterator(List<E> list, E target) {
        for (ListIterator<E> iterator = list.listIterator(); iterator.hasNext(); ) {
            E e = iterator.next();
            if (e.equals(target)) {
                iterator.remove();
            }
        }
    }

    public static <E> void remove(List<E> list, E target) {
        list.remove(target);
    }

    public static <E> void removeIf(List<E> list, E target) {
        list.removeIf(e -> e.equals(target));
    }
}
