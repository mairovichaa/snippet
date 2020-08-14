package com.amairovi.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Properties;

public class Simple {

    public static void main(String[] args) {
        Properties properties = new Properties();

        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("group.id", "amairovi-test");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("fetch.min.bytes", 100 * 1024 * 1024);
        properties.put("fetch.max.wait.ms", 10 * 1000);
        properties.put("max.poll.records", 15 * 1000);


        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList("topic_name"));

        Instant prevTime = Instant.now();
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));

            System.out.println("amount of records: " + records.count());

            if (prevTime != null) {
                Instant curTime = Instant.now();
                System.out.println("time diff: " + (curTime.toEpochMilli() - prevTime.toEpochMilli()));

            }
//            for (ConsumerRecord<String, String> record : records) {
//                System.out.println(String.format("%s , value size %d", record.value(), record.serializedValueSize()));
//            }


            prevTime = records.isEmpty() ? null : Instant.now();
        }

    }

}
