package com.example.saapgs.model;

import java.util.List;

public record TableMetadata(String name, String description, List<ColumnMetadata> columns) {
    public TableMetadata { columns = columns == null ? List.of() : List.copyOf(columns); }
}
