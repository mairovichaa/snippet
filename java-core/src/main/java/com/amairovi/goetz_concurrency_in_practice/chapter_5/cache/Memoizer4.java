package com.amairovi.goetz_concurrency_in_practice.chapter_5.cache;

import java.util.Map;
import java.util.concurrent.*;

public class Memoizer4<A, V> implements Computable<A, V> {

    private final Map<A, Future<V>> cache = new ConcurrentHashMap<>();
    private final Computable<A, V> c;

    public Memoizer4(Computable<A, V> c) {
        this.c = c;
    }

    @Override
    public V compute(A arg) throws InterruptedException {
        Future<V> f = cache.get(arg);

        if (f == null) {
            Callable<V> eval = () -> c.compute(arg);

            FutureTask<V> futureTask = new FutureTask<>(eval);
            Future<V> currentValue = cache.putIfAbsent(arg, futureTask);

            if (currentValue == null) {
                f = futureTask;
                futureTask.run();
            } else {
                f = currentValue;
            }
        }

        try {
            return f.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

}
