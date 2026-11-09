# API examples

Create a template:

```bash
curl -X POST localhost:8080/api/v1/prompts/templates -H 'Content-Type: application/json' \
  -d '{"name":"orders-summary","template":"Use {tables}. Answer {question}.","description":"Orders reporting","tableNames":["orders"]}'
```

Compose and invoke:

```bash
curl -X POST localhost:8080/api/v1/prompts/compose-and-run -H 'Content-Type: application/json' \
  -d '{"templateName":"orders-summary","question":"Which orders are pending?"}'
```

Submit feedback:

```bash
curl -X POST localhost:8080/api/v1/feedback -H 'Content-Type: application/json' \
  -d '{"promptId":"orders-summary:1","rating":5,"comments":"Relevant schema context"}'
```
