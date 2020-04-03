package com.amairovi.context;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.context.Lifecycle;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

public class BeanLifecycle {

    private static class C1 implements Lifecycle {

        private boolean started = false;

        @Override
        public void start() {
            started = true;
        }

        @Override
        public void stop() {
            started = false;
        }

        @Override
        public boolean isRunning() {
            return started;
        }
    }

    @Test
    void when_context_starts_or_stops_then_call_according_methods_on_lifecycle_bean() {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(C1.class);
        C1 bean = context.getBean(C1.class);

        assertThat(bean.isRunning()).isFalse();

        context.start();
        assertThat(bean.isRunning()).isTrue();

        context.stop();
        assertThat(bean.isRunning()).isFalse();
    }

    @RequiredArgsConstructor
    @Getter
    private static class C21 implements Lifecycle {

        private final C22 c22;

        boolean dependencyIsSetBeforeStart;

        @Override
        public void start() {
            dependencyIsSetBeforeStart = c22 != null;
            c22.flag = true;
        }

        @Override
        public void stop() {
        }

        @Override
        public boolean isRunning() {
            return false;
        }
    }

    private static class C22 {
        boolean flag = false;
    }

    @Test
    void when_lifecycle_bean_has_dependency_then_it_is_set_before_start() {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(C21.class, C22.class);
        C21 bean = context.getBean(C21.class);

        context.start();
        assertThat(bean.dependencyIsSetBeforeStart).isTrue();

        C22 bean2 = context.getBean(C22.class);

        assertThat(bean2.flag).isTrue();
    }
}
