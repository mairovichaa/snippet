package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_6;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class Task16 {

//    Signature: #242                         // <T::Ljava/lang/Comparable<-TT;>;>(Ljava/util/List<TT;>;)V
//
//    from Collections - before compilation
//        public static <T extends Comparable<? super T>> void sort(List<T> list)
//    after erasure - Comparable


//    Signature: #279                         // <T:Ljava/lang/Object;:Ljava/lang/Comparable<-TT;>;>(Ljava/util/Collection<+TT;>;)TT;
//
//    from Collections - before compilation
//        public static <T extends Object & Comparable<? super T>> T max(Collection<? extends T> coll) {
//    after erasure - Object

    public static void main(String[] args) throws NoSuchMethodException {
        Method sort = Collections.class.getMethod("sort", List.class);
        Method max = Collections.class.getMethod("max", Collection.class);

        System.out.println(max);
    }
}
