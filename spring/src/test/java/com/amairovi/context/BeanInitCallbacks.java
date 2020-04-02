package com.amairovi.context;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BeanInitCallbacks {

    public static class Clazz {
        boolean initIsCalled = false;

        @PostConstruct
        void init() {
            initIsCalled = true;
        }
    }

    @Test
    void when_post_construct_is_presented_then_call_it() {
        ApplicationContext context = new AnnotationConfigApplicationContext(Clazz.class);

        Clazz bean = context.getBean(Clazz.class);

        assertThat(bean.initIsCalled).isTrue();
    }

    static class Clazz2 implements InitializingBean {
        boolean initIsCalled = false;

        @Override
        public void afterPropertiesSet() {
            initIsCalled = true;
        }
    }

    @Test
    void when_InitializingBean_then_call_afterPropertiesSet() {
        ApplicationContext context = new AnnotationConfigApplicationContext(Clazz2.class);

        Clazz2 bean = context.getBean(Clazz2.class);

        assertThat(bean.initIsCalled).isTrue();
    }

    static class Clazz3 {
        boolean initIsCalled = false;

        void init() {
            initIsCalled = true;
        }
    }

    public static class Config3 {
        @Bean(initMethod = "init")
        public Clazz3 clazz3() {
            return new Clazz3();
        }
    }

    @Test
    void when_config_with_init_method_then_call_it() {
        ApplicationContext context = new AnnotationConfigApplicationContext(Config3.class);

        Clazz3 bean = context.getBean(Clazz3.class);

        assertThat(bean.initIsCalled).isTrue();
    }

    static class Clazz4 implements InitializingBean{
        int amountOfCalls = 0;

        @PostConstruct
        @Override
        public void afterPropertiesSet() {
            amountOfCalls++;
        }
    }

    public static class Config4 {
        @Bean(initMethod = "afterPropertiesSet")
        public Clazz4 clazz4() {
            return new Clazz4();
        }
    }

    @Test
    void when_several_init_approaches_but_single_init_method_then_call_it_only_once() {
        ApplicationContext context = new AnnotationConfigApplicationContext(Config4.class);

        Clazz4 bean = context.getBean(Clazz4.class);

        assertThat(bean.amountOfCalls).isEqualTo(1);
    }

    static class Clazz5 implements InitializingBean{
        List<String> methodNames = new ArrayList<>();

        @Override
        public void afterPropertiesSet() {
            methodNames.add("afterPropertiesSet");
        }

        @PostConstruct
        public void postConstruct(){
            methodNames.add("postConstruct");
        }

        public void initFromConfig(){
            methodNames.add("initFromConfig");
        }
    }

    public static class Config5 {
        @Bean(initMethod = "initFromConfig")
        public Clazz5 clazz5() {
            return new Clazz5();
        }
    }

    @Test
    void when_several_init_methods_then_call_them_all() {
        ApplicationContext context = new AnnotationConfigApplicationContext(Config5.class);

        Clazz5 bean = context.getBean(Clazz5.class);

        assertThat(bean.methodNames.get(0)).isEqualTo("postConstruct");
        assertThat(bean.methodNames.get(1)).isEqualTo("afterPropertiesSet");
        assertThat(bean.methodNames.get(2)).isEqualTo("initFromConfig");
    }

    static class Clazz61 {
        boolean class61InitIsCalled = false;

        @PostConstruct
        public void init(){
            class61InitIsCalled = true;
        }
    }

    static class Clazz62 extends Clazz61 {
        boolean class62InitIsCalled = false;

        @PostConstruct
        public void init2(){
            class62InitIsCalled = true;
        }
    }

    @Test
    void when_parent_and_child_both_has_different_post_construct_then_call_them() {
        ApplicationContext context = new AnnotationConfigApplicationContext(Clazz62.class);

        Clazz62 bean = context.getBean(Clazz62.class);

        assertThat(bean.class61InitIsCalled).isTrue();
        assertThat(bean.class62InitIsCalled).isTrue();
    }

    static class Clazz71 {
        int initCalled = 0;

        @PostConstruct
        public void init(){
            initCalled++;
        }
    }

    static class Clazz72 extends Clazz71 {

        @PostConstruct
        @Override
        public void init(){
            initCalled++;
        }
    }

    @Test
    void when_child_overrides_parent_post_construct_then_parent_post_construct_is_not_called() {
        ApplicationContext context = new AnnotationConfigApplicationContext(Clazz72.class);

        Clazz72 bean = context.getBean(Clazz72.class);

        assertThat(bean.initCalled).isEqualTo(1);
    }
}
