package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_6;

import java.util.ArrayList;
import java.util.List;

public class Task14 {

    public static <T extends AutoCloseable> void closeAll(List<T> elems) throws Exception {

        List<Exception> exceptions = new ArrayList<>();

        for (T elem : elems) {
            try {
                elem.close();
            } catch (Exception e) {
                exceptions.add(e);
            }
        }

        if (exceptions.isEmpty()) {
            return;
        }

        int size = exceptions.size();
        for (int i = 0; i < size - 1; i++) {
            Exception prevException = exceptions.get(i);
            Exception nextException = exceptions.get(i + 1);
            nextException.initCause(prevException);
        }

        Exception lastException = exceptions.get(size - 1);
        throw new Exception(lastException);
    }


    private static class CloseAllException extends RuntimeException {

        private final List<Exception> exceptions = new ArrayList<>();

        CloseAllException(List<Exception> exceptions) {

        }

    }

    public static <T extends AutoCloseable> void closeAll2(ArrayList<T> elems) throws Exception {

        List<Exception> exceptions = new ArrayList<>();

        for (T elem : elems) {
            try {
                elem.close();
            } catch (Exception e) {
                exceptions.add(e);
            }
        }

        if (!exceptions.isEmpty()) {
            throw new CloseAllException(exceptions);
        }
    }
}
