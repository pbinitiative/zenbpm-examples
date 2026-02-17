# Showcase: Employee Onboarding

> **Status: Coming Soon**

A full end-to-end employee onboarding application demonstrating how ZenBPM orchestrates a real-world business process involving multiple departments, systems, and human approvals.

## Planned Architecture

```
                  ┌──────────────┐
                  │  Frontend    │  React app for HR staff
                  │  (React)     │
                  └──────┬───────┘
                         │
                  ┌──────┴───────┐
                  │  Backend     │  REST API, business logic
                  │  (Go / Node) │
                  └──────┬───────┘
                         │
           ┌─────────────┼─────────────┐
           │             │             │
    ┌──────┴──────┐ ┌────┴────┐ ┌─────┴─────┐
    │  ZenBPM     │ │ Workers │ │ External  │
    │  Engine     │ │  (Go)   │ │ Services  │
    └─────────────┘ └─────────┘ └───────────┘
```

## Planned BPMN Flow

1. **HR submits new employee request** (Start)
2. **Manager approval** (User Task)
3. **Parallel onboarding tasks:**
   - IT setup: create accounts, provision hardware (Service Tasks)
   - HR paperwork: generate contract, benefits enrollment (Service Tasks)
   - Facilities: assign desk, building access (Service Tasks)
4. **Welcome email** (Service Task)
5. **First-day checklist** (User Task)
6. **Onboarding complete** (End)

## Directory Structure

```
showcases/employee-onboarding/
├── frontend/       # React frontend
├── backend/        # API backend
├── workers/        # ZenBPM job workers
├── process/        # BPMN process definitions
└── docker-compose.yml
```

## Contributing

This showcase is under development. If you'd like to contribute, please open an issue in the [zenbpm-examples](https://github.com/pbinitiative/zenbpm-examples) repository.
