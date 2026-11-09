package com.example.saapgs.model;

import jakarta.validation.constraints.NotBlank;

public record FeedbackRequest(@NotBlank String promptId, int rating, String comments) {}
