package com.example.saapgs.model;

import java.util.*;

public record SchemaSnapshot(String catalog, String schema, List<TableMetadata> tables) {
    public SchemaSnapshot { tables = tables == null ? List.of() : tables.stream().sorted(Comparator.comparing(TableMetadata::name, String.CASE_INSENSITIVE_ORDER)).toList(); }
    public Optional<TableMetadata> table(String name) {
        return tables.stream().filter(t -> t.name().equalsIgnoreCase(name)).findFirst();
    }
}
