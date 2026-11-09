# Architecture and data flow

```text
REST API
  |-- template create/update --> PromptTemplateManager
  |                                |
  |                                +--> PromptTableMapper.scanAndMapPrompt
  |
  +-- compose-and-run --> SchemaIntrospectorService --> SchemaSnapshot
                           |
                           v
                  AdaptivePromptRefresher
                  - schema fingerprint
                  - versioned cache key
                  - invalidation on schema change
                           |
                           v
                  AdaptivePromptComposer
                  - mapped tables only
                  - columns/types/keys/descriptions
                           |
                           v
                     ModelClient
                    /          \
              Mock provider   AWS Bedrock
                           |
                           v
                PromptEvaluationService
```

The schema fingerprint is a canonical SHA-256 over catalog/schema, sorted table names, and sorted column metadata. It includes type, nullability, primary-key status and descriptions. A description edit therefore invalidates prompts even when the physical column name is unchanged. The cache key additionally includes template name/version and question.
