# 02 · First BPMN process

Deploy the **first-bpmn-process**, run a **worker** (Java or Go) that handles its one service task, and watch an instance execute end to end.

```
[Start] --> [Log Greeting] --> [Done]
                 |
            log-worker   <- the worker (java/ or go/) in this folder
```

Deploying is done **manually** with `curl` (the tutorial teaches it). The worker is provided in **both Java and Go** — pick one.

## Files

| Path | What it is |
|---|---|
| `first-bpmn-process.bpmn` | The process definition you deploy (shared by both workers) |
| `java/` | A Java (Spring Boot) worker that handles the `log-worker` job |
| `go/` | A Go worker that handles the `log-worker` job |

## Prerequisites

- The engine running — from the [`getting-started/`](../) folder run `docker compose up -d` (see [01-run-the-engine](../01-run-the-engine/))
- For the Java worker: Java 17+ and Maven — **or** for the Go worker: Go 1.22+

## 1. Deploy the process (manual)

```bash
curl -X POST http://localhost:8080/v1/process-definitions \
  -F "resource=@first-bpmn-process.bpmn"
```

Note the returned `processDefinitionKey`.

## 2. Run the worker

Pick your language and leave it running.

**Java**

The ZenBPM Java client is on Maven Central, so no extra setup is needed:

```bash
cd java
mvn spring-boot:run
```

> `java/pom.xml` uses `org.pbinitiative.zenbpm:zenbpm-spring-boot-starter:1.4.0` (the client version tracks the engine version) plus `grpc-netty-shaded` for the worker transport.

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

Stop the worker (Ctrl+C), then stop the engine from the [`getting-started/`](../) folder with `docker compose down`.
