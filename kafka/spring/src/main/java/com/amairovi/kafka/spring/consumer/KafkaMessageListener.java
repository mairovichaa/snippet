package com.amairovi.kafka.spring.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class KafkaMessageListener {

    private AtomicInteger integer = new AtomicInteger();

    @KafkaListener(topics = "topic", groupId = "amairovi-test")
    public void listen(ConsumerRecord<String, String> record) {
        int number = integer.incrementAndGet();
        System.out.println(number + " : ");
        System.out.println("key: " + record.key());
        System.out.println("value: " + record.value());
        System.out.println();
    }


}
