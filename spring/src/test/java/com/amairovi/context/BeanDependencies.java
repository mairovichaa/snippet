package com.amairovi.context;

import com.amairovi.context.test1.circular.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class BeanDependencies {

    @Test
    void when_unresolvable_circular_dependency_then_exception() {
        assertThatThrownBy(() -> new AnnotationConfigApplicationContext(Class1.class, Class2.class))
                .isInstanceOf(UnsatisfiedDependencyException.class);
    }

    @Test
    void when_resolvable_with_setters_circular_dependency_and_autowired_in_place_then_success() {
        ApplicationContext context = new AnnotationConfigApplicationContext(Class3.class, Class4.class);

        Class4 c4 = context.getBean(Class4.class);

        Class3 c3 = context.getBean(Class3.class);

        assertThat(c4.getClass3()).isEqualTo(c3);
        assertThat(c3.getClass4()).isEqualTo(c4);
    }

    @Test
    void when_resolvable_with_setters_circular_dependency_and_autowired_not_in_place_then_nulls() {
        ApplicationContext context = new AnnotationConfigApplicationContext(Class5.class, Class6.class);

        Class5 c5 = context.getBean(Class5.class);

        Class6 c6 = context.getBean(Class6.class);

        assertThat(c5.getClass6()).isNull();
        assertThat(c6.getClass5()).isNull();
    }
}
