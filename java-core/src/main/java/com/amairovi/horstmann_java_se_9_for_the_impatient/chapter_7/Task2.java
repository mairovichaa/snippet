package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_7;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class Task2 {

    private static void m1(List<String> list) {
        Iterator<String> iterator = list.iterator();
        List<String> swap = new ArrayList<>();
        while (iterator.hasNext()) {
            String upperCase = iterator.next().toUpperCase();
            swap.add(upperCase);

            iterator.remove();
        }
        list.addAll(swap);
    }

    static void m2(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            list.set(i, list.get(i).toLowerCase());
        }
    }


    static void m3(List<String> list) {
        list.replaceAll(String::toUpperCase);
    }

    public static void main(String[] args) {
        ArrayList<String> objects = new ArrayList<>();
        objects.add("str1");
        objects.add("str2");
        objects.add("str3");
        objects.add("abc");

        m1(objects);
        System.out.println(objects);

        m2(objects);
        System.out.println(objects);

        m3(objects);
        System.out.println(objects);
    }

}
