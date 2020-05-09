package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_6;

import com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_6.Task2.GenericArrayStack;
import com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_6.Task2.ObjectArrayStack;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class Task2Test {


    @Nested
    class ObjectArrayStackTests {

        private ObjectArrayStack<Object> underTest;

        @Test
        void when_created_then_empty() {
            underTest = new ObjectArrayStack<>();

            assertThat(underTest.isEmpty()).isTrue();
        }


        @Test
        void when_created_with_non_empty_then_not_empty() {
            underTest = new ObjectArrayStack<>(singletonList(new Object()));

            assertThat(underTest.isEmpty()).isFalse();
        }

        @Test
        void when_add_and_empty_then_add() {
            ArrayList<Object> objects = new ArrayList<>();

            underTest = new ObjectArrayStack<>(objects);

            Object expected = new Object();
            underTest.push(expected);

            assertThat(underTest.getArray()).containsExactly(expected, null);
        }

        @Test
        void when_add_several_and_empty_then_add() {
            ArrayList<Object> objects = new ArrayList<>();

            underTest = new ObjectArrayStack<>(objects);

            Object expected = new Object();
            Object expected2 = new Object();
            underTest.push(expected);
            underTest.push(expected2);

            assertThat(underTest.getArray()).containsExactly(expected, expected2);
        }

        @Test
        void when_pop_and_empty_then_no_such_element_exception() {
            underTest = new ObjectArrayStack<>();

            assertThatThrownBy(() -> underTest.pop())
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessage("Stack is empty");
        }

    }


    @Nested
    class GenericArrayStackTests {

        private GenericArrayStack<Object> underTest;

        @Test
        void when_created_then_empty() {
            underTest = new GenericArrayStack<>();

            assertThat(underTest.isEmpty()).isTrue();
        }


        @Test
        void when_created_with_non_empty_then_not_empty() {
            underTest = new GenericArrayStack<>(singletonList(new Object()));

            assertThat(underTest.isEmpty()).isFalse();
        }

        @Test
        void when_add_and_empty_then_add() {
            ArrayList<Object> objects = new ArrayList<>();

            underTest = new GenericArrayStack<>(objects);

            Object expected = new Object();
            underTest.push(expected);

            assertThat(underTest.getArray()).containsExactly(expected, null);
        }

        @Test
        void when_add_several_and_empty_then_add() {
            ArrayList<Object> objects = new ArrayList<>();

            underTest = new GenericArrayStack<>(objects);

            Object expected = new Object();
            Object expected2 = new Object();
            underTest.push(expected);
            underTest.push(expected2);

            assertThat(underTest.getArray()).containsExactly(expected, expected2);
        }

        @Test
        void when_pop_and_empty_then_no_such_element_exception() {
            underTest = new GenericArrayStack<>();

            assertThatThrownBy(() -> underTest.pop())
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessage("Stack is empty");
        }

    }

}
