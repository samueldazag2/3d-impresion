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

With the `local` profile the app runs against an in-memory H2 database — no
PostgreSQL setup needed for day-to-day development.

## Scripts

- `./mvnw test` — unit + integration tests.
- `./mvnw spotless:check` / `./mvnw spotless:apply` — lint / auto-format Kotlin.
- `./mvnw spring-boot:run` — run locally.

## Deployment

Deployed separately from the legacy PrintCost Studio site: PostgreSQL managed
instance (Neon) + backend on Render, both provisioned in Fase 1. See
`docs/decisions.md` once written.
