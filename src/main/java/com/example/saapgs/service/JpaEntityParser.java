package com.example.saapgs.service;

import com.example.saapgs.model.*;
import org.springframework.stereotype.Component;
import java.util.*;

/** Optional adapter for applications that want to enrich JDBC metadata with known JPA entity mappings. */
@Component
public class JpaEntityParser {
    public SchemaSnapshot fromEntityMetadata(String schema, Map<String, Map<String, String>> entityColumns) {
        List<TableMetadata> tables = new ArrayList<>();
        if (entityColumns != null) entityColumns.forEach((table, cols) -> {
            List<ColumnMetadata> columns = cols.entrySet().stream()
                .map(e -> new ColumnMetadata(e.getKey(), e.getValue(), true, false, "JPA mapped attribute")).toList();
            tables.add(new TableMetadata(table, "JPA entity mapping", columns));
        });
        return new SchemaSnapshot(null, schema, tables);
    }
}
