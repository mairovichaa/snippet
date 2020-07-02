package com.amairovi.kafka.spring.consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.util.HashMap;
import java.util.Map;


@EnableKafka
public class Configuration {

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        String kafkaServers = "";
        props.put("bootstrap.servers", kafkaServers);
        props.put("kafka.topic", "event_store_stream");


        String username = "";
        props.put("kafka.user.name", username);

        String password = "";
        props.put("kafka.user.password", password);

        props.put("max.poll.records", 5);
        props.put("retries", 10);
        props.put("security.protocol", "SASL_SSL");
        props.put("sasl.mechanism", "PLAIN");
        props.put("ssl.protocol", "TLSv1.2");
        props.put("ssl.enabled.protocols", "TLSv1.2");
        props.put("ssl.endpoint.identification.algorithm", "HTTPS");
        props.put("auto.offset.reset", "earliest");
        props.put("session.timeout.ms", "20000");

        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        String saslString = "org.apache.kafka.common.security.plain.PlainLoginModule required serviceName=\"kafka\"" +
                "username=\"" + username + "\" password=\"" + password + "\";";

        props.put("sasl.jaas.config", saslString);
        return props;
    }
    @Bean
    public DefaultKafkaConsumerFactory consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory(ConsumerFactory<String, String> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConcurrency(1);
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}
