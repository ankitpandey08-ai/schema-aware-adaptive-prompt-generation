package com.example.saapgs.model;

import jakarta.validation.constraints.NotBlank;
import java.util.Set;

public record TemplateRequest(@NotBlank String name, @NotBlank String template, String description, Set<String> tableNames) {}
