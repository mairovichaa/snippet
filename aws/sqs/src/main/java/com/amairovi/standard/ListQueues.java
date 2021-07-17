package com.amairovi.standard;

import software.amazon.awssdk.services.sqs.SqsClient;

import static com.amairovi.standard.Configs.REGION;

public class ListQueues {
    public static void main(String[] args) {
        SqsClient sqsClient = SqsClient.builder().region(REGION).build();

        sqsClient.listQueues().queueUrls()
                .forEach(System.out::println);
    }
}
