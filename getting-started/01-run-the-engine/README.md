# 01 · Run the engine

Everything else in Getting Started builds on a running engine. This chapter starts one — no BPMN, no code.

## Start

```bash
docker compose up -d
```

The engine exposes two APIs:

| Address | Protocol | Used for |
|---|---|---|
| `http://localhost:8080` | REST | Deploy processes, start instances, query state |
| `localhost:9090` | gRPC | Connect workers |

## Verify

```bash
curl http://localhost:8080/v1/process-definitions
```

An empty list is the healthy response: the engine is up with nothing deployed yet. You're ready for [02-hello-world](../02-hello-world/).

## Optional: web UI

```bash
docker compose --profile ui up -d
```

Then open http://localhost:9000.

## Stop

```bash
docker compose down
```

The engine keeps its state in the container, so `down` clears everything you deployed.
