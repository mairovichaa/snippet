package com.amairovi.kafka.producer;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;

public class AsyncProducer {

    private final static String TOPIC_NAME = "topic_example";
    private final static String VALUE = "async value example";

    public static void main(String[] args) {
        Properties properties = new Properties();

        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, VALUE);
        producer.send(record, new CallbackExample());
        producer.close();
    }

    private static class CallbackExample implements Callback {

        @Override
        public void onCompletion(RecordMetadata metadata, Exception exception) {
            if (exception != null) {
                exception.printStackTrace();
            } else {
                System.out.println("Message was successfully sent.");
            }
        }

    }


}
