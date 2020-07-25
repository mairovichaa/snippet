package com.amairovi.goetz_concurrency_in_practice.chapter_5.cache;

import java.util.Map;
import java.util.concurrent.*;

public class Memoizer3<A, V> implements Computable<A, V> {

    private final Map<A, Future<V>> cache = new ConcurrentHashMap<>();
    private final Computable<A, V> c;

    public Memoizer3(Computable<A, V> c) {
        this.c = c;
    }

    @Override
    public V compute(A arg) throws InterruptedException {
        Future<V> f = cache.get(arg);

        if (f == null) {
            Callable<V> eval = () -> c.compute(arg);

            FutureTask<V> futureTask = new FutureTask<>(eval);
            f = futureTask;

            cache.put(arg, futureTask);

            futureTask.run();
        }

        try {
            return f.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

}
