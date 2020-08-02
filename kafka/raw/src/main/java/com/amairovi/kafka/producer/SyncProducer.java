package com.amairovi.kafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class SyncProducer {

    private final static String TOPIC_NAME = "topic_example";

    public static void main(String[] args) {
        Properties properties = new Properties();

        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        String value = "value example";
        ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, value);

        try {
            RecordMetadata recordMetadata = producer.send(record).get();

            String topic = recordMetadata.topic();

            int partition = recordMetadata.partition();

            boolean hasOffset = recordMetadata.hasOffset();
            long offset = recordMetadata.offset();

            boolean hasTimestamp = recordMetadata.hasTimestamp();
            long timestamp = recordMetadata.timestamp();

            int serializedValueSize = recordMetadata.serializedValueSize();
            int serializedKeySize = recordMetadata.serializedKeySize();

            System.out.println(recordMetadata);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        producer.close();

    }

}
