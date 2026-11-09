package com.example.saapgs.model;

public record PromptResponse(String templateName, String prompt, String answer, String schemaFingerprint, boolean cacheHit) {}
