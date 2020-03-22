package com.amairovi.context;

import com.amairovi.context.test1.lookup.Class1;
import com.amairovi.context.test1.lookup.Class2;
import com.amairovi.context.test1.lookup.Class2Counter;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

class BeanLookup {

    @Test
    void when_look_up_and_scope_prototype_then_create_new_each_time() {
        ApplicationContext context = new AnnotationConfigApplicationContext(
                Class1.class, Class2.class, Class2Counter.class);

        Class1 c1 = context.getBean(Class1.class);
        assertThat(c1).isInstanceOf(Class1.class);

        Class2Counter class2Counter = context.getBean(Class2Counter.class);
        assertThat(class2Counter).isInstanceOf(Class2Counter.class);
        assertThat(class2Counter.amountOfClass2Instances).isEqualTo(0);

        Class2 c21 = c1.process();
        assertThat(c21).isNotNull();
        assertThat(c21).isInstanceOf(Class2.class);
        assertThat(class2Counter.amountOfClass2Instances).isEqualTo(1);

        Class2 c22 = c1.process();
        assertThat(c22).isNotNull();
        assertThat(c22).isInstanceOf(Class2.class);
        assertThat(class2Counter.amountOfClass2Instances).isEqualTo(2);

        Class2 c23 = c1.process();
        assertThat(c23).isNotNull();
        assertThat(c23).isInstanceOf(Class2.class);
        assertThat(class2Counter.amountOfClass2Instances).isEqualTo(3);
    }


}
