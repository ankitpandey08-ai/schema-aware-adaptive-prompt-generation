package com.example.saapgs.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class BedrockConfig {
    @Bean
    @ConditionalOnProperty(name="saapgs.model.provider", havingValue="bedrock")
    BedrockRuntimeClient bedrockRuntimeClient(@Value("${aws.region:us-east-1}") String region) {
        return BedrockRuntimeClient.builder().region(Region.of(region)).build();
    }
}
