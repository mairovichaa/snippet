package com.amairovi.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;
import java.util.stream.Collectors;

public class Consumer {

    private final static String TOPIC_NAME = "topic_example";

    public static void main(String[] args) {
        Properties properties = new Properties();

        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("group.id", "amairovi");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        consumer.subscribe(Collections.singletonList(TOPIC_NAME));

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(2000));
                System.out.println(String.format("amount of records %d", records.count()));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println("------");
                    System.out.println("topic = " + record.topic());
                    System.out.println("partition = " + record.partition());
                    System.out.println("offset = " + record.offset());
                    System.out.println("headers = " + Arrays.stream((record.headers().toArray())).map(h -> h.key() + ": " + new String(h.value())).collect(Collectors.joining(",", "{ ", " }")));
                    System.out.println("key = " + record.key());
                    System.out.println("value = " + record.value());
                }
                System.out.println();
            }
        } finally {
            consumer.close();
        }

    }

}
