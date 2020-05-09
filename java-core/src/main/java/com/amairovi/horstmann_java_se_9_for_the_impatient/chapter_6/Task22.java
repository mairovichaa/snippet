package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_6;

import java.util.concurrent.Callable;
import java.util.function.*;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class Task22 {

    public static <V, T extends Throwable> V doWork(Callable<V> c, T ex) throws T {
        try {
            return c.call();
        } catch (Throwable realEx) {
            ex.initCause(realEx);
            throw ex;
        }
    }

    public static <V, T extends Throwable> V doWork2(Callable<V> c, Function<Throwable, ? extends T> exceptionCreator) throws T {
        try {
            return c.call();
        } catch (Throwable realEx) {
            throw exceptionCreator.apply(realEx);

        }
    }

    public static void main(String[] args) {
        assertThatThrownBy(() ->
                doWork2(
                        () -> {
                            throw new IllegalArgumentException("real exception");
                        },
                        realEx -> new IllegalArgumentException("wrapper", realEx)
                )
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("wrapper")
                .hasCauseExactlyInstanceOf(IllegalArgumentException.class)
                .hasRootCauseMessage("real exception");

    }

}
