package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_6;

import com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_6.Task1.Stack;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class Task1Test {

    private Stack<Object> underTest;


    @Test
    void when_created_then_empty() {
        underTest = new Stack<>();

        assertThat(underTest.isEmpty()).isTrue();
    }


    @Test
    void when_created_with_non_empty_then_not_empty() {
        underTest = new Stack<>(singletonList(new Object()));

        assertThat(underTest.isEmpty()).isFalse();
    }

    @Test
    void when_add_and_empty_then_add() {
        ArrayList<Object> objects = new ArrayList<>();

        underTest = new Stack<>(objects);

        Object expected = new Object();
        underTest.push(expected);

        assertThat(objects).containsExactly(expected);
    }

    @Test
    void when_add_several_and_empty_then_add() {
        ArrayList<Object> objects = new ArrayList<>();

        underTest = new Stack<>(objects);

        Object expected = new Object();
        Object expected2 = new Object();
        underTest.push(expected);
        underTest.push(expected2);

        assertThat(objects).containsExactly(expected, expected2);
    }

    @Test
    void when_pop_and_empty_then_no_such_element_exception() {
        underTest = new Stack<>();

        assertThatThrownBy(() -> underTest.pop())
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("Stack is empty");
    }



}
