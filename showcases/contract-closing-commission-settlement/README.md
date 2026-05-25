# Showcase: Contract Closing & Commission Settlement

> **Status: Ready to be used**

A full end-to-end contract closing and commission settlement application demonstrating how ZenBPM orchestrates a real-world business process involving multiple departments, systems, and human approvals.

## Used Architecture

```
                  ┌──────────────┐
                  │  Frontend    │  React app for salesperson and management
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

## BPMN Flow

The process covers the full lifecycle from contract creation to commission payout:

1. **Create the contract** — salesperson fills in client details, region, contract type, value and payment terms
2. **Electronically sign** — director signs the contract
3. **Send to client** — service task dispatches the contract by e-mail
4. **Determine commission** — DMN decision evaluates region × contract value × contract type × payment terms
5. **Approve commission** *(EMEA & LATAM only)* — director reviews and approves the proposed commission
6. **Show commission** — salesperson is notified of the final commission amount

## Directory Structure

```
showcases/contract-closing-commission-settlement/
├── frontend/       # React frontend
├── backend/        # API backend
├── workers/        # ZenBPM job workers
├── process/        # BPMN & DMN process definitions
└── docker-compose.yml
```

## Contributing

Feel free to contribute to this showcase.
