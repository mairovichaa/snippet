package com.amairovi.kafka.producer.avro;

public class CustomerGenerator {

    private static int id = 1;

    static Customer getNext() {
        Customer customer = new Customer(id, "customer " + id);
        id++;
        return customer;
    }

}
