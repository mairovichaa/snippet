package com.amairovi.context.test1.bpp.cglib;

import com.amairovi.context.test1.bpp.Count;
import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.core.Ordered;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Getter
public class MethodCounterCglibBeanPostProcessor implements BeanPostProcessor, Ordered {

    // bean's name -> methods' names
    private final Map<String, List<String>> beanNameToMethodNames = new HashMap<>();

    // bean's name -> bean's class
    private final Map<String, Class> beanNameToClass = new HashMap<>();

    // bean's name -> method's names -> amount of calls
    private final Map<String, Map<String, AtomicLong>> beanToCounter = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        Method[] methods = beanClass.getMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(Count.class)) {
                String methodName = method.getName();

                List<String> currentListOfMethods = beanNameToMethodNames.getOrDefault(beanName, new ArrayList<>());
                currentListOfMethods.add(methodName);
                beanNameToMethodNames.put(beanName, currentListOfMethods);

                Map<String, AtomicLong> methodNameToAmountOfCalls = beanToCounter.getOrDefault(beanName, new HashMap<>());
                methodNameToAmountOfCalls.put(methodName, new AtomicLong());
                beanToCounter.put(beanName, methodNameToAmountOfCalls);

                beanNameToClass.putIfAbsent(beanName, beanClass);
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (beanNameToMethodNames.containsKey(beanName)) {

            Enhancer enhancer = new Enhancer();
            Class beanClass = beanNameToClass.get(beanName);
            enhancer.setSuperclass(beanClass);

            List<String> methodNames = beanNameToMethodNames.get(beanName);

            enhancer.setCallback((MethodInterceptor)
                    (obj, method, args, proxy) -> {
                        String methodName = method.getName();
                        if (methodNames.contains(methodName)) {
                            Map<String, AtomicLong> methodNameToAmountOfCalls = beanToCounter.get(beanName);
                            methodNameToAmountOfCalls.get(methodName).incrementAndGet();
                        }


                        if (bean instanceof Callable) {
                            ((Callable) bean).call("MethodCounterCglibBeanPostProcessor " + method.getName());
                        }

                        return proxy.invoke(bean, args);
                    });

            return enhancer.create();
        }

        return bean;
    }

    @Override
    public int getOrder() {
        return 0;
    }

}
