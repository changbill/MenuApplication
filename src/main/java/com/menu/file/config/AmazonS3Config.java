package com.menu.file.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AmazonS3Config {
    private final AwsCredentialsProperties awsCredentialsProperties;

    @Bean
    public AmazonS3 amazonS3Client() {
        String accessKey = awsCredentialsProperties.getCredentials().getAccessKey();
        String secretKey = awsCredentialsProperties.getCredentials().getSecretKey();
        String region = awsCredentialsProperties.getRegion().getRegionStatic();

        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .build();
    }
}
