package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_6;

import org.junit.jupiter.api.Test;

import java.io.Closeable;
import java.util.Arrays;
import java.util.List;

import static com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_6.Task14.closeAll;
import static org.assertj.core.api.Assertions.assertThat;

class Task14Test {


    @Test
    void when_several_exception_then_chain_them() {
        Closeable c1 = () -> {
            throw new IllegalArgumentException("c1");
        };

        Closeable c2 = () -> {
            throw new IllegalArgumentException("c2");
        };

        Closeable c3 = () -> {
            throw new IllegalArgumentException("c3");
        };


        List<Closeable> closeables = Arrays.asList(c1, c2, c3);
        try {
            closeAll(closeables);
        } catch (Exception e) {
            assertThat(e.getCause().getMessage()).isEqualTo("c3");
            assertThat(e.getCause().getCause().getMessage()).isEqualTo("c2");
            assertThat(e.getCause().getCause().getCause().getMessage()).isEqualTo("c1");
            assertThat(e.getCause().getCause().getCause().getCause()).isNull();
            return;
        }
        // should not come to this line
        assertThat(true).isFalse();
    }

}
