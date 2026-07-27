# 01 · Run the engine

Everything else in Getting Started builds on a running engine. This chapter starts one — no BPMN, no code.

The engine is defined in the shared [`compose.yaml`](../compose.yaml) at the root of the Getting Started track. Run these commands **from the `getting-started/` folder**.

## Start

```bash
cd ..            # into getting-started/, where compose.yaml lives
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

An empty list is the healthy response: the engine is up with nothing deployed yet. You're ready for [02-first-bpmn-process](../02-first-bpmn-process/).

## Optional: web UI

```bash
docker compose --profile ui up -d
```

Then open http://localhost:9000.

## Stop

```bash
docker compose down
```

Engine state is kept in a named volume, so it survives a restart. Use `docker compose down -v` to wipe it.
