package com.amairovi.context.test1.bpp;

import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Getter
public class MethodCounterBeanPostProcessor implements BeanPostProcessor {

    // bean's name -> methods' names
    private final Map<String, List<String>> beanNameToMethodNames = new HashMap<>();

    // bean's name -> method's names -> amount of calls
    private final Map<String, Map<String, AtomicLong>> beanToCounter = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Method[] methods = bean.getClass().getMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(Count.class)) {
                String methodName = method.getName();

                List<String> currentListOfMethods = beanNameToMethodNames.getOrDefault(beanName, new ArrayList<>());
                currentListOfMethods.add(methodName);
                beanNameToMethodNames.put(beanName, currentListOfMethods);

                Map<String, AtomicLong> methodNameToAmountOfCalls = beanToCounter.getOrDefault(beanName, new HashMap<>());
                methodNameToAmountOfCalls.put(methodName, new AtomicLong());
                beanToCounter.put(beanName, methodNameToAmountOfCalls);
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (beanNameToMethodNames.containsKey(beanName)) {
            List<String> methodNames = beanNameToMethodNames.get(beanName);
            return Proxy.newProxyInstance(
                    bean.getClass().getClassLoader(),
                    bean.getClass().getInterfaces(),
                    (proxy, method, args) -> {
                        String methodName = method.getName();
                        if (methodNames.contains(methodName)) {
                            Map<String, AtomicLong> methodNameToAmountOfCalls = beanToCounter.get(beanName);
                            methodNameToAmountOfCalls.get(methodName).incrementAndGet();
                        }

                        return method.invoke(bean, args);
                    }
            );
        }

        return bean;
    }

}
