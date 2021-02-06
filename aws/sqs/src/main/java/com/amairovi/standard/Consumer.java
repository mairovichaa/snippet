package com.amairovi.standard;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

public class Consumer {

    private final String queueUrl = "https://sqs.eu-central-1.amazonaws.com/263515182719/test-queue";
    private final SqsClient sqsClient = SqsClient.builder().region(Region.EU_CENTRAL_1).build();

    public static void main(String[] args) {
        new Consumer().send();
    }

    public void send() {
        ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .maxNumberOfMessages(10)
                .waitTimeSeconds(10)
                .build();
        long start = System.currentTimeMillis();
        ReceiveMessageResponse receiveMessageResponse = sqsClient.receiveMessage(receiveMessageRequest);
        long end = System.currentTimeMillis();
        System.out.println(String.format("Amount of messages: %d.", receiveMessageResponse.messages().size()));
        System.out.println(receiveMessageResponse);
        System.out.println("It took " + (end - start) + "ms.");
    }
}
