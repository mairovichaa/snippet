package com.amairovi.kafka.spring.consumer;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(Configuration.class);
        context.register(KafkaMessageListener.class);

        context.refresh();
    }
}
