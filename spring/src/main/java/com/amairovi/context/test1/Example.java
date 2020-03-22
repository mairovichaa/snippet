package com.amairovi.context.test1;

import com.amairovi.context.test1.inner.ComponentClass1;
import com.amairovi.context.test1.inner.ComponentClass2;
import com.amairovi.context.test1.inner.ComponentClass3;
import com.amairovi.context.test1.inner.ComponentClass4;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class Example {

    public static void main(String[] args) {

        ApplicationContext context1 = new AnnotationConfigApplicationContext(
                ComponentClass1.class,

                // doesn't have any Spring annotations
                ComponentClass2.class
        );

        assertThat(context1.getBean(ComponentClass1.class)).isInstanceOf(ComponentClass1.class);
        assertThat(context1.getBean(ComponentClass2.class)).isInstanceOf(ComponentClass2.class);
        assertThatThrownBy(() -> context1.getBean(ComponentClass3.class)).isInstanceOf(NoSuchBeanDefinitionException.class);
        assertThatThrownBy(() -> context1.getBean(ComponentClass4.class)).isInstanceOf(NoSuchBeanDefinitionException.class);

        ApplicationContext context = new AnnotationConfigApplicationContext(Configuration.class);

        assertThatThrownBy(() -> context.getBean(ComponentClass1.class)).isInstanceOf(NoSuchBeanDefinitionException.class);
        assertThatThrownBy(() -> context.getBean(ComponentClass2.class)).isInstanceOf(NoSuchBeanDefinitionException.class);
        assertThat(context.getBean(ComponentClass3.class)).isInstanceOf(ComponentClass3.class);
        assertThat(context.getBean(ComponentClass4.class)).isInstanceOf(ComponentClass4.class);


        ApplicationContext context3 = new AnnotationConfigApplicationContext(ComponentClass1.class, com.amairovi.context.test1.inner_2.ComponentClass1.class);

        assertThat(context3.getBean(com.amairovi.context.test1.inner_2.ComponentClass1.class)).isInstanceOf(com.amairovi.context.test1.inner_2.ComponentClass1.class);
        assertThat(context3.getBean(ComponentClass1.class)).isInstanceOf(ComponentClass1.class);

        ApplicationContext context4 = new AnnotationConfigApplicationContext(com.amairovi.context.test1.inner_2.ComponentClass1.class, ComponentClass1.class);

        assertThat(context4.getBean(com.amairovi.context.test1.inner_2.ComponentClass1.class)).isInstanceOf(com.amairovi.context.test1.inner_2.ComponentClass1.class);
        assertThat(context4.getBean(ComponentClass1.class)).isInstanceOf(ComponentClass1.class);

    }

}
