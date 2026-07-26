# Getting Started

Runnable companion projects for the [Getting Started tutorial](https://zenbpm.pbinitiative.org/tutorials/getting-started). Each folder maps 1:1 to a chapter of the docs.

| Folder | Docs chapter | What it contains |
|---|---|---|
| [`01-run-the-engine/`](01-run-the-engine/) | Run the ZenBPM engine | A `compose.yaml` that starts the engine. No code. |
| [`02-hello-world/`](02-hello-world/) | Run your first BPMN process | The `hello-world.bpmn` process and a **worker** in both Java and Go. You deploy the process manually and run the worker. |

## How the pieces fit

- **The engine** runs from `01-run-the-engine/` and is shared by every chapter. Start it once.
- **Processes** (`.bpmn`) are deployed **manually** with `curl` in each chapter — deploying is a step the tutorial teaches, so it is never automated here.
- **Workers** are small programs *you* run. They connect to the engine over gRPC and carry out service tasks. Chapter 02 ships one as a Java project.

## Prerequisites

- [Docker](https://docs.docker.com/get-docker/) with Compose
- `curl`
- For the worker: [Java](https://adoptium.net/) 17+ and [Maven](https://maven.apache.org/), **or** [Go](https://go.dev/dl/) 1.22+

## Quick path

```bash
# 1. Start the engine
cd 01-run-the-engine
docker compose up -d

# 2. Go to chapter 02 and follow its README
cd ../02-hello-world
```
