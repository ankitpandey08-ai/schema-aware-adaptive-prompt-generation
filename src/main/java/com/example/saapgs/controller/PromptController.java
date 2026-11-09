package com.example.saapgs.controller;

import com.example.saapgs.model.*;
import com.example.saapgs.service.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/v1/prompts")
public class PromptController {
    private final PromptTemplateManager templates;
    private final PromptApplicationService application;
    public PromptController(PromptTemplateManager templates, PromptApplicationService application) { this.templates=templates; this.application=application; }

    @PostMapping("/templates")
    @ResponseStatus(HttpStatus.CREATED)
    public PromptTemplate save(@Valid @RequestBody TemplateRequest request) {
        return templates.save(request.name(), request.template(), request.description(), request.tableNames());
    }
    @GetMapping("/templates")
    public List<PromptTemplate> list() { return templates.findAll(); }
    @DeleteMapping("/templates/{name}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String name) { templates.delete(name); }
    @PostMapping("/compose-and-run")
    public PromptResponse compose(@Valid @RequestBody PromptRequest request) { return application.composeAndRun(request); }
}
