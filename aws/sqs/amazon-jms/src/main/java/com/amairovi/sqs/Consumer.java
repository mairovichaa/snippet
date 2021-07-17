package com.amairovi.sqs;

import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.handlers.RequestHandler2;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.ChangeMessageVisibilityBatchRequest;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import static com.amairovi.sqs.Configs.*;

public class Consumer {

    private static final BasicAWSCredentials credentials =
            new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);

    private static final AmazonSQS sqsClient = AmazonSQSClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion(REGION)
            .withRequestHandlers(new RequestHandler2() {
                @Override
                public AmazonWebServiceRequest beforeExecution(AmazonWebServiceRequest request) {
                    if (request instanceof ChangeMessageVisibilityBatchRequest) {
                        ((ChangeMessageVisibilityBatchRequest) request).getEntries().forEach(changeMessageVisibilityBatchRequestEntry -> {
                            changeMessageVisibilityBatchRequestEntry.setVisibilityTimeout(15);
                        });
                        System.out.println("Changed visibility timeout for request " + request);
                    }
                    return request;
                }
            })
            .build();

    public static void main(String[] args) throws JMSException {
        SQSConnectionFactory connectionFactory = new SQSConnectionFactory(
                new ProviderConfiguration(),
                sqsClient
        );
        SQSConnection connection = connectionFactory.createConnection();
        var session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue("test-queue");
        var consumer = session.createConsumer(queue);
        connection.start();
        var message = (TextMessage) consumer.receive();
        System.out.println("Send message " + message.getJMSMessageID());
        System.out.println("Text" + message.getText());
        System.out.println("-----------------------------------");
        connection.close();
    }
}
