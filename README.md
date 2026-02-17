# ZenBPM Examples

Examples and showcases for the [ZenBPM](https://github.com/pbinitiative/zenbpm) engine.

## Quick Start

### Run all examples at once

```bash
docker compose -f docker-compose.all.yml up -d
```

This starts the platform, deploys all BPMN processes, and runs a single unified worker that handles every job type. Open http://localhost:9000 to explore.

### Run a single process

```bash
cd examples/processes/01-hello-world
docker compose up -d
```

Each process auto-deploys on startup and pulls in the workers it needs.

## Repository Structure

```
zenbpm-examples/
├── docker-compose.yml              # Platform: ZenBPM engine + UI
├── docker-compose.all.yml          # All-in-one: platform + deploy all + unified worker
├── docker-compose.build.yml        # Override: build from local source
├── utils/
│   ├── scripts/deploy-process.sh   # Deploy a BPMN file via REST API
│   └── all-workers/                # Dockerfile to build all workers into one container
├── examples/
│   ├── processes/                  # BPMN processes (each with docker-compose + README)
│   │   ├── 01-hello-world/
│   │   └── 02-commission-payout/
│   └── workers/                    # Reusable worker containers
│       ├── log-worker/
│       └── openai-connector/
└── showcases/                      # Full end-to-end applications
    └── employee-onboarding/        # (coming soon)
```

## Processes

| # | Process | Workers Used | Concepts |
|---|---------|-------------|----------|
| [01](examples/processes/01-hello-world/) | **Hello World** | `log-worker` | Service task, process variables |
| [02](examples/processes/02-commission-payout/) | **Commission Payout** | `log-worker`, `openai-connector` | User tasks, AI validation, exclusive gateway |

## Workers

Reusable worker containers that handle specific job types. Processes reference them in their `docker-compose.yml`.

| Worker | Job Type | Description |
|--------|----------|-------------|
| [log-worker](examples/workers/log-worker/) | `log-worker` | Reads the `log` variable and prints it |
| [openai-connector](examples/workers/openai-connector/) | `openai-connector` | Validates data via OpenAI GPT-4o-mini, returns `decision` + `reason` |

## Showcases

Full applications that demonstrate real-world digitalization scenarios.

| Showcase | Status | Description |
|----------|--------|-------------|
| [Employee Onboarding](showcases/employee-onboarding/) | Coming Soon | Multi-department onboarding with parallel tasks |

## Platform

All examples share a common platform defined in the root `docker-compose.yml`:

- **ZenBPM Engine** — `ghcr.io/pbinitiative/zenbpm:latest` (REST on :8080, gRPC on :9090)
- **ZenBPM UI** — `ghcr.io/pbinitiative/zenbpm-ui:latest` (http://localhost:9000)

### Building from Source

If you have the `zenbpm` and `zenbpm-ui` repos cloned as siblings, you can build from local source:

```bash
docker compose -f docker-compose.yml -f docker-compose.build.yml up --build
```

## Writing Workers

Workers connect to ZenBPM via gRPC and handle service task jobs. Here's the minimal pattern:

```go
conn, _ := grpc.NewClient("localhost:9090", grpc.WithTransportCredentials(insecure.NewCredentials()))
client := zenclient.NewGrpc(conn)

client.RegisterWorker(ctx, "my-worker-1", func(ctx context.Context, job *proto.WaitingJob) (map[string]any, *zenclient.WorkerError) {
    // Read variables from job.GetVariables()
    // Do work
    // Return output variables
    return map[string]any{"result": "done"}, nil
}, "my-job-type")
```

See [examples/workers/log-worker/main.go](examples/workers/log-worker/main.go) for a complete example.

## License

Examples are provided under the [Apache-2.0](LICENSE) license.
