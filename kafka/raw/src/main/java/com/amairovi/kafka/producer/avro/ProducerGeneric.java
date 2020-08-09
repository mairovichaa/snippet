package com.amairovi.kafka.producer.avro;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class ProducerGeneric {

    private final static String TOPIC_NAME = "persons";
    private final static String SCHEMA_URL = "http://localhost:8081";

    private final static int AMOUNT_OF_PERSONS = 10;

    public static void main(String[] args) {
        Properties properties = new Properties();

        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.serializer", KafkaAvroSerializer.class.getName());
        properties.put("value.serializer", KafkaAvroSerializer.class.getName());
        properties.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, SCHEMA_URL);

        KafkaProducer<String, GenericRecord> producer = new KafkaProducer<>(properties);

        Schema.Parser parser = new Schema.Parser();

        String schemaString = "{" +
                "  \"namespace\": \"com.amairovi.kafka.producer.avro\"," +
                "  \"type\": \"record\"," +
                "  \"name\": \"Person\"," +
                "  \"fields\": [" +
                "    {" +
                "      \"name\": \"firstName\"," +
                "      \"type\": \"string\"" +
                "    }," +
                "    {" +
                "      \"name\": \"birthDate\"," +
                "      \"type\": \"long\"" +
                "    }" +
                "  ]" +
                "}";

        Schema schema = parser.parse(schemaString);


        for (int i = 0; i < AMOUNT_OF_PERSONS; i++) {
            String firstName = "name " + i;
            long birth = System.currentTimeMillis();

            GenericRecord person = new GenericData.Record(schema);
            person.put("firstName", firstName);
            person.put("birthDate", birth);

            ProducerRecord<String, GenericRecord> record = new ProducerRecord<>(TOPIC_NAME, person);

            try {
                System.out.println("Generated " + person);
                RecordMetadata recordMetadata = producer.send(record).get();

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }


        producer.close();

    }


}
