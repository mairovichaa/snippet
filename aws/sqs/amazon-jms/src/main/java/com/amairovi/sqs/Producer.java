package com.amairovi.sqs;

import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import static com.amairovi.sqs.Configs.*;

public class Producer {

    private static final BasicAWSCredentials credentials =
            new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
    private static final AmazonSQS sqsClient = AmazonSQSClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion(REGION)
            .build();

    public static void main(String[] args) throws JMSException {
        SQSConnectionFactory connectionFactory = new SQSConnectionFactory(
                new ProviderConfiguration(),
                sqsClient
        );
        SQSConnection connection = connectionFactory.createConnection();
        var session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue("test-queue");
        var producer = session.createProducer(queue);
        TextMessage message = session.createTextMessage("Hello world!");
        producer.send(message);
        System.out.println("Send message " + message.getJMSMessageID());
        connection.close();
    }
}
