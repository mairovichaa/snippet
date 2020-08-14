package com.amairovi.kafka.spring.batch_consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class KafkaMessageListener {

    private AtomicInteger integer = new AtomicInteger();

    private long lastTime = Instant.now().toEpochMilli();

    @KafkaListener(topics = "topic_name", groupId = "amairovi-test")
    public void listen(@Payload List<String> messages) {
        int number = integer.incrementAndGet();
        System.out.println(number + " : ");
        long now = Instant.now().toEpochMilli();
        System.out.println("time : " + now);
        System.out.println("diff: " + (now - lastTime));
        lastTime = now;
        System.out.println("amount of records : " + messages.size());
    }


}
