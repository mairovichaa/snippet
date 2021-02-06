package com.amairovi.standard;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

public class ListQueues {
    public static void main(String[] args) {
        SqsClient sqsClient = SqsClient.builder().region(Region.EU_CENTRAL_1).build();

        sqsClient.listQueues().queueUrls()
                .forEach(System.out::println);
    }
}
