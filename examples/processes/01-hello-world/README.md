# Process 01: Hello World

The simplest possible ZenBPM example. A BPMN process with one service task handled by the `log-worker`.

## What You'll Learn

- How to deploy a BPMN process to ZenBPM
- How to start a process instance with variables
- How a service task delegates work to an external worker via gRPC

## Process

```
[Start] --> [Log Greeting] --> [Done]
                  |
             log-worker
```

The service task creates a job of type `log-worker`. The worker picks it up, reads the `log` variable, and prints it.

## Run

```bash
docker compose up -d
```

The process is deployed automatically. Check the deploy logs:

```bash
docker compose logs deploy
```

## Start a Process Instance

Use the `processDefinitionKey` from the deploy logs:

```bash
curl -X POST http://localhost:8080/v1/process-instances \
  -H "Content-Type: application/json" \
  -d '{
    "processDefinitionKey": <KEY_FROM_DEPLOY>,
    "variables": {"log": "Hello, ZenBPM!"}
  }'
```

The worker will log:

```
[log-worker] Hello, ZenBPM!
```

Check with:

```bash
docker compose logs log-worker
```

## View in the UI

Open http://localhost:9000 to see the deployed process definition and completed instance.

## Clean Up

```bash
docker compose down
```
