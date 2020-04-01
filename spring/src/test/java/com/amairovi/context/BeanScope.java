package com.amairovi.context;

import com.amairovi.context.test1.scope.BeanWithCustomScope;
import com.amairovi.context.test1.scope.BeanWithSingletonDependency;
import com.amairovi.context.test1.scope.CustomScope;
import com.amairovi.context.test1.scope.Dependency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static com.amairovi.context.test1.scope.CustomScope.CUSTOM_SCOPE_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BeanScope {

    private CustomScope scope;

    @BeforeEach
    void setup() {
        scope = new CustomScope();
    }

    @Test
    void when_bean_has_unknown_scope_then_exception_during_retrieval_of_the_bean() {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext();
        context.register(BeanWithCustomScope.class);
        context.refresh();

        assertThrows(IllegalStateException.class,
                () -> context.getBean(BeanWithCustomScope.class));
    }

    @Test
    void when_get_bean_then_counter_increased() {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext();
        context.getBeanFactory()
                .registerScope(CUSTOM_SCOPE_NAME, scope);
        context.register(BeanWithCustomScope.class);
        context.refresh();

        assertThat(scope.getCounter()).isEqualTo(0);

        context.getBean(BeanWithCustomScope.class);
        assertThat(scope.getCounter()).isEqualTo(1);

        context.getBean(BeanWithCustomScope.class);
        assertThat(scope.getCounter()).isEqualTo(2);
    }

    @Test
    void when_get_bean_and_remove_counter_then_reset_it() {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext();
        context.getBeanFactory()
                .registerScope(CUSTOM_SCOPE_NAME, scope);
        context.register(BeanWithCustomScope.class);
        context.refresh();

        assertThat(scope.getCounter()).isEqualTo(0);

        context.getBean(BeanWithCustomScope.class);
        assertThat(scope.getCounter()).isEqualTo(1);

        scope.remove("counter");
        assertThat(scope.getCounter()).isEqualTo(0);

        context.getBean(BeanWithCustomScope.class);
        assertThat(scope.getCounter()).isEqualTo(1);
    }


    @Test
    void when_get_bean_several_time_and_it_is_no_memorized_then_each_bean_instance_is_new() {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext();
        context.getBeanFactory()
                .registerScope(CUSTOM_SCOPE_NAME, scope);
        context.register(BeanWithCustomScope.class);
        context.refresh();

        assertThat(scope.getCounter()).isEqualTo(0);

        BeanWithCustomScope bean = context.getBean(BeanWithCustomScope.class);
        BeanWithCustomScope bean2 = context.getBean(BeanWithCustomScope.class);
        assertThat(bean).isNotSameAs(bean2);

        BeanWithCustomScope bean3 = context.getBean(BeanWithCustomScope.class);
        assertThat(bean).isNotSameAs(bean3);
        assertThat(bean2).isNotSameAs(bean3);
    }

    @Test
    void when_bean_has_a_dependency_on_singleton_then_autowire_it() {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext();
        context.getBeanFactory()
                .registerScope(CUSTOM_SCOPE_NAME, scope);
        context.register(BeanWithSingletonDependency.class);
        context.register(Dependency.class);
        context.refresh();

        BeanWithSingletonDependency bean1 = context.getBean(BeanWithSingletonDependency.class);
        BeanWithSingletonDependency bean2 = context.getBean(BeanWithSingletonDependency.class);

        assertThat(bean1).isNotSameAs(bean2);
        assertThat(bean1.getDependency()).isSameAs(bean2.getDependency());
    }
}
