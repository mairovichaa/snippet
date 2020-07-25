package com.amairovi.goetz_concurrency_in_practice.chapter_3;

public class ThreadLocalSample {

    ThreadLocal<String> local = ThreadLocal.withInitial(() -> Thread.currentThread().getName());

    {
        local.get();
    }
}
