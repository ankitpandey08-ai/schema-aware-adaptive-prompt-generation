package com.example.saapgs.service;

import com.example.saapgs.model.*;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdaptivePromptComposer {
    public String compose(PromptTemplate template, String question, SchemaSnapshot snapshot) {
        Set<String> requested = template.tableNames();
        List<TableMetadata> relevant = snapshot.tables().stream()
            .filter(t -> requested.isEmpty() || requested.stream().anyMatch(n -> n.equalsIgnoreCase(t.name())))
            .toList();
        String schemaContext = relevant.stream().map(this::formatTable).collect(Collectors.joining("\\n"));
        return template.template().replace("{tables}", schemaContext)
            .replace("{{tables}}", schemaContext)
            .replace("{question}", question).replace("{{question}}", question)
            + "\\n\\nUse only the schema context above. If the requested information cannot be derived, say so explicitly.";
    }
    private String formatTable(TableMetadata t) {
        String cols = t.columns().stream().map(c -> c.name() + " " + c.dataType()
            + (c.primaryKey() ? " PRIMARY KEY" : "") + (c.nullable() ? " NULL" : " NOT NULL")
            + (c.description() == null || c.description().isBlank() ? "" : " -- " + c.description()))
            .collect(Collectors.joining(", "));
        return "TABLE " + t.name() + " (" + cols + ")";
    }
}
