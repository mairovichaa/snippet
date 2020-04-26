package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_4.secttion_5;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class Task10 {

    public static void main(String[] args) {
        Class<?> clazz = int[].class;

        while (clazz != null) {
            System.out.println(clazz.getName());

            for (Method m : clazz.getDeclaredMethods()) {
                String methodDescription = Modifier.toString(m.getModifiers()) + " "
                        + m.getReturnType().getCanonicalName() + " "
                        + m.getName() +
                        Arrays.toString(m.getParameters());

                System.out.println(methodDescription);
            }
            System.out.println();

            clazz = clazz.getSuperclass();
        }
    }

}
