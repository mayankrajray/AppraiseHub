# AppraiseHub - Employee Appraisal Platform

[![Backend](https://img.shields.io/badge/Backend-Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](src)
[![Frontend](https://img.shields.io/badge/Frontend-Vite%20%2B%20React-646CFF?style=for-the-badge&logo=vite&logoColor=white)](frontend)

**Version:** 0.0.1-SNAPSHOT
**Status:** Deployed
**Live App:** [https://appraise-hub-two.vercel.app](https://appraise-hub-two.vercel.app)
**Live API:** [https://appraisehub-production.up.railway.app](https://appraisehub-production.up.railway.app)
**Tech Stack:** Java 17, Spring Boot 3.5.14, Spring Security, JPA/Hibernate, MySQL, JWT, Vite 8, React 19.2, TypeScript 6.0, Tailwind CSS 4.3

![Java](https://img.shields.io/badge/Java-17-007396?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.14-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4169E1?style=for-the-badge&logo=mysql&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-Auth-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)
![Vite](https://img.shields.io/badge/Vite-8.1.1-646CFF?style=for-the-badge&logo=vite&logoColor=white)
![React](https://img.shields.io/badge/React-19.2.7-61DAFB?style=for-the-badge&logo=react&logoColor=000000)
![TypeScript](https://img.shields.io/badge/TypeScript-6.0.2-3178C6?style=for-the-badge&logo=typescript&logoColor=white)
![Tailwind](https://img.shields.io/badge/Tailwind%20CSS-4.3-38B2AC?style=for-the-badge&logo=tailwindcss&logoColor=white)

---

## Project Overview

AppraiseHub is a full-stack employee performance appraisal platform with a Spring Boot REST API and a React (Vite) dashboard. It supports role-based appraisal cycles, self-assessments, manager reviews, goal tracking, and notifications, built with a clean layered monolith backend and a component-driven frontend.

**Modules:**

- **Backend:** Spring Boot API with JWT authentication, JPA persistence, and role-based access control.
- **Frontend:** React 19 + TypeScript app built with Vite, styled with Tailwind CSS, using TanStack Query for data fetching and Zustand for auth state.

---

## System Architecture

The backend follows a clean layered monolith with clear separation of controller, service, and repository layers. Authentication is stateless via JWT, and DTOs isolate persistence entities from API responses.

**High-Level Flow:**

```
Client UI (React/Vite) -> Spring Boot API -> MySQL
```

---

## Core Technology Stack

| Layer     | Technology                  | Version | Purpose                            |
|-----------|------------------------------|---------|-------------------------------------|
| Runtime   | OpenJDK                     | 17      | Backend runtime                     |
| Framework | Spring Boot                 | 3.5.14  | REST API and dependency injection   |
| Security  | Spring Security + JJWT      | 6.x + 0.12.6 | JWT authentication and RBAC    |
| ORM       | Hibernate (JPA)              | 6       | Data persistence                    |
| Database  | MySQL                        | 8+      | Relational data storage             |
| Build     | Maven                         | -       | Backend build and packaging         |
| Frontend  | Vite + React + TypeScript    | 8.1 + 19.2 + 6.0 | UI framework and type safety |
| Styling   | Tailwind CSS                 | 4.3     | UI styling                          |
| Data      | TanStack Query + Zustand      | 5.x     | Server state and auth state         |
| Hosting   | Railway (backend + MySQL) + Vercel (frontend) | - | Live deployment |

---

## Database Schema

The schema supports multi-step appraisal cycles. Core entities include Users, Departments, Appraisals, Goals, and Notifications, with role-based constraints (HR, MANAGER, EMPLOYEE) enforced at the API layer.

---

## API Architecture

The API is JWT-secured and role-aware (HR, MANAGER, EMPLOYEE). Requests are authenticated via the `Authorization` header and validated by a Spring Security filter chain. DTOs isolate persistence entities from API responses.

---

## Security Implementation

### Authentication & Authorization

**Authentication:**

- Spring Security 6.x with JWT (JJWT 0.12.6)
- BCrypt password hashing
- Role-based access control for HR, MANAGER, and EMPLOYEE

### Input Validation

- `jakarta.validation` for request validation
- Global exception handling via `@RestControllerAdvice`

### Session Handling Notes

- `spring.jpa.open-in-view=false`, so lazily-loaded relations (department, manager) are fetched inside `@Transactional` service methods rather than the view layer.

---

## Deployment Architecture

- **Backend + MySQL:** deployed on Railway, connected to this GitHub repo for auto-redeploy on push to `main`.
- **Frontend:** deployed on Vercel with root directory set to `frontend/`, using `VITE_API_HOST` to point at the live Railway API.
- **CORS:** the backend's allowed origins list includes the live Vercel domain (see `src/main/java/com/appraisehub/config/SecurityConfig.java`).

---

## Getting Started

### Prerequisites

```bash
Java 17
Maven (or Maven Wrapper)
Node.js 18+
MySQL 8+
```

### Backend (Spring Boot)

```bash
# Windows
mvnw.cmd spring-boot:run

# macOS/Linux
./mvnw spring-boot:run
```

The backend starts on `http://localhost:8080`.

### Frontend (Vite + React)

```bash
cd frontend
npm install
npm run dev
```

### Configuration

Development properties live in `src/main/resources/application.properties`.

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/appraisehub
spring.datasource.username=root
spring.datasource.password=

jwt.secret=change-me
jwt.expiration=86400000
```

Frontend configuration lives in `frontend/.env` (copy from `frontend/.env.example`):

```properties
VITE_API_HOST=http://localhost:8080
```

---

## Testing

```bash
./mvnw test
```

```bash
cd frontend
npm run lint
```

---

## Project Structure

```
AppraiseHub/
├── src/
│   ├── main/java/               # Spring Boot source
│   └── main/resources/          # application.properties
├── pom.xml                      # Maven config
└── frontend/
    ├── src/
    │   ├── pages/                # Route-level screens
    │   ├── components/           # Shared UI components
    │   └── lib/                  # API client, auth store, types
    └── package.json
```

---

## Roadmap

- Reporting dashboards for HR and managers
- More granular appraisal workflows and approvals
- Audit logging for critical HR actions
- Custom domain for the live frontend

---

## Technical Decisions

- Spring Boot with layered architecture for maintainability and testability
- JWT-based stateless auth to support web and API clients
- MySQL for relational integrity across users, departments, appraisals, and goals
- Vite + React for a fast dev loop and a UI deliberately distinct from other implementations of this system
- TanStack Query with direct cache patching (`setQueryData`) for instant UI updates on mutations, rather than relying solely on refetch timing

---

## Monitoring & Observability

- Application logs via Spring Boot logging
- Railway deployment logs for the backend service

---

## Contributing

```bash
git checkout -b feature/feature-name
# Make changes
git commit -m "Add: feature description"
git push origin feature/feature-name
# Open pull request
```

**Code Standards:**

- Backend: standard Spring Boot conventions
- Frontend: TypeScript strict mode, oxlint

---

## Spring Boot Flow

This section outlines the typical Spring Boot request & runtime flow used by the backend:

- **Client → Controller:** HTTP requests from the UI or API clients are received by `@RestController` endpoints.
- **Controller → Service:** Controllers delegate business logic to `@Service` components.
- **Service → Repository:** Services use Spring Data JPA repositories (`@Repository`) to read/write domain entities.
- **Persistence → DB:** Hibernate (JPA) translates entity operations into SQL executed against MySQL.
- **Security & Filters:** Requests pass through the Spring Security filter chain (authentication/authorization) and a custom JWT filter (`JwtAuthFilter`, an `OncePerRequestFilter`).
- **DTOs & Mapping:** Controllers expose DTOs; entity ↔ DTO mapping isolates persistence concerns from API contracts.
- **Exception Handling:** Global exceptions are handled by `@RestControllerAdvice` to standardize error responses.
- **Config:** Configuration lives under `src/main/resources` (`application.properties`); production values are supplied via Railway environment variables.
- **Build & Run:** Use Maven (or the included Maven Wrapper) to build and run. Packaging produces an executable JAR which can be run with `java -jar`.

Common commands:

```bash
# Windows (dev)
mvnw.cmd spring-boot:run

# Package
./mvnw package    # or mvnw.cmd package on Windows

# Run packaged jar
java -jar target/appraisehub-0.0.1-SNAPSHOT.jar
```

---

## Current Project Flow

This project (`AppraiseHub`) follows the flow below when developing, building, and deploying:

- **Local dev:**
    - Start the backend in dev mode: `mvnw.cmd spring-boot:run` (Windows) or `./mvnw spring-boot:run` (macOS/Linux).
    - Start the frontend with Vite: `cd frontend && npm install && npm run dev`.
- **Configuration:**
    - Backend properties: [src/main/resources/application.properties](src/main/resources/application.properties).
    - Frontend environment: [frontend/.env.example](frontend/.env.example) (copy to `.env`, set `VITE_API_HOST`).
- **Build & Package:**
    - Backend: `./mvnw package` produces the application artifact at `target/` (e.g. `appraisehub-0.0.1-SNAPSHOT.jar`).
    - Frontend: `npm run build` in `frontend` to produce the optimized Vite output.
- **Deployment:**
    - Backend is deployed on Railway, connected to this repo — every push to `main` triggers an automatic redeploy.
    - Frontend is deployed on Vercel, built from the `frontend/` directory with `VITE_API_HOST` pointing at the live Railway URL.
- **Runtime interactions:**
    - The frontend communicates with the backend via the REST API, authenticated via JWT tokens issued by the backend and sent in the `Authorization` header by the frontend.
    - CORS on the backend explicitly allows the live Vercel origin alongside local dev origins.

---

## License

ISC [License](./LICENSE)

---

## Author

**Mayank Raj Ray**
GitHub: [@mayankrajray](https://github.com/mayankrajray)

---
