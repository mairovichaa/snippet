package com.amairovi.kafka.producer.avro;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class ProducerGenerated {

    private final static String TOPIC_NAME = "topic_example";
    private final static String SCHEMA_URL = "http://localhost:8081";

    public static void main(String[] args) {
        Properties properties = new Properties();

        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.serializer", KafkaAvroSerializer.class.getName());
        properties.put("value.serializer", KafkaAvroSerializer.class.getName());
        properties.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, SCHEMA_URL);

        KafkaProducer<String, Customer> producer = new KafkaProducer<>(properties);

        Customer customer = CustomerGenerator.getNext();
        ProducerRecord<String, Customer> record = new ProducerRecord<>(TOPIC_NAME, customer);

        try {

            System.out.println("Generated " + customer);
            RecordMetadata recordMetadata = producer.send(record).get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        producer.close();

    }


}
