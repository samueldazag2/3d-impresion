# PrintCost Studio — Backend

REST API built with Spring Boot 4 (Kotlin) + Spring Data JPA + Spring Security.
PostgreSQL is the system of record — there is no `localStorage` fallback.

## Stack

- Kotlin, Spring Boot 4, Spring Data JPA, Spring Security, Bean Validation.
- PostgreSQL in every real environment; H2 in-memory only under the `local`
  Spring profile, for running the app without a local database.
- Spotless + ktlint for formatting (`mvn spotless:check` / `spotless:apply`).

## Package structure

Packages are organized by domain, not by layer (`auth/`, `quote/`, `material/`,
`client/`, `settings/`, `electricity/`, each holding their own controller,
service, repository and entity). Domain packages are added starting in Fase 1;
`config/` holds cross-cutting infrastructure wiring (security, CORS, etc.).

## Getting started

```bash
cp .env.example .env      # then export the vars, or use SPRING_PROFILES_ACTIVE=local
export SPRING_PROFILES_ACTIVE=local
./mvnw spring-boot:run
```

With the `local` profile the app runs against an in-memory H2 database and a
dev JWT secret, and seeds an admin user (`admin@printcoststudio.local` /
`changeme123` by default) — no PostgreSQL or manual setup needed.

## Authentication

- `POST /api/auth/login` — email/password, returns a short-lived access
  token (15 min) and a longer-lived refresh token (7 days), both JWTs signed
  HS256 with `JWT_SECRET`.
- `POST /api/auth/refresh` — exchanges a refresh token for a new access
  token. Refresh tokens carry a `typ: refresh` claim and are rejected by
  every other endpoint's resource-server check.
- Every endpoint other than `/actuator/health`, `/api/auth/login` and
  `/api/auth/refresh` requires `ROLE_ADMIN` (the only role with a login flow
  in Fase 1).
- `/api/auth/login` is rate-limited per IP (in-memory, 5 attempts/minute).

## Scripts

- `./mvnw test` — unit + integration tests.
- `./mvnw spotless:check` / `./mvnw spotless:apply` — lint / auto-format Kotlin.
- `./mvnw spring-boot:run` — run locally.

## Deployment

Deployed separately from the legacy PrintCost Studio site: PostgreSQL managed
instance (Neon) + backend on Render, both provisioned in Fase 1. See
`docs/decisions.md` once written.
