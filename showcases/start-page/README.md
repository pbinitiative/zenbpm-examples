# ZenBPM Showcase Launcher

A simple start page that lists all available showcases and links directly to their UIs.

## Usage

```bash
cd showcases/start-page
npm install
npm start
```

Then open **http://localhost:8888**.

## How it works

- Scans all subdirectories of `showcases/` that contain a `docker-compose.yml`
- Reads each showcase's `README.md` for the title and description
- Reads each showcase's `.showcase.json` for the port number
- Renders a card for each showcase with a direct **Open** link

## Adding a new showcase

1. Create the showcase folder with a `docker-compose.yml` and `README.md`
2. Add a `.showcase.json` file with the assigned ports:

```json
{
  "frontendPort": 3003,
  "backendPort": 8084
}
```

## Port assignments

| Showcase                              | Frontend | Backend |
|---------------------------------------|----------|---------|
| contract-closing-commission-settlement| 3000     | 8081    |
| bank-loan-origination-and-processing  | 3001     | 8082    |
| employee-onboarding                   | 3002     | 8083    |
| start-page                            | 8888     | —       |
