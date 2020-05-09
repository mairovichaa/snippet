package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_6;

import java.util.concurrent.Callable;

public class Task23 {

    @SuppressWarnings("unchecked")
    public static <T extends Throwable> void throwAs(Throwable e) throws T {
        throw (T) e;
    }

    public static <V> V doWork(Callable<V> c) {
        try {
            return c.call();
        } catch (Throwable ex) {

            Task23.throwAs(ex);
            return null;
        }
    }

    static void m() {
        Task23.throwAs(new Exception());
    }

    public static void main(String[] args) {
        doWork(() -> {
            throw new Exception("");
        });
    }

}
