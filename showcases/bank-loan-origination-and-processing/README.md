# Bank Loan Origination and Processing

A showcase demonstrating a bank loan origination and processing workflow using ZenBPM.

## Process overview

The process is triggered when a customer submits a loan application via the bank chatbot.
It covers credit scoring, automated/manual underwriting, customer offer acceptance, loan
disbursement, and rejection handling.

```
Loan requested
  └─► Get credit score (service)
  └─► Validate data and create ticket (service)
  └─► Loan processing (sub-process)
        ├─► Calculate loan results (DMN)
        ├─► [auto approve] Send offer to chatbot (service)
        ├─► [send to underwriter] Underwrite loan (user task) ← portal
        └─► [auto decline] Send rejection to chatbot (service)
  └─► Send offer via mail (service)
  └─► Customer response process (sub-process)
        └─► Customer accepts offer (user task) ← portal
  └─► Provide loan (service)
  └─► Send confirmation mail (service)
```

On decline (any path), an event sub-process sends a rejection mail and records
the declined application in the bank's IT systems.

## User tasks handled by the portal

| BPMN element ID        | Task name             | Form fields                          |
|------------------------|-----------------------|--------------------------------------|
| `UT_UnderwriteLoan`    | Underwrite loan       | Approve loan (checkbox), notes       |
| `UT_CustomerAcceptsOffer` | Customer accepts offer| Accept offer (checkbox)           |

## Running locally

```bash
docker compose up --build
```

- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8081
- **ZenBPM**: defined in the root `docker-compose.yml`

## Project structure

```
bank-loan-origination-and-processing/
├── backend/          # Spring Boot portal backend (layered: controller / service / client / config)
├── frontend/         # React task portal UI
├── process/          # BPMN, DMN and form JSON files
│   └── forms/        # Form schemas named after BPMN user-task element IDs
└── workers/
    └── bank-loan-worker/   # Spring Boot service-task workers
```
