# PrintCost Studio

Calculadora de costos y precio sugerido para impresión 3D, con backend propio,
base de datos persistente y autenticación por roles. Proyecto nuevo e
independiente de la versión anterior en producción (printcost-studio.netlify.app),
usada solo como referencia funcional/visual.

## Estructura del repositorio

```
frontend/   React + Vite + TypeScript SPA (ver frontend/README.md)
backend/    API REST Spring Boot + Kotlin (ver backend/README.md)
docs/       Alcance, arquitectura, decisiones técnicas y checklist de seguridad
```

## Correr todo en local

```bash
# Backend (perfil local: H2 en memoria, sin Postgres, admin/JWT ya configurados)
cd backend
export SPRING_PROFILES_ACTIVE=local
./mvnw spring-boot:run

# Frontend, en otra terminal
cd frontend
npm install
cp .env.example .env.local
npm run dev
```

Login por defecto en local: `admin@printcoststudio.local` / `changeme123`
(ver `backend/src/main/resources/application-local.yml`).

## Documentación

- [`docs/scope.md`](docs/scope.md) — alcance funcional y forma de los datos.
- [`docs/architecture.md`](docs/architecture.md) — diagrama de componentes y flujo de datos.
- [`docs/decisions.md`](docs/decisions.md) — ADRs, incluyendo desvíos del stack sugerido.
- [`docs/security-checklist.md`](docs/security-checklist.md) — checklist OWASP Top 10.

## Despliegue (Fase 1)

Tres servicios nuevos y separados de la versión anterior:

1. **Base de datos — Neon (PostgreSQL gestionado)**
   - Crear un proyecto en [neon.tech](https://neon.tech), copiar la connection string.
   - Backups automáticos (point-in-time restore) vienen incluidos en el free tier — no requiere configuración adicional.
2. **Backend — Render**
   - Nuevo Web Service desde este repo, root directory `backend/`, runtime Docker (usa `backend/Dockerfile`, o el blueprint `render.yaml` en la raíz).
   - Variables de entorno: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` (de Neon), `JWT_SECRET` (`openssl rand -base64 48`), `CORS_ALLOWED_ORIGINS` (el dominio de Netlify), `ADMIN_EMAIL`, `ADMIN_PASSWORD`, `SPRING_PROFILES_ACTIVE=production`.
3. **Frontend — Netlify**
   - Nuevo sitio (no el existente), apuntando a `frontend/` como base directory, build command `npm run build`, publish directory `dist`.
   - Variable de entorno: `VITE_API_BASE_URL` con la URL del backend en Render.

CI (`.github/workflows/ci.yml`) corre lint + build (frontend) y Spotless +
tests (backend) en cada push/PR; los despliegues en sí son manuales por
ahora vía los dashboards de Netlify/Render.

## Convenciones

- Código en inglés (variables, funciones, clases, tablas, columnas); UI en
  español; documentación en español.
- Nombres de archivo: camelCase para archivos TypeScript de utilidades/hooks,
  PascalCase para componentes React, kebab-case para lo demás.
- Conventional Commits (`feat:`, `fix:`, `refactor:`, `docs:`, `test:`, `chore:`).
- Cada carpeta de feature expone un `index.ts` barrel — no se hacen imports
  profundos entre features.

## Estado

Fase 1 en progreso — backend completo (auth JWT, CRUD, cost engine con
tests, auditoría). Ver [`docs/scope.md`](docs/scope.md) para el plan de fases
y el estado detallado.
