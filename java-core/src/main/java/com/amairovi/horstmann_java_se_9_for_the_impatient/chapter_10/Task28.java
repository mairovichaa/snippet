package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_10;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Task28 {
    static <T> CompletableFuture<List<T>> allOf(List<CompletableFuture<T>> cfs) {
        List<T> result = new ArrayList<>();
        for (CompletableFuture<T> f : cfs) {
            try {
                result.add(f.get());
            } catch (InterruptedException | ExecutionException e) {
                result.add(null);
            }
        }
        return CompletableFuture.completedFuture(result);
    }
}
