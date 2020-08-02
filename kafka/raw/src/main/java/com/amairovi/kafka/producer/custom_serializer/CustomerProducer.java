package com.amairovi.kafka.producer.custom_serializer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class CustomerProducer {

    private final static String TOPIC_NAME = "topic_example";

    public static void main(String[] args) {
        Properties properties = new Properties();

        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "com.amairovi.kafka.producer.custom_serializer.CustomerSerializer");

        KafkaProducer<String, Customer> producer = new KafkaProducer<>(properties);

        Customer value = new Customer(3000, "Andrei");
        ProducerRecord<String, Customer> record = new ProducerRecord<>(TOPIC_NAME, value);

        try {
            RecordMetadata recordMetadata = producer.send(record).get();
            System.out.println(recordMetadata);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        producer.close();

    }
}
