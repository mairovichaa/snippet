package com.amairovi.goetz_concurrency_in_practice.chapter_8;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class Parallelizing {

    void processSequentially(List<Object> objects) {
        for (Object o :
                objects) {
            process(o);
        }
    }

    void processInParallel(Executor executor, List<Object> objects) {
        for (final Object o : objects) {
            executor.execute(() -> process(o));
        }
    }

    private void process(Object o) {

    }

    <T, R> void sequentialRecursive(List<Node<T>> nodes, Collection<R> results) {
        for (Node<T> n : nodes) {
            results.add(n.compute());
            sequentialRecursive(n.getChildren(), results);
        }
    }

    <T, R> void parallelRecursive(final Executor executor, List<Node<T>> nodes, final Collection<R> results) {
        for (final Node<T> n : nodes) {
            executor.execute(() -> results.add(n.compute()));
            parallelRecursive(executor, n.getChildren(), results);
        }
    }

    public <T, R> Collection<R> getParallelResults(List<Node<T>> nodes) throws InterruptedException {
        ExecutorService executor = Executors.newCachedThreadPool();
        ConcurrentLinkedQueue<R> result = new ConcurrentLinkedQueue<>();
        parallelRecursive(executor, nodes, result);
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        return result;
    }


    private static class Node<T> {

        public List<Node<T>> getChildren() {
            return Collections.emptyList();
        }

        public <R> R compute() {
            return null;
        }

    }

}


