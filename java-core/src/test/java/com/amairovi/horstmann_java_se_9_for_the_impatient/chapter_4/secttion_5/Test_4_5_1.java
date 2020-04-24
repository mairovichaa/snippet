package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_4.secttion_5;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class Test_4_5_1 {

    static class Class1Parent {

        public void parentPublicMethod() {
        }

        protected void parentProtectedMethod() {
        }

        void parentPackageMethod() {
        }

        private void parentPrivateMethod() {
        }

        protected void parentProtectedMethodForOverriding() {
        }

    }

    static class Class1 extends Class1Parent {

        public void publicMethod() {
        }

        protected void protectedMethod() {
        }

        void packageMethod() {
        }

        private void privateMethod() {
        }

        @Override
        protected void parentProtectedMethodForOverriding() {
        }
    }

    @Test
    void when_get_methods_then_return_all_public_methods() {
        Method[] methods = Class1.class.getMethods();
        Modifier.toString(2);
        List<String> methodNames = Arrays.stream(methods)
                .map(Method::getName)
                .collect(Collectors.toList());
        assertThat(methodNames).containsExactly(
                "publicMethod",
                "parentPublicMethod",
                "wait",
                "wait",
                "wait",
                "equals",
                "toString",
                "hashCode",
                "getClass",
                "notify",
                "notifyAll");
    }

    @Test
    void when_get_methods_then_return_all_declared_in_the_class() {
        Method[] methods = Class1.class.getDeclaredMethods();

        List<String> methodNames = Arrays.stream(methods)
                .map(Method::getName)
                .collect(Collectors.toList());
        assertThat(methodNames).containsExactly(
                "publicMethod", "privateMethod",
                "protectedMethod", "packageMethod",
                "parentProtectedMethodForOverriding"
        );
    }

}
