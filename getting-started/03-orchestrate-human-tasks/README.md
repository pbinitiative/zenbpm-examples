# 03 · Orchestrate human tasks

Some steps wait for a **person**, not a worker. This chapter runs a one-step approval process and drives the human task over REST — no worker to write, because *you* are the one completing it.

```
[Request received] --> (Approve request) --> [Done]
                            user task
```

A **user task** surfaces at the engine as a job of type `user-task`. You list it, assign it, and complete it with a decision. In a real system your application (or the UI) makes these same calls on a person's behalf.

## Files

| Path | What it is |
|---|---|
| `approval.bpmn` | The process definition you deploy |

There is no `java/` or `go/` folder here: a user task has no worker. The Java and Go clients *can* drive user tasks (see the docs), but the core flow is the REST calls below.

## Prerequisites

- The engine running — from the [`getting-started/`](../) folder run `docker compose up -d` (see [01-run-the-engine](../01-run-the-engine/))
- `curl`

## 1. Deploy the process (manual)

```bash
curl -X POST http://localhost:8080/v1/process-definitions \
  -F "resource=@approval.bpmn"
```

Note the returned `processDefinitionKey`.

## 2. Start an instance

```bash
curl -X POST http://localhost:8080/v1/process-instances \
  -H "Content-Type: application/json" \
  -d '{"processDefinitionKey": <PROCESS_DEFINITION_KEY>, "variables": {}}'
```

The instance starts and **parks at the user task** — it will wait for a person indefinitely.

## 3. Find the pending task

```bash
curl "http://localhost:8080/v1/jobs?jobType=user-task&state=active"
```

Note the job `key`.

## 4. Assign and complete it

```bash
curl -X POST http://localhost:8080/v1/jobs/<JOB_KEY>/assign \
  -H "Content-Type: application/json" \
  -d '{"assignee": "john.doe"}'

curl -X POST http://localhost:8080/v1/jobs/<JOB_KEY>/complete \
  -H "Content-Type: application/json" \
  -d '{"variables": {"approved": true}}'
```

The token advances to the end event and the instance completes. Completing with `{"approved": false}` is also a valid decision — "reject" is still a `complete`, not a `fail`.

## Clean up

Stop the engine from the [`getting-started/`](../) folder with `docker compose down`.
