package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_4.section_4_4_5;

import java.util.ServiceLoader;

public class ServiceLoaders {

    public static void main(String[] args) {
        ServiceLoader<CounterInterface> loader = ServiceLoader.load(CounterInterface.class);
        for (CounterInterface counter : loader) {
            System.out.println(counter.getClass().getName());
        }
    }
}
