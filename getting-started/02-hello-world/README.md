# 02 · Run your first BPMN process

Deploy the **hello-world** process, run a **Java worker** that handles its one service task, and watch an instance execute end to end.

```
[Start] --> [Log Greeting] --> [Done]
                 |
            log-worker   <- the Java worker in this folder
```

Deploying is done **manually** with `curl` (the tutorial teaches it). The worker is provided in **both Java and Go** — pick one.

## Files

| Path | What it is |
|---|---|
| `hello-world.bpmn` | The process definition you deploy (shared by both workers) |
| `java/` | A Java (Spring Boot) worker that handles the `log-worker` job |
| `go/` | A Go worker that handles the `log-worker` job |

## Prerequisites

- The engine running — start it in [01-run-the-engine](../01-run-the-engine/) (`docker compose up -d`)
- For the Java worker: Java 17+ and Maven — **or** for the Go worker: Go 1.22+

## 1. Deploy the process (manual)

```bash
curl -X POST http://localhost:8080/v1/process-definitions \
  -F "resource=@hello-world.bpmn"
```

Note the returned `processDefinitionKey`.

## 2. Run the worker

Pick your language and leave it running.

**Java**

```bash
cd java
mvn spring-boot:run
```

> Set the `zenbpm.version` property in `java/pom.xml` to the client release matching your engine before building — see the comment in the file.

**Go**

```bash
cd go
go mod tidy
go run .
```

Either one connects to the engine and registers the `log-worker` handler.

## 3. Start an instance

In another terminal, using the key from step 1:

```bash
curl -X POST http://localhost:8080/v1/process-instances \
  -H "Content-Type: application/json" \
  -d '{"processDefinitionKey": <PROCESS_DEFINITION_KEY>, "variables": {}}'
```

The worker logs:

```
[log-worker] Hello, World!
```

That greeting came out of your instance. Try starting an instance **before** the worker is running to see it park at the service task and wait — then start the worker and watch it complete.

## Clean up

Stop the worker (Ctrl+C), then stop the engine from [01-run-the-engine](../01-run-the-engine/) with `docker compose down`.
