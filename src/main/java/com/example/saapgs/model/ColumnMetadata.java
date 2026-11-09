package com.example.saapgs.model;

public record ColumnMetadata(String name, String dataType, boolean nullable, boolean primaryKey, String description) {}
