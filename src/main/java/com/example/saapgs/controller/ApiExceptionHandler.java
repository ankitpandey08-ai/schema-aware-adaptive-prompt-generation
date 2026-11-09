package com.example.saapgs.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(NoSuchElementException.class)
    ResponseEntity<Map<String,String>> notFound(Exception e) { return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage())); }
    @ExceptionHandler({IllegalArgumentException.class, jakarta.validation.ConstraintViolationException.class})
    ResponseEntity<Map<String,String>> badRequest(Exception e) { return ResponseEntity.badRequest().body(Map.of("error", e.getMessage())); }
    @ExceptionHandler(Exception.class)
    ResponseEntity<Map<String,String>> internal(Exception e) { return ResponseEntity.internalServerError().body(Map.of("error", "Request failed", "detail", Objects.toString(e.getMessage(), "unknown"))); }
}
