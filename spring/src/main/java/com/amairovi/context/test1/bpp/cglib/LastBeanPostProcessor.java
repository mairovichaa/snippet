package com.amairovi.context.test1.bpp.cglib;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.core.Ordered;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
public class LastBeanPostProcessor implements Ordered, BeanPostProcessor {

    private final Set<String> beanNamesForProcessing;

    // bean's name -> bean's class
    private final Map<String, Class> beanNameToClass = new HashMap<>();

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 1;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (beanNamesForProcessing.contains(beanName)) {
            beanNameToClass.put(beanName, bean.getClass());
        }

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (beanNamesForProcessing.contains(beanName)) {
            Enhancer enhancer = new Enhancer();

            Class beanClass = beanNameToClass.get(beanName);
            enhancer.setSuperclass(beanClass);

            enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
                if (bean instanceof Callable) {
                    ((Callable) bean).call("LastBeanPostProcessor " + method.getName());
                }

                return proxy.invoke(bean, args);
            });

            return enhancer.create();
        }

        return bean;
    }

}
