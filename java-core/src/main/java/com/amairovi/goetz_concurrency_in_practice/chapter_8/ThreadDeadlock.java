package com.amairovi.goetz_concurrency_in_practice.chapter_8;

import java.util.concurrent.*;

public class ThreadDeadlock {

    static ExecutorService executor = Executors.newSingleThreadExecutor();

    public static class RenderPageTask implements Callable<String> {

        @Override
        public String call() throws Exception {
            Future<String> header, footer;

            header = executor.submit(new LoadFileTask("header.html"));
            footer = executor.submit(new LoadFileTask("footer.html"));

            String page = renderBody();

            //Will deadlock - task is waiting for results of sub tasks
            return header.get() + page + footer.get();
        }

        private String renderBody() {
            return "";
        }

    }

    private static class LoadFileTask implements Callable<String> {

        public LoadFileTask(String s) {
        }


        @Override
        public String call() {
            return null;
        }

    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Future<String> submit = executor.submit(new RenderPageTask());
        submit.get();
        executor.shutdown();
    }

}
