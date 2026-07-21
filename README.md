# AppraiseHub

A performance appraisal system with role-based workflows for Employees, Managers, and HR — appraisal cycles, self-assessments, manager reviews, goals, notifications, and reports.

## Structure

- `src/` — Spring Boot backend (Java, Spring Security + JWT, JPA).
- `frontend/` — React + TypeScript frontend (Vite, Tailwind, TanStack Query, Zustand).

## Running the backend

```bash
./mvnw spring-boot:run
```

The backend starts on `http://localhost:8080`. Configure your database and JWT secret in `src/main/resources/application.properties`.

## Running the frontend

```bash
cd frontend
npm install
npm run dev
```

The frontend expects the backend at `http://localhost:8080` by default (see `frontend/.env.example`, copy to `.env` and adjust `VITE_API_HOST` if needed).

## Roles

- **EMPLOYEE** — writes self-assessments, tracks goal progress, acknowledges completed appraisals.
- **MANAGER** — reviews direct reports' self-assessments, sets goals, submits manager reviews.
- **HR** — creates appraisal cycles (single or bulk per department), approves completed appraisals, manages users and departments.
