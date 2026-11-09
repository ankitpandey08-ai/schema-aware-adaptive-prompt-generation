package com.example.saapgs.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name="saapgs.model.provider", havingValue="mock", matchIfMissing=true)
public class MockModelClient implements ModelClient {
    @Override public String generate(String prompt) {
        return "MOCK RESPONSE: Prompt received (" + prompt.length() + " characters). Configure AWS Bedrock for a model-generated answer.";
    }
}
