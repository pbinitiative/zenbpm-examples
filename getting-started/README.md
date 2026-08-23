# Getting Started

Runnable companion projects for the [Getting Started tutorial](https://zenbpm.pbinitiative.org/tutorials/getting-started). Each folder maps 1:1 to a chapter of the docs.

The engine is defined once in [`compose.yaml`](compose.yaml) at the root of this track and is shared by every chapter — start it once, from this folder.

| Folder | Docs chapter | What it contains |
|---|---|---|
| [`01-run-the-engine/`](01-run-the-engine/) | Run the engine | How to start the shared engine (`compose.yaml`). No code. |
| [`02-first-bpmn-process/`](02-first-bpmn-process/) | First BPMN process | The `first-bpmn-process.bpmn` process and a **worker** in both Java and Go. You deploy the process manually and run the worker. |
| [`03-orchestrate-human-tasks/`](03-orchestrate-human-tasks/) | Orchestrate human tasks | The `approval-process.bpmn` process with a user task. You deploy it and complete the task over REST — no worker. |

## How the pieces fit

- **The engine** runs from `01-run-the-engine/` and is shared by every chapter. Start it once.
- **Processes** (`.bpmn`) are deployed **manually** with `curl` in each chapter — deploying is a step the tutorial teaches, so it is never automated here.
- **Workers** are small programs *you* run. They connect to the engine over gRPC and carry out service tasks. Chapter 02 ships one in both Java and Go.

## Prerequisites

- [Docker](https://docs.docker.com/get-docker/) with Compose
- `curl`
- For the worker: [Java](https://adoptium.net/) 17+ and [Maven](https://maven.apache.org/), **or** [Go](https://go.dev/dl/) 1.22+

## Quick path

```bash
# 1. Start the engine (from this getting-started/ folder)
docker compose up -d

# 2. Go to chapter 02 and follow its README
cd 02-first-bpmn-process
```
