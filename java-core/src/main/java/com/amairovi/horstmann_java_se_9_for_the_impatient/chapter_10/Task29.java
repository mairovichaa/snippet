package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_10;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class Task29 {

    @SuppressWarnings("unchecked")
    static <T> CompletableFuture<T> anyOf(List<Supplier<T>> actions, Executor executor) {
        int amountOfElements = actions.size();
        System.out.println("Amount of elements: " + amountOfElements);
        BlockingQueue<Object> queue = new LinkedBlockingDeque<>();

        Consumer<Object> addToQueue = obj -> {
            while (true) {
                try {
                    queue.put(obj);
                    return;
                } catch (InterruptedException ignored) {
                }
            }
        };

        return CompletableFuture.supplyAsync(() -> {
            for (Supplier<T> action : actions) {
                executor.execute(() -> {
                    try {
                        T value = action.get();
                        System.out.println("Add " + value + " to queue");
                        addToQueue.accept(value);
                    } catch (Exception e) {
                        System.out.println("Add " + e + " to queue");
                        addToQueue.accept(e);
                    }
                });
            }

            for (int i = 0; i < amountOfElements; i++) {
                while (true) {
                    try {
                        Object value = queue.take();
                        System.out.println("Retrieve " + value);
                        if (!(value instanceof Exception)) {
                            return (T) value;
                        }
                        break;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            throw new NoSuchElementException();
        });
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        Supplier<Integer> error = () -> {
            throw new IllegalArgumentException();
        };

        System.out.println("There is a successful supplier");
        CompletableFuture<Integer> future = anyOf(Arrays.asList(error, () -> 10, error), executorService);
        Integer result = future.get();
        System.out.println(result);
        assertThat(result).isEqualTo(10);
        System.out.println("finished");


        System.out.println("There is no a successful supplier");
        future = anyOf(Arrays.asList(error, error, error), executorService);
        assertThatThrownBy(future::get)
                .isInstanceOf(ExecutionException.class)
                .hasCauseExactlyInstanceOf(NoSuchElementException.class);
        System.out.println("finished");

        executorService.shutdown();
    }

}
