package com.amairovi.kafka.admin;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.TopicListing;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class ListTopicNames {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Properties config = new Properties();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        AdminClient admin = AdminClient.create(config);

        ListTopicsResult topics = admin.listTopics();

        for (TopicListing value : topics.listings().get()) {
            System.out.println(value);
        }

        admin.close();
    }

}
