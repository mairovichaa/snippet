package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_10;

import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Task26 {
    public static <T> CompletableFuture<T> repeat(Supplier<T> action, Predicate<T> until) {
        return CompletableFuture.supplyAsync(() -> {
                    while (true) {
                        T value = action.get();
                        if (until.test(value)) {
                            return value;
                        }
                    }
                }
        );
    }


    public static void main(String[] args) {
        Supplier<Integer> supplier = new Supplier<Integer>() {
            private int num;

            @Override
            public Integer get() {
                System.out.println("get for " + num);
                return num++;
            }
        };

        repeat(supplier, num -> num == 3)
                .thenAccept(num -> System.out.println("thenAccept for " + num));
    }
}
