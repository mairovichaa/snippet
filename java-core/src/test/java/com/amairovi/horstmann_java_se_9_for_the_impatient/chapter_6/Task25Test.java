package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_6;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_6.Task25.genericDeclaration2;
import static org.assertj.core.api.Assertions.assertThat;

class Task25Test {


    @Test
    void when_simple_then_return_simple() throws NoSuchMethodException {
        class Clazz {

            public <T> void m() {
            }

        }

        Method m = Clazz.class.getMethod("m");
        List<String> actual = genericDeclaration2(m);

        assertThat(actual).containsExactly("T - simple param.");

    }

    @Test
    void when_couple_of_simple_types_then_return_all() throws NoSuchMethodException {
        class Clazz {

            public <T, K, V> void m() {
            }

        }

        Method m = Clazz.class.getMethod("m");
        List<String> actual = genericDeclaration2(m);

        assertThat(actual).containsExactly("T - simple param.", "K - simple param.", "V - simple param.");

    }

}
