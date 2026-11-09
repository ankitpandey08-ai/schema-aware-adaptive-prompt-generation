package com.example.saapgs.service;

import com.example.saapgs.model.*;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AdaptivePromptRefresher {
    private final Map<String, String> promptCache = new ConcurrentHashMap<>();
    private volatile String lastFingerprint;
    private final PromptTableMapper mapper;
    public AdaptivePromptRefresher(PromptTableMapper mapper) { this.mapper = mapper; }

    public String refreshIfSchemaChanged(PromptTemplate template, String question, SchemaSnapshot snapshot) {
        // Re-map on refresh too: templates may have been imported or changed outside the manager.
        Set<String> dependencies = mapper.scanAndMapPrompt(template.template(), template.tableNames());
        PromptTemplate effective = new PromptTemplate(template.name(), template.template(), template.description(),
            dependencies, template.version(), template.updatedAt());
        String fingerprint = SchemaFingerprint.of(snapshot);
        if (!fingerprint.equals(lastFingerprint)) {
            synchronized (this) {
                if (!fingerprint.equals(lastFingerprint)) {
                    promptCache.clear();
                    lastFingerprint = fingerprint;
                }
            }
        }
        String key = template.name() + "|" + template.version() + "|" + fingerprint + "|" + question;
        return promptCache.computeIfAbsent(key, ignored -> new AdaptivePromptComposer().compose(effective, question, snapshot));
    }
    public void clear() { promptCache.clear(); lastFingerprint = null; }
    public int cacheSize() { return promptCache.size(); }
    public String currentFingerprint() { return lastFingerprint; }
}
