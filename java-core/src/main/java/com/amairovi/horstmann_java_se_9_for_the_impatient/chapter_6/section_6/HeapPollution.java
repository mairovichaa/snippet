package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_6.section_6;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class HeapPollution {

    public static void main(String[] args) {
        ArrayList<Long> longs = new ArrayList<>();
        longs.add(0L);

        ArrayList objects = longs;
        @SuppressWarnings("unchecked")
        ArrayList<String> strings = (ArrayList<String>) objects;

        objects.add("string");
        // exception when get - not when add
        assertThatThrownBy(() -> strings.get(0))
                .isInstanceOf(ClassCastException.class)
                .hasMessage("java.lang.Long cannot be cast to java.lang.String");
        // is ok
        strings.get(1);


        // --------------------
        List<Long> longs2 = Collections.checkedList(new ArrayList<>(), Long.class);
        List objects2 = longs2;
        List<String> strings2 = (List<String>) objects2;

        // exception when insert wrong type
        assertThatThrownBy(() -> strings2.add("string"))
                .isInstanceOf(ClassCastException.class)
                .hasMessage("Attempt to insert class java.lang.String element into collection with element type class java.lang.Long");
    }

}
