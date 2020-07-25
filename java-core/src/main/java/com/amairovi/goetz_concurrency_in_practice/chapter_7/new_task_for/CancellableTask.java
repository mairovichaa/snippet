package com.amairovi.goetz_concurrency_in_practice.chapter_7.new_task_for;

import java.util.concurrent.Callable;
import java.util.concurrent.RunnableFuture;

public interface CancellableTask<T> extends Callable<T> {

    void cancel();

    RunnableFuture<T> newTask();

}
