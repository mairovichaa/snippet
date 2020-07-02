package com.amairovi.ibm.cos;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import com.ibm.cloud.objectstorage.ClientConfiguration;
import com.ibm.cloud.objectstorage.auth.AWSCredentials;
import com.ibm.cloud.objectstorage.auth.AWSStaticCredentialsProvider;
import com.ibm.cloud.objectstorage.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3ClientBuilder;
import com.ibm.cloud.objectstorage.services.s3.model.*;
import com.ibm.cloud.objectstorage.oauth.BasicIBMOAuthCredentials;

public class Main {

    public static void main(String[] args) {
        String bucketName = "";
        String apiKey = "";
        String serviceInstanceId = "";
        String endpointUrl = "";

        String location = "";

        System.out.println("Current time: " + LocalDateTime.now());
        AmazonS3 cosClient = createClient(apiKey, serviceInstanceId, endpointUrl, location);

        createTextFile(bucketName, "file-1", "Hello world", cosClient);
        listObjects(cosClient, bucketName);
        listBuckets(cosClient);
    }

    public static AmazonS3 createClient(String apiKey, String serviceInstanceId, String endpointUrl, String location) {
        AWSCredentials credentials = new BasicIBMOAuthCredentials(apiKey, serviceInstanceId);
        ClientConfiguration clientConfig = new ClientConfiguration()
                .withRequestTimeout(5000)
                .withTcpKeepAlive(true);

        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(new EndpointConfiguration(endpointUrl, location))
                .withPathStyleAccessEnabled(true)
                .withClientConfiguration(clientConfig)
                .build();
    }

    public static void listObjects(AmazonS3 cosClient, String bucketName) {
        System.out.println("Listing objects in bucket " + bucketName);
        ObjectListing objectListing = cosClient.listObjects(new ListObjectsRequest().withBucketName(bucketName));
        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            System.out.println(" - " + objectSummary.getKey() + "  " + "(size = " + objectSummary.getSize() + ")");
        }
        System.out.println();
    }


    public static void listBuckets(AmazonS3 cosClient) {
        System.out.println("Listing buckets");
        final List<Bucket> bucketList = cosClient.listBuckets();
        for (final Bucket bucket : bucketList) {
            System.out.println(bucket.getName());
        }
        System.out.println();
    }

    public static void createTextFile(String bucketName, String itemName, String fileText, AmazonS3 client) {
        System.out.printf("Creating new item: %s\n", itemName);

        InputStream newStream = new ByteArrayInputStream(fileText.getBytes(StandardCharsets.UTF_8));

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(fileText.length());

        PutObjectRequest req = new PutObjectRequest(bucketName, itemName, newStream, metadata);
        client.putObject(req);

        S3Object object = client.getObject("sad", "asd");
        System.out.printf("Item: %s created!\n", itemName);
    }

}
