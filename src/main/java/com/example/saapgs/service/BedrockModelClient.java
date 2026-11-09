package com.example.saapgs.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.document.Document;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.*;

import java.util.*;

@Component
@ConditionalOnProperty(name="saapgs.model.provider", havingValue="bedrock")
public class BedrockModelClient implements ModelClient {
    private final BedrockRuntimeClient client;
    private final String modelId;
    public BedrockModelClient(BedrockRuntimeClient client,
            @Value("${saapgs.bedrock.model-id}") String modelId) {
        this.client = client; this.modelId = modelId;
    }
    @Override public String generate(String prompt) {
        Message message = Message.builder().role(ConversationRole.USER)
            .content(ContentBlock.fromText(prompt)).build();
        ConverseResponse response = client.converse(ConverseRequest.builder().modelId(modelId)
            .messages(message).inferenceConfig(InferenceConfiguration.builder().maxTokens(1200).temperature(0.2f).build()).build());
        return response.output().message().content().stream().map(ContentBlock::text)
            .filter(Objects::nonNull).reduce("", String::concat);
    }
}
