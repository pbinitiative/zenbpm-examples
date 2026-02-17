# ZenBPM Examples

Examples and showcases for the [ZenBPM](https://github.com/pbinitiative/zenbpm) engine.

## Quick Start

```bash
docker compose up -d
```

This starts the platform, deploys all BPMN processes, and runs the workers. Open http://localhost:9000 to explore.

## Start a Process Instance

Check the deploy logs for process definition keys:

```bash
docker compose logs deploy
```

Then start a hello-world instance:

```bash
curl -X POST http://localhost:8080/v1/process-instances \
  -H "Content-Type: application/json" \
  -d '{"processDefinitionKey": <KEY_FROM_DEPLOY>, "variables": {}}'
```

Check the worker output:

```bash
docker compose logs workers
```

## Repository Structure

```
zenbpm-examples/
├── docker-compose.yml              # Platform + deploy + workers
├── docker-compose.build.yml        # Override: build from local source
├── utils/
│   └── scripts/deploy-process.sh   # Deploy a BPMN file via REST API
├── examples/
│   ├── processes/                  # BPMN processes (each with README)
│   │   ├── 01-hello-world/
│   │   └── 02-commission-payout/
│   └── workers/                    # All workers (single Go project)
│       ├── main.go
│       └── workers/
│           ├── log/
│           └── openai/
└── showcases/                      # Full end-to-end applications
    └── employee-onboarding/        # (coming soon)
```

## Processes

| # | Process | Concepts |
|---|---------|----------|
| [01](examples/processes/01-hello-world/) | **Hello World** | Service task, input mappings |
| [02](examples/processes/02-commission-payout/) | **Commission Payout** | User tasks, AI validation, exclusive gateway |

## Workers

All workers live in [`examples/workers/`](examples/workers/). Each worker has its own directory under `workers/workers/`.

| Job Type | Directory | Description |
|----------|-----------|-------------|
| `log-worker` | `workers/log/` | Reads the `log` variable and prints it |
| `openai-connector` | `workers/openai/` | Validates data via OpenAI GPT-4o-mini, returns `decision` + `reason` |

To add a new worker: create a directory under `workers/workers/`, implement the handler, and register it in `main.go`.

## Showcases

| Showcase | Status | Description |
|----------|--------|-------------|
| [Employee Onboarding](showcases/employee-onboarding/) | Coming Soon | Multi-department onboarding with parallel tasks |

## Platform

- **ZenBPM Engine** — `ghcr.io/pbinitiative/zenbpm:latest` (REST on :8080, gRPC on :9090)
- **ZenBPM UI** — `ghcr.io/pbinitiative/zenbpm-ui:latest` (http://localhost:9000)

### Building from Source

If you have the `zenbpm` and `zenbpm-ui` repos cloned as siblings:

```bash
docker compose -f docker-compose.yml -f docker-compose.build.yml up --build
```

## Writing Workers

Workers connect to ZenBPM via gRPC and handle service task jobs:

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

See [examples/workers/](examples/workers/) for the full implementation.

## License

Examples are provided under the [Apache-2.0](LICENSE) license.
