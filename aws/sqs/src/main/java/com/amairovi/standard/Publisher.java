package com.amairovi.standard;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.amairovi.standard.Configs.REGION;

public class Publisher {

    private final String queueUrl = Configs.QUEUE_URL;
    private final SqsClient sqsClient = SqsClient.builder()
            .region(REGION)
            .credentialsProvider(Configs.INSTANCE)
            .build();

    public static void main(String[] args) {
        new Publisher().send();
    }

    public void send() {
        Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
        MessageAttributeValue messageAttributeValue = MessageAttributeValue.builder()
                .dataType("String")
                .stringValue("cause error").build();
        messageAttributes.put("JMS_SQSMessageType", messageAttributeValue);

        SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                .messageBody("Message #1." + generateRandomString(1 * 1024))
                .queueUrl(queueUrl)
                .messageAttributes(messageAttributes)
                .build();
        long start = System.currentTimeMillis();
        SendMessageResponse sendMessageResponse = sqsClient.sendMessage(sendMessageRequest);
        long end = System.currentTimeMillis();
        final long tookTime = end - start;
        System.out.println("It took " + tookTime + "ms. " + sendMessageResponse.messageId());
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
