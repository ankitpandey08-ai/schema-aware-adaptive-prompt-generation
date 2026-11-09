package com.example.saapgs.service;

import com.example.saapgs.model.FeedbackRequest;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class FeedbackService {
    public record Feedback(FeedbackRequest request, Instant submittedAt) {}
    private final List<Feedback> feedback = new CopyOnWriteArrayList<>();
    public Feedback submit(FeedbackRequest request) {
        if (request.rating() < 1 || request.rating() > 5) throw new IllegalArgumentException("rating must be between 1 and 5");
        Feedback item = new Feedback(request, Instant.now()); feedback.add(item); return item;
    }
    public List<Feedback> all() { return List.copyOf(feedback); }
}
