package com.example.saapgs.service;

import com.example.saapgs.model.*;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class PromptApplicationService {
    private final PromptTemplateManager templates;
    private final SchemaIntrospectorService introspector;
    private final AdaptivePromptRefresher refresher;
    private final ModelClient modelClient;
    private final PromptEvaluationService evaluation;
    public PromptApplicationService(PromptTemplateManager templates, SchemaIntrospectorService introspector,
        AdaptivePromptRefresher refresher, ModelClient modelClient, PromptEvaluationService evaluation) {
        this.templates=templates; this.introspector=introspector; this.refresher=refresher; this.modelClient=modelClient; this.evaluation=evaluation;
    }
    public PromptResponse composeAndRun(PromptRequest request) {
        PromptTemplate template = templates.find(request.templateName()).orElseThrow(() -> new NoSuchElementException("Unknown template: " + request.templateName()));
        SchemaSnapshot snapshot = introspector.introspect(null);
        String fingerprint = SchemaFingerprint.of(snapshot);
        int before = refresher.cacheSize();
        String prompt = refresher.refreshIfSchemaChanged(template, request.question(), snapshot);
        boolean hit = refresher.cacheSize() == before;
        String answer = modelClient.generate(prompt);
        evaluation.evaluate(prompt, answer);
        return new PromptResponse(template.name(), prompt, answer, fingerprint, hit);
    }
}
