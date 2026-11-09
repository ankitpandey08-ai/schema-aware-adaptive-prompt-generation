package com.example.saapgs.service;

import com.example.saapgs.model.PromptTemplate;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PromptTemplateManager {
    private final Map<String, PromptTemplate> templates = new ConcurrentHashMap<>();
    private final AtomicLong versions = new AtomicLong();
    private final PromptTableMapper mapper;
    public PromptTemplateManager(PromptTableMapper mapper) { this.mapper = mapper; }

    public PromptTemplate save(String name, String template, String description, Set<String> explicitTables) {
        Set<String> mapped = mapper.scanAndMapPrompt(template, explicitTables);
        PromptTemplate saved = new PromptTemplate(name, template, description, mapped, versions.incrementAndGet(), Instant.now());
        templates.put(name.toLowerCase(Locale.ROOT), saved);
        return saved;
    }
    public Optional<PromptTemplate> find(String name) { return Optional.ofNullable(templates.get(name.toLowerCase(Locale.ROOT))); }
    public List<PromptTemplate> findAll() { return templates.values().stream().sorted(Comparator.comparing(PromptTemplate::name)).toList(); }
    public void delete(String name) { templates.remove(name.toLowerCase(Locale.ROOT)); }
}
