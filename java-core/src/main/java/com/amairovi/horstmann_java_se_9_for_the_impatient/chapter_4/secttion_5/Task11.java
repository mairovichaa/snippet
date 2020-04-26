package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_4.secttion_5;

import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Task11 {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Class<System> systemClass = System.class;

        Field outField = systemClass.getField("out");

        Object outObj = outField.get(null);

        if (outObj instanceof PrintStream) {
            PrintStream out = (PrintStream) outObj;

            Method println = PrintStream.class.getMethod("println", String.class);

            println.invoke(out, "Hello world");
        }
    }

}
