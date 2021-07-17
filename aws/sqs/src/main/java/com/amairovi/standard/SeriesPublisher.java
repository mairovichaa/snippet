package com.amairovi.standard;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import java.util.Random;

import static com.amairovi.standard.Configs.QUEUE_URL;
import static com.amairovi.standard.Configs.REGION;

public class SeriesPublisher {

    private final String queueUrl = QUEUE_URL;
    private final SqsClient sqsClient = SqsClient.builder()
            .region(REGION)
            .credentialsProvider(Configs.INSTANCE)
            .build();

    public static void main(String[] args) {
        new SeriesPublisher().send();

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
