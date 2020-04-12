package com.amairovi.context;

import com.amairovi.context.test1.bpp.Count;
import com.amairovi.context.test1.bpp.MethodCounterBeanPostProcessor;
import com.amairovi.context.test1.bpp.cglib.Callable;
import com.amairovi.context.test1.bpp.cglib.LastBeanPostProcessor;
import com.amairovi.context.test1.bpp.cglib.MethodCounterCglibBeanPostProcessor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class BeanPostProcessorTest {

    static class Clazz1 implements InitializingBean {

        List<String> methodNames = new ArrayList<>();

        @Override
        public void afterPropertiesSet() {
            methodNames.add("afterPropertiesSet");
        }

        @PostConstruct
        public void postConstruct() {
            methodNames.add("postConstruct");
        }

        public void initFromConfig() {
            methodNames.add("initFromConfig");
        }

    }

    public static class Config1 {

        @Bean(initMethod = "initFromConfig")
        public Clazz1 clazz1() {
            return new Clazz1();
        }

        @Bean
        public BeanPostProcessor beanPostProcessor() {
            return new BeanPostProcessor() {
                @Override
                public Object postProcessBeforeInitialization(Object bean, String beanName) {
                    if (bean instanceof Clazz1) {
                        ((Clazz1) bean).methodNames.add("postProcessBeforeInitialization for " + beanName);
                    }
                    return bean;
                }

                @Override
                public Object postProcessAfterInitialization(Object bean, String beanName) {
                    if (bean instanceof Clazz1) {
                        ((Clazz1) bean).methodNames.add("postProcessAfterInitialization for " + beanName);
                    }
                    return bean;
                }
            };
        }

    }

    @Test
    void order_of_applying_bean_post_processor_methods_and_init_methods() {
        ApplicationContext context = new AnnotationConfigApplicationContext(Config1.class);

        Clazz1 bean = context.getBean(Clazz1.class);

        assertThat(bean.methodNames.get(0)).isEqualTo("postProcessBeforeInitialization for clazz1");
        assertThat(bean.methodNames.get(1)).isEqualTo("postConstruct");
        assertThat(bean.methodNames.get(2)).isEqualTo("afterPropertiesSet");
        assertThat(bean.methodNames.get(3)).isEqualTo("initFromConfig");
        assertThat(bean.methodNames.get(4)).isEqualTo("postProcessAfterInitialization for clazz1");
    }

    public interface Interface2 {

        boolean isPostProcessBeforeInitializationWasCalled();

        boolean isPostProcessAfterInitializationWasCalledByClass();

        boolean isPostProcessAfterInitializationWasCalledByBeanName();

        boolean isPostProcessAfterInitializationWasCalledByInterface();

        void setPostProcessAfterInitializationWasCalledByBeanName();

        void setPostProcessAfterInitializationWasCalledByInterface();

    }

    public static class Clazz2 implements Interface2 {

        boolean postProcessBeforeInitializationWasCalled;
        boolean postProcessAfterInitializationWasCalledByClass;
        boolean postProcessAfterInitializationWasCalledByBeanName;
        boolean postProcessAfterInitializationWasCalledByInterface;

        @Override
        public boolean isPostProcessBeforeInitializationWasCalled() {
            return postProcessBeforeInitializationWasCalled;
        }

        @Override
        public boolean isPostProcessAfterInitializationWasCalledByClass() {
            return postProcessAfterInitializationWasCalledByClass;
        }

        @Override
        public boolean isPostProcessAfterInitializationWasCalledByBeanName() {
            return postProcessAfterInitializationWasCalledByBeanName;
        }

        @Override
        public boolean isPostProcessAfterInitializationWasCalledByInterface() {
            return postProcessAfterInitializationWasCalledByInterface;
        }

        @Override
        public void setPostProcessAfterInitializationWasCalledByBeanName() {
            postProcessAfterInitializationWasCalledByBeanName = true;
        }

        @Override
        public void setPostProcessAfterInitializationWasCalledByInterface() {
            postProcessAfterInitializationWasCalledByInterface = true;
        }

    }

    public static class Config2 {

        @Bean
        public Clazz2 clazz2() {
            return new Clazz2();
        }

        @Bean
        public BeanPostProcessor beanPostProcessor() {
            return new BeanPostProcessor() {

                @Override
                public Object postProcessBeforeInitialization(Object bean, String beanName) {
                    if (bean instanceof Clazz2) {
                        ((Clazz2) bean).postProcessBeforeInitializationWasCalled = true;
                        return Proxy.newProxyInstance(
                                Clazz2.class.getClassLoader(),
                                Clazz2.class.getInterfaces(),
                                (proxy, method, args) -> method.invoke(bean, args)
                        );
                    }
                    return bean;
                }

                @Override
                public Object postProcessAfterInitialization(Object bean, String beanName) {
                    if (bean instanceof Clazz2) {
                        ((Clazz2) bean).postProcessAfterInitializationWasCalledByClass = true;
                    }
                    if (bean instanceof Interface2) {
                        ((Interface2) bean).setPostProcessAfterInitializationWasCalledByInterface();
                        if (beanName.equals("clazz2")) {
                            ((Interface2) bean).setPostProcessAfterInitializationWasCalledByBeanName();
                        }
                    }
                    return bean;
                }
            };
        }

    }

    @Test
    void when_proxy_is_returned_by_before_bpp_then_information_about_source_class_is_lost() {
        ApplicationContext context = new AnnotationConfigApplicationContext(Config2.class);

        assertThatThrownBy(
                () -> context.getBean(Clazz2.class),
                "postProcessBeforeInitialization created proxy, so information about source class was lost"
        )
                .isInstanceOf(NoSuchBeanDefinitionException.class)
                .hasMessage("No qualifying bean of type 'com.amairovi.context.BeanPostProcessorTest$Clazz2' available");

        Interface2 bean = context.getBean("clazz2", Interface2.class);

        assertThat(bean.isPostProcessBeforeInitializationWasCalled()).isTrue();
        assertThat(bean.isPostProcessAfterInitializationWasCalledByClass()).isFalse();
        assertThat(bean.isPostProcessAfterInitializationWasCalledByInterface()).isTrue();
        assertThat(bean.isPostProcessAfterInitializationWasCalledByBeanName()).isTrue();
    }


    public interface Interface3 {

        void methodToCount();

        void methodNotToCount();

    }

    public static class Clazz3 implements Interface3 {

        @Count
        @Override
        public void methodToCount() {
        }

        @Override
        public void methodNotToCount() {
        }

    }

    @Configuration
    public static class Config3 {

        @Bean
        public Clazz3 clazz3() {
            return new Clazz3();
        }

    }

    @RequiredArgsConstructor
    public static class Class32 {

        private final Clazz3 clazz3;

    }

    @Configuration
    public static class Config32 {

        @Bean
        public Clazz3 clazz3() {
            return new Clazz3();
        }

        @Bean
        public Class32 clazz32(Clazz3 clazz3) {
            return new Class32(clazz3);
        }

    }

    @Test
    void example_of_bpp_implemented_using_jdk_proxy() {
        ApplicationContext context =
                new AnnotationConfigApplicationContext(Config3.class, MethodCounterBeanPostProcessor.class);

        MethodCounterBeanPostProcessor bpp = context.getBean(MethodCounterBeanPostProcessor.class);
        List<String> namesOfMethodsToCount = bpp.getBeanNameToMethodNames().get("clazz3");

        assertThat(namesOfMethodsToCount).containsExactly("methodToCount");

        Interface3 bean = context.getBean(Interface3.class);
        bean.methodToCount();
        bean.methodToCount();
        bean.methodToCount();

        bean.methodNotToCount();
        bean.methodNotToCount();

        Map<String, AtomicLong> clazz3 = bpp.getBeanToCounter().get("clazz3");
        assertThat(clazz3).hasSize(1);
        AtomicLong methodToCount = clazz3.get("methodToCount");
        assertThat(methodToCount.intValue()).isEqualTo(3);

        Object clazz31 = context.getBean("clazz3");
        assertThat(clazz31).isNotInstanceOf(Clazz3.class);

        assertThatThrownBy(
                () -> new AnnotationConfigApplicationContext(Config32.class, MethodCounterBeanPostProcessor.class)
        ).isInstanceOf(UnsatisfiedDependencyException.class);
    }


    @Getter
    public static class Clazz4 implements Callable {

        private List<String> calls = new ArrayList<>();

        @Count
        public void methodToCount() {
            System.out.println(this.getClass());
        }

        public void methodNotToCount() {
        }

        public void call(String name) {
            if (name.endsWith("methodToCount") || name.endsWith("methodNotToCount")) {
                calls.add(name);
            }
        }

    }

    @RequiredArgsConstructor
    @Getter
    public static class Clazz42 {

        private final Clazz4 clazz4;

    }

    @Configuration
    public static class Config4 {

        @Bean
        public Clazz4 clazz4() {
            return new Clazz4();
        }


        @Bean
        public Clazz42 clazz42() {
            return new Clazz42(clazz4());
        }

        @Bean
        public LastBeanPostProcessor lastBeanPostProcessor() {
            HashSet<String> beanNames = new HashSet<>();
            beanNames.add("clazz4");
            return new LastBeanPostProcessor(beanNames);
        }

    }

    @Test
    void example_of_bpp_implemented_using_cglib() {
        ApplicationContext context =
                new AnnotationConfigApplicationContext(Config4.class, MethodCounterCglibBeanPostProcessor.class);
        Clazz4 bean = context.getBean(Clazz4.class);

        bean.methodToCount();
        bean.methodNotToCount();

        assertThat(bean.getCalls())
                .containsExactly(
                        "LastBeanPostProcessor methodToCount",
                        "MethodCounterCglibBeanPostProcessor methodToCount",
                        "LastBeanPostProcessor methodNotToCount",
                        "MethodCounterCglibBeanPostProcessor methodNotToCount"
                );


        MethodCounterCglibBeanPostProcessor bpp = context.getBean(MethodCounterCglibBeanPostProcessor.class);
        List<String> namesOfMethodsToCount = bpp.getBeanNameToMethodNames().get("clazz4");

        assertThat(namesOfMethodsToCount).containsExactly("methodToCount");

        Map<String, AtomicLong> clazz4 = bpp.getBeanToCounter().get("clazz4");
        assertThat(clazz4).hasSize(1);
        AtomicLong methodToCount = clazz4.get("methodToCount");
        assertThat(methodToCount.intValue()).isEqualTo(1);

        Clazz42 clazz42 = context.getBean(Clazz42.class);
        assertThat(clazz42.getClazz4()).isSameAs(bean);
    }

}
