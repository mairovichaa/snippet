package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_4.secttion_5;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.DoubleFunction;

public class Task13 {


    private static void applyForRange(Method staticMethod, double lowerBound, double upperBound, double step) {
        try {
            for (double i = lowerBound; i < upperBound; i += step) {
                Object result = staticMethod.invoke(null, i);
                System.out.println(String.format("%f: %s", i, result));
            }
            Object result = staticMethod.invoke(null, upperBound);
            System.out.println(String.format("%f: %s", upperBound, result));

        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static void applyForRange(DoubleFunction<Object> function, double lowerBound, double upperBound, double step) {
        for (double i = lowerBound; i < upperBound; i += step) {
            Object result = function.apply(i);
            System.out.println(String.format("%f: %s", i, result));
        }
        Object result = function.apply(upperBound);
        System.out.println(String.format("%f: %s", upperBound, result));

    }

    public static void main(String[] args) throws NoSuchMethodException {

        //reflection
        Method sqrt = Math.class.getMethod("sqrt", double.class);
        applyForRange(sqrt, 0, 10, 1);

        System.out.println();

        Method toHexString = Double.class.getMethod("toHexString", double.class);
        applyForRange(toHexString, 0, 10, 1);

        System.out.println();


        //function
        applyForRange(Math::sqrt, 0, 10, 1);

        System.out.println();

        applyForRange(Double::toHexString, 0, 10, 1);

    }

}
