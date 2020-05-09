package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_6;

public class Task5 {

    public static <T> T[] swap(int i, int j, T... values) {
        T temp = values[i];
        values[i] = values[j];
        values[j] = temp;

        return values;
    }

    public static void main(String[] args) {
//  Error:(14, 31) java: incompatible types: inferred type does not conform to upper bound(s)
//  inferred: java.lang.Number&java.lang.Comparable<? extends java.lang.Number&java.lang.Comparable<?>>
//  upper bound(s): java.lang.Double,java.lang.Object
//  Double[] result = swap(0, 1, 1.5, 2, 3);

//  Error:(19, 32) java: method swap in class com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_6.Task5 cannot be applied to given types;
//  required: int,int,T[]
//  found: int,int,double,int,int
//  reason: varargs mismatch; int cannot be converted to java.lang.Double
//  Double[] result = Task5.<Double>swap(0, 1, 1.5, 2, 3);

        // fixed
        Double[] result = Task5.<Double>swap(0, 1, 1.5, 2.0, 3.0);
        // another approach
        Number[] result2 = Task5.swap(0, 1, 1.5, 2, 3);


//        Number[] result2 = Task5.<Double>swap(0, 1, 1.5, 2, 3);
//        Object[] result2 = Task5.<Double>swap(0, 1, 1.5, 2, 3);


    }

}
