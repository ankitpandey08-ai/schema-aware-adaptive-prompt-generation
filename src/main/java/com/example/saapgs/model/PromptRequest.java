package com.example.saapgs.model;

import jakarta.validation.constraints.NotBlank;

public record PromptRequest(@NotBlank String templateName, @NotBlank String question) {}
