package com.amairovi.context;

import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AutowiredTest {

    @Component
    @Getter
    public static class Clazz11 {

        private boolean prepareCalled = false;

        @Autowired
        public void prepare(Clazz12 clazz12) {
            prepareCalled = true;
            System.out.println(clazz12);
        }

    }

    @Component
    private static class Clazz12 {

    }

    @Test
    void when_autowired_on_method_then_use_to_set_dependencies() {
        ApplicationContext context =
                new AnnotationConfigApplicationContext(Clazz11.class, Clazz12.class);

        Clazz11 bean = context.getBean(Clazz11.class);

        assertThat(bean.isPrepareCalled()).isTrue();
    }


    @Component
    @Getter
    public static class Clazz21 {

        private boolean prepareCalled = false;
        private final List<String> order = new ArrayList<>();

        private Clazz23 clazz23;

        private final Clazz24 clazz24;

        public Clazz21(Clazz24 clazz24) {
            this.clazz24 = clazz24;
            order.add("constructor");
        }

        @Autowired
        public void prepare(Clazz22 clazz22) {
            prepareCalled = true;
            order.add("method");
        }

        @Autowired
        public void setClazz23(Clazz23 clazz23) {
            order.add("setter");
            this.clazz23 = clazz23;
        }

    }

    @Component
    private static class Clazz22 {

    }

    @Component
    private static class Clazz23 {

    }

    @Component
    private static class Clazz24 {

    }

    @Test
    void when_combination_of_different_methods_then_apply_all_of_them() {
        ApplicationContext context =
                new AnnotationConfigApplicationContext(Clazz21.class, Clazz22.class, Clazz23.class, Clazz24.class);

        Clazz21 bean = context.getBean(Clazz21.class);

        assertThat(bean.isPrepareCalled()).isTrue();
        assertThat(bean.getClazz23()).isNotNull();
        assertThat(bean.getClazz24()).isNotNull();
        assertThat(bean.getOrder()).containsExactly("constructor", "method", "setter");
    }


}
