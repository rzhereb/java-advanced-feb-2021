package com.oktenweb.decjavaadv.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@RequiredArgsConstructor
public class AwsClientConfig {

  private final AwsProperties awsProperties;

  @Bean
  public S3Client s3Client() {
    // якщо не передали змінні середовища у операційну систему, тоді треба вручну передати ключі при створенні клієнта
//    final AwsCredentialsProvider awsCredentialsProvider =
//        StaticCredentialsProvider.create(
//            AwsBasicCredentials.create(awsProperties.getAccessKey(), awsProperties.getSecretKey()));
    return S3Client.builder().region(Region.EU_WEST_1).build();
  }

  @Bean
  public S3AsyncClient s3AsyncClient() {
    // якщо не передали змінні середовища у операційну систему, тоді треба вручну передати ключі при створенні клієнта
//    final AwsCredentialsProvider awsCredentialsProvider =
//        StaticCredentialsProvider.create(
//            AwsBasicCredentials.create(awsProperties.getAccessKey(), awsProperties.getSecretKey()));
    return S3AsyncClient.builder().region(Region.EU_WEST_1).build();
  }

}
