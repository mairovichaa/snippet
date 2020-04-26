package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_4.secttion_5;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

public class Task12 {

    private static String[] params = {"p1", "p2", "p3"};

    static class Clazz {

        public String method(String param) {
            return param;
        }

    }

    private static int amountOfIteration = 2000000;

    public static void main(String[] args) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Clazz clazz = new Clazz();

        List<Long> measures1 = new ArrayList<>();
        String res = "2";

        for (int i = 0; i < amountOfIteration; i++) {
            String param = params[i % params.length];
            long start = System.nanoTime();
            res = clazz.method(param);
            long end = System.nanoTime();
            measures1.add(end - start);
        }

        System.out.println(res);
        OptionalDouble average = measures1.stream()
                .mapToLong(Long::longValue)
                .average();

        average.ifPresent(System.out::println);

        List<Long> measures2 = new ArrayList<>();

        Method method = Clazz.class.getMethod("method", String.class);
        for (int i = 0; i < amountOfIteration; i++) {
            String param = params[i % params.length];

            long start = System.nanoTime();
            res = (String) method.invoke(clazz, param);
            long end = System.nanoTime();
            measures2.add(end - start);
        }

        System.out.println(res);
        OptionalDouble average2 = measures2.stream()
                .mapToLong(Long::longValue)
                .average();

        average2.ifPresent(System.out::println);
    }

}
