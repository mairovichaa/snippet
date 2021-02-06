package com.amairovi.standard;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import java.util.Random;

public class Publisher {

    private final String queueUrl = "";
    private final SqsClient sqsClient = SqsClient.builder()
            .region(Region.EU_CENTRAL_1)
            .credentialsProvider(
                    () -> AwsBasicCredentials.create("accessKeyId", "secretAccessKey"))
            .build();

    public static void main(String[] args) {
        new Publisher().send();

    }

    public void send() {
        long sum = 0;
        int amountOfMessages = 10;
        for (int i = 0; i < amountOfMessages; i++) {
            SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                    .messageBody("Message #" + i + "." + generateRandomString(50 * 1024))
                    .queueUrl(queueUrl)
                    .build();
            long start = System.currentTimeMillis();
            SendMessageResponse sendMessageResponse = sqsClient.sendMessage(sendMessageRequest);
            long end = System.currentTimeMillis();
//            System.out.println(sendMessageResponse);
            final long tookTime = end - start;
            System.out.println("It took " + tookTime + "ms. " + sendMessageResponse.messageId());
            sum += tookTime;
        }

        System.out.println("Average: " + (sum / amountOfMessages));

    }

    private String generateRandomString(int size) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = size;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
