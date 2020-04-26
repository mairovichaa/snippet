package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_4.secttion_5;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class Task10Test {

    @Test
    void array_type_has_clone_method_but_it_is_not_possible_to_get_it() {
        int[] source = new int[]{2, 3, 4};
        int[] clone = source.clone();

        assertThat(clone).isNotSameAs(source);
        assertThat(clone).containsExactly(source);

        Class<?> clazz = int[].class;

        assertThatThrownBy(() -> clazz.getMethod("clone"))
                .isInstanceOf(NoSuchMethodException.class)
                .hasMessage("[I.clone()");
    }

}
