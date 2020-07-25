package com.amairovi.goetz_concurrency_in_practice.chapter_6;

import java.util.concurrent.Executor;

public class WithinThreadExecutor implements Executor {

    @Override
    public void execute(Runnable r) {
        r.run();
    }

}
