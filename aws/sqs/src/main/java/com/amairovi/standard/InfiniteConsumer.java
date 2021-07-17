package com.amairovi.standard;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

import java.time.Instant;

import static com.amairovi.standard.Configs.REGION;

public class InfiniteConsumer {

    private final String queueUrl = Configs.QUEUE_URL;

    private final SqsClient sqsClient = SqsClient.builder()
            .credentialsProvider(Configs.INSTANCE)
            .region(REGION).build();

    public static void main(String[] args) {
        new InfiniteConsumer().infiniteConsume();
    }

    public void infiniteConsume() {
        while (true) {
            consume();
            System.out.println("---------------------------------------------");

        }
    }

    public void consume() {
        ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .maxNumberOfMessages(10)
                .waitTimeSeconds(10)
                .build();
        long start = System.currentTimeMillis();
        ReceiveMessageResponse receiveMessageResponse = sqsClient.receiveMessage(receiveMessageRequest);
        long end = System.currentTimeMillis();
        System.out.println(Instant.now());
        System.out.println(String.format("Amount of messages: %d.", receiveMessageResponse.messages().size()));
        System.out.println(receiveMessageResponse);
        System.out.println("It took " + (end - start) + "ms.");
    }
}
