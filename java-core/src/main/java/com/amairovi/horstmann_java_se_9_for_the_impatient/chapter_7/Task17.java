package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_7;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class Task17 {

    public static void main(String[] args) {
        ArrayList<Integer> integers = new ArrayList<>();

        assertThatCode(() -> pollute(integers)).doesNotThrowAnyException();
        assertThatThrownBy(() -> integers.get(0))
                .isInstanceOf(ClassCastException.class);

        List<Integer> integersChecked = Collections.checkedList(integers, Integer.class);

        assertThatThrownBy(() -> pollute(integersChecked))
                .isInstanceOf(ClassCastException.class)
                .hasMessage("Attempt to insert class java.lang.Object element into collection with element type class java.lang.Integer");


    }

    private static void pollute(List list) {
        list.add(new Object());
    }

}
