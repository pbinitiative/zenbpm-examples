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

## Contributing

Feel free to contribute to this showcase
