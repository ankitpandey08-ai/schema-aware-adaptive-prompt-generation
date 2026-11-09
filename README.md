# SAAPGS — Schema-Aware Adaptive Prompt Generation System

A runnable Java 17 / Spring Boot reference implementation of the patent concept discussed in the SAAPGS thread. It discovers relational schema metadata, maps prompt templates to the tables they depend on, detects schema changes, composes schema-grounded prompts, and invokes either a deterministic mock model or AWS Bedrock.

## What is included
- JDBC schema discovery: tables, columns, SQL types, nullability, primary keys and remarks.
- Optional JPA entity metadata adapter (works with supplied entity metadata; JDBC remains authoritative for live DB state).
- Prompt template CRUD-like in-memory management and explicit table dependency mapping.
- `PromptTableMapper.scanAndMapPrompt(...)`, called after template create/update and during refresh, to associate templates with referenced tables.
- Schema fingerprinting and adaptive prompt refresh. Cache keys include schema identity, template version and question so edits cannot return stale prompt text.
- Mock model provider by default; AWS Bedrock Converse API adapter is selectable by configuration.
- REST endpoints, feedback capture, lightweight response evaluation, sample H2 schema and tests.

## Run
Requires JDK 17+ and Maven 3.9+.

```bash
mvn test
mvn spring-boot:run
```

The app starts with an H2 in-memory sample database and mock model provider. Try:

```bash
curl -X POST http://localhost:8080/api/v1/prompts/templates \
 -H 'Content-Type: application/json' \
 -d '{"name":"orders-summary","template":"Answer using {tables}. Focus on {question}.","description":"Summarize order status","tableNames":["orders"]}'

curl -X POST http://localhost:8080/api/v1/prompts/compose-and-run \
 -H 'Content-Type: application/json' \
 -d '{"templateName":"orders-summary","question":"How many orders are pending?"}'
```

## AWS Bedrock
Set `saapgs.model.provider=bedrock`, `AWS_REGION`, and `saapgs.bedrock.model-id` (or `SAAPGS_BEDROCK_MODEL_ID`). Credentials are resolved using the AWS SDK default credential chain. The adapter uses the Bedrock Runtime Converse API. No credentials are stored in this repository.

## Configuration
See `src/main/resources/application.yml`. For a real database, configure `spring.datasource.*`; the included H2 datasource is only a runnable demo.

## Schema change flow
1. `SchemaIntrospectorService` reads the current catalog and produces a normalized `SchemaSnapshot`.
2. `PromptTableMapper.scanAndMapPrompt` scans table identifiers in a template and merges explicit dependencies.
3. `AdaptivePromptRefresher.refreshIfSchemaChanged` compares deterministic fingerprints and invalidates affected derived prompts.
4. `AdaptivePromptComposer` injects only mapped/relevant table and column metadata into the template.
5. `ModelClient` invokes mock or Bedrock and returns a normalized response.

## Important scope note
This is a reconstructed, complete reference project based on the recoverable SAAPGS discussion and project design. The original full ZIP could not be recovered from the available prior-file library; this archive therefore does not claim to be byte-for-byte identical to that missing artifact. The implementation includes all major modules and integrations identified in the thread, plus tests and documentation.
