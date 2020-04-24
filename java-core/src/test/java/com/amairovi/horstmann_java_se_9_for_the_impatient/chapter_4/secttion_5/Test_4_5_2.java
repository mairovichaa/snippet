package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_4.secttion_5;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class Test_4_5_2 {


    static class Class1 {

        private String field;
    }

    @Test
    void when_get_field_then_return_new_instance_each_time() throws NoSuchFieldException {
        Field field1 = Class1.class.getDeclaredField("field");
        Field field2 = Class1.class.getDeclaredField("field");

        assertThat(field1).isNotSameAs(field2);
        field1.setAccessible(true);
        assertThat(field2.isAccessible()).isFalse();
    }


}
