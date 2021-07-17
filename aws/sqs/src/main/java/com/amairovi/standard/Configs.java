package com.amairovi.standard;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;

public class Configs implements AwsCredentialsProvider {

    public static Configs INSTANCE = new Configs();

    public static String QUEUE_URL = "queue url";

    static Region REGION = Region.EU_CENTRAL_1;

    @Override
    public AwsCredentials resolveCredentials() {
        return AwsBasicCredentials.create("access key id", "secret access key");
    }
}
