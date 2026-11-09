package com.example.saapgs.service;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class PromptEvaluationService {
    public Map<String, Object> evaluate(String prompt, String answer) {
        boolean hasSchemaContext = prompt != null && prompt.contains("TABLE ");
        boolean hasAnswer = answer != null && !answer.isBlank();
        return Map.of("hasSchemaContext", hasSchemaContext, "hasAnswer", hasAnswer,
            "promptLength", prompt == null ? 0 : prompt.length(),
            "answerLength", answer == null ? 0 : answer.length(),
            "passed", hasSchemaContext && hasAnswer);
    }
}
