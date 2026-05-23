# Showcase: Contract Onboarding and Registration

> **Status: Ready to be used

A full end-to-end contract onboarding and registration application demonstrating how ZenBPM orchestrates a real-world business process involving multiple departments, systems, and human approvals.

## Used Architecture

```
                  ┌──────────────┐
                  │  Frontend    │  React app for salesperson and procurement
                  │  (React)     │
                  └──────┬───────┘
                         │
                  ┌──────┴───────┐
                  │  Backend     │  REST API, business logic
                  │  (Java)      │
                  └──────┬───────┘
                         │
           ┌─────────────┼─────────────┐
           │             │             │
    ┌──────┴──────┐ ┌────┴────┐  ┌─────┴─────┐
    │  ZenBPM     │ │ Workers │  │ External  │
    │  Engine     │ │  (Java) │  │ Services  │
    └─────────────┘ └─────────┘  └───────────┘
```

## Existing BPMN Flow



## Directory Structure

showcases/employee-onboarding/
├── frontend/       # React frontend
├── backend/        # API backend
├── workers/        # ZenBPM job workers
├── process/        # BPMN process definitions
└── docker-compose.yml connecting the  dots
```

## Prerequisites

The `contract-onboarding-worker` pulls `zenbpm-spring-boot-starter` from GitHub Packages, which requires authentication. Before running `docker compose up`, create a `.env` file from the template:

```sh
cp .env.example .env
# then fill in GITHUB_ACTOR and GITHUB_TOKEN
```

`GITHUB_TOKEN` must be a personal access token (classic or fine-grained) with the **read:packages** scope.

## Contributing

Feel free to contribute to this showcase
