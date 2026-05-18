# Showcase: Contract Onboarding and Registration

> **Status: Coming Soon**

A full end-to-end contract onboarding and registration application demonstrating how ZenBPM orchestrates a real-world business process involving multiple departments, systems, and human approvals.

## Planned Architecture

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
    ┌──────┴──────┐ ┌────┴────┐ ┌─────┴─────┐
    │  ZenBPM     │ │ Workers │ │ External  │
    │  Engine     │ │  (Java) │ │ Services  │
    └─────────────┘ └─────────┘ └───────────┘
```

## Planned BPMN Flow



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

Feel free to contribute to this showcase
