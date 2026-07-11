# Slate — Smart Classroom OS

A full-stack classroom management system: **Students, Classes, Attendance**, with JWT auth and role-based access (`ADMIN`, `TEACHER`, `STUDENT`).

## Structure

```
slate/
├── backend/    Spring Boot 3 + MySQL + JWT API
└── frontend/   React (Vite) UI, talks to the API via fetch()
```

## Quick start

**1. Backend** (needs Java 17 + MySQL)

```bash
cd backend
# edit src/main/resources/application.properties with your MySQL credentials
mvn spring-boot:run
```

Runs on `http://localhost:8080`.

**2. Frontend** (needs Node.js)

```bash
cd frontend
npm install
npm run dev
```

Runs on `http://localhost:5173`.

See `backend/README.md` and `frontend/README.md` for full details, including auth flow and API usage.

## Stack

- **Backend:** Java 17, Spring Boot 3.3, Spring Data JPA, Spring Security, MySQL, JWT (jjwt)
- **Frontend:** React 18, Vite

## Security note

Before deploying anywhere real, change `app.jwt.secret` in `backend/src/main/resources/application.properties` and load it from an environment variable instead of committing it.
