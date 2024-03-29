package com.amairovi.standard;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

import static com.amairovi.standard.Configs.QUEUE_URL;
import static com.amairovi.standard.Configs.REGION;

public class Consumer {

    private final SqsClient sqsClient = SqsClient.builder().region(REGION).build();

    public static void main(String[] args) {
        new Consumer().consume();
    }

    public void consume() {
        ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(QUEUE_URL)
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
