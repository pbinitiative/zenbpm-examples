# Process 02: Commission Payout

A Czech commission payout approval process ("Výplata obchodní provize") that demonstrates AI-assisted pre-validation using OpenAI and user tasks for manual approval.

Adapted from the [zenbpm-workshop-workers](https://github.com/pbinitiative/zenbpm-workshop-workers).

## What You'll Learn

- How to use **user tasks** for human interaction (form filling, approval)
- How to integrate an **external AI service** (OpenAI) via the `openai-connector` worker
- How to use **exclusive gateways** for conditional branching
- How to compose **multiple workers** in a single process

## Process Flow

```
[Commission Request] --> [Fill Request Form] --> [AI Pre-Check] --> <AI OK?>
                              ^                                       |
                              |--- No (NOT_OK) -----------------------|
                              |
                              Yes (OK)
                              |
                              v
                        [Approval] --> [Send Payment] --> [Done]
```

The BPMN process is in Czech (it models a Czech business scenario):

| Czech Name | English | Type |
|---|---|---|
| Požadavek na vyplacení provize | Commission payout request | Start Event |
| Vyplnění žádosti | Fill request form | User Task |
| Provedení AI předkontroly | AI pre-check | Service Task (`openai-connector`) |
| AI kontrola OK? | AI check OK? | Exclusive Gateway |
| Schválení | Approval | User Task |
| Odeslání platby | Send payment | Service Task (`log-worker`) |
| Provize zpracována | Commission processed | End Event |

## Workers Used

| Worker | Job Type | Description |
|--------|----------|-------------|
| [log-worker](../../workers/log-worker/) | `log-worker` | Logs the payment amount (placeholder for actual payment) |
| [openai-connector](../../workers/openai-connector/) | `openai-connector` | Validates the payout via GPT-4o-mini |

## Run

1. Set your OpenAI API key:

```bash
export OPENAI_API_KEY=sk-your-key-here
```

2. Start everything (process is deployed automatically):

```bash
docker compose up -d
```

3. Start a process instance (use the `processDefinitionKey` from `docker compose logs deploy`):

```bash
curl -X POST http://localhost:8080/v1/process-instances \
  -H "Content-Type: application/json" \
  -d '{
    "processDefinitionKey": <KEY_FROM_DEPLOY>
  }'
```

4. Open the UI at http://localhost:9000 to interact with user tasks (fill the form, approve the payout).

## Test Data

See [test-data.md](test-data.md) for example inputs that trigger OK and NOT_OK decisions.

## Clean Up

```bash
docker compose down
```
