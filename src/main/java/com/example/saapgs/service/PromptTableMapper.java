package com.example.saapgs.service;

import org.springframework.stereotype.Component;
import java.util.*;
import java.util.regex.*;

@Component
public class PromptTableMapper {
    private static final Pattern SQL_OBJECT = Pattern.compile("(?i)\\b(?:from|join|update|into)\\s+([a-zA-Z_][\\w$]*(?:\\.[a-zA-Z_][\\w$]*)?)");
    private static final Pattern PLACEHOLDER = Pattern.compile("\\{\\{?table:([a-zA-Z_][\\w$]*)}?}");

    /** Scans a prompt template for table references and merges explicitly declared dependencies. */
    public Set<String> scanAndMapPrompt(String prompt, Set<String> explicitTables) {
        Set<String> result = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        if (explicitTables != null) result.addAll(explicitTables);
        if (prompt != null) {
            Matcher sql = SQL_OBJECT.matcher(prompt);
            while (sql.find()) result.add(lastPart(sql.group(1)));
            Matcher placeholders = PLACEHOLDER.matcher(prompt);
            while (placeholders.find()) result.add(placeholders.group(1));
        }
        return Collections.unmodifiableSet(result);
    }
    private String lastPart(String identifier) {
        int dot = identifier.lastIndexOf('.');
        return dot >= 0 ? identifier.substring(dot + 1) : identifier;
    }
}
