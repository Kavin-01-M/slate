# Slate — Frontend

React (Vite) UI for the Slate Smart Classroom OS. Talks to the Spring Boot backend via `fetch()` — no extra API library needed.

## Setup

```bash
cd frontend
npm install
npm run dev
```

The app runs at `http://localhost:5173`.

Make sure the backend is running at `http://localhost:8080` (see `../backend/README.md`). The API base URL is configurable from the login screen inside the app.

## Build

```bash
npm run build
```

Outputs a production build to `dist/`.
