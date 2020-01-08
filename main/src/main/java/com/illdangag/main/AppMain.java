package com.illdangag.main;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.forcs.eformbot.common.log.LogMarker;
import com.forcs.eformbot.common.log.policy.CustomSizeAndTimeBasedRollingPolicy;
import com.forcs.eformbot.common.log.policy.ICustomRolloverEventListener;
import com.forcs.eformbot.common.log.policy.ICustomShutdownEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class AppMain {
    public static final Logger logger = LoggerFactory.getLogger("");

    public static void main(String[] args) throws InterruptedException {
        final String ACCESS_KEY = "ybchoi";
        final String SECRET_KEY = "dudqja12!";
        final String SERVICE_ENDPOINT = "http://localhost:9000";
        final String BUCKET_NAME = "s3-log";

        AWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setSignerOverride("AWSS3V4SignerType");
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(SERVICE_ENDPOINT, Regions.US_EAST_1.name()))
                .withPathStyleAccessEnabled(true)
                .withClientConfiguration(clientConfiguration)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();

        ICustomRolloverEventListener rolloverEventListener = new ICustomRolloverEventListener() {
            @Override
            public void rollover(final String filePath) {
                System.out.println("======== rollover - " + filePath);

                new Thread(() -> {
                    StringBuilder s3ObjectPathBuilder = new StringBuilder(filePath);
                    if (s3ObjectPathBuilder.indexOf("/") == 0) {
                        s3ObjectPathBuilder.delete(0, 1);
                    }

                    File rollFile = new File(filePath);
                    if (rollFile.exists()) {
                        s3Client.putObject(BUCKET_NAME, s3ObjectPathBuilder.toString(), rollFile);
                    }
                }).start();
            }
        };
        ICustomShutdownEventListener shutDownEventListener = new ICustomShutdownEventListener() {
            @Override
            public void shutdown(CustomSizeAndTimeBasedRollingPolicy policy) {
                System.out.println("======== shutdown - " + policy);

                String activeFilePath = policy.getActiveFileName();
                String rollFilePath = policy.getTimeBasedFileNamingAndTriggeringPolicy().getCurrentPeriodsFileNameWithoutCompressionSuffix();
                StringBuilder s3ObjectPathBuilder = new StringBuilder(rollFilePath);
                if (s3ObjectPathBuilder.indexOf("/") == 0) {
                    s3ObjectPathBuilder.delete(0, 1);
                }
                s3ObjectPathBuilder.insert(s3ObjectPathBuilder.lastIndexOf("."), "-shutdown");
                System.out.println(s3ObjectPathBuilder.toString());
                File rollFile = new File(activeFilePath);
                if (rollFile.exists()) {
                    s3Client.putObject(BUCKET_NAME, s3ObjectPathBuilder.toString(), rollFile);
                }
            }
        };

        CustomSizeAndTimeBasedRollingPolicy.setRolloverEventListener(rolloverEventListener);
        CustomSizeAndTimeBasedRollingPolicy.setShutdownEventListener(shutDownEventListener);

        for (int i = 0; i < 30; i++) {
            Thread.sleep(200);
            logger.info(LogMarker.SYSTEM, "system log");
            logger.info(LogMarker.USER, "user log");
        }
    }
}
