package com.example.saapgs.controller;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
public class HealthController {
    @GetMapping("/health")
    public Map<String,String> health() { return Map.of("status","UP","application","SAAPGS"); }
}
