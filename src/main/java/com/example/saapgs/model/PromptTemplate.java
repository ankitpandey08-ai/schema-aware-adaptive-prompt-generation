package com.example.saapgs.model;

import java.time.Instant;
import java.util.*;

public record PromptTemplate(String name, String template, String description, Set<String> tableNames,
                             long version, Instant updatedAt) {
    public PromptTemplate {
        tableNames = tableNames == null ? Set.of() : Collections.unmodifiableSet(new TreeSet<>(String.CASE_INSENSITIVE_ORDER) {{ addAll(tableNames); }});
    }
}
