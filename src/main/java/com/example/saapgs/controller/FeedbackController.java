package com.example.saapgs.controller;

import com.example.saapgs.model.FeedbackRequest;
import com.example.saapgs.service.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/feedback")
public class FeedbackController {
    private final FeedbackService service;
    public FeedbackController(FeedbackService service) { this.service=service; }
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public FeedbackService.Feedback submit(@Valid @RequestBody FeedbackRequest request) { return service.submit(request); }
    @GetMapping public Object all() { return service.all(); }
}
