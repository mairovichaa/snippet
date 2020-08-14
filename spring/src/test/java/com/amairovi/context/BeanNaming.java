package com.amairovi.context;

import com.amairovi.context.test1.inner.ComponentClass1;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class BeanNaming {

    @Test
    void when_beans_have_the_same_simple_class_name_then_the_latter_overrides() {
        ApplicationContext context3 = new AnnotationConfigApplicationContext(ComponentClass1.class, com.amairovi.context.test1.inner_2.ComponentClass1.class);

        assertThat(context3.getBean(com.amairovi.context.test1.inner_2.ComponentClass1.class)).isInstanceOf(com.amairovi.context.test1.inner_2.ComponentClass1.class);
        assertThat(context3.getBean("componentClass1")).isInstanceOf(com.amairovi.context.test1.inner_2.ComponentClass1.class);
        assertThatThrownBy(() -> context3.getBean(ComponentClass1.class)).isInstanceOf(NoSuchBeanDefinitionException.class);

        ApplicationContext context4 = new AnnotationConfigApplicationContext(com.amairovi.context.test1.inner_2.ComponentClass1.class, ComponentClass1.class);

        assertThat(context4.getBean(ComponentClass1.class)).isInstanceOf(ComponentClass1.class);
        assertThat(context4.getBean("componentClass1")).isInstanceOf(ComponentClass1.class);
        assertThatThrownBy(() -> context4.getBean(com.amairovi.context.test1.inner_2.ComponentClass1.class)).isInstanceOf(NoSuchBeanDefinitionException.class);
    }

    @Component("another-name")
    private static class Clazz2 {

    }

    @Test
    void when_rename_bean_through_component_annotation() {
        ApplicationContext context = new AnnotationConfigApplicationContext(Clazz2.class);

        assertThat(context.getBean("another-name")).isInstanceOf(Clazz2.class);
        assertThatThrownBy(() -> context.getBean("clazz2")).isInstanceOf(NoSuchBeanDefinitionException.class);
    }

}
