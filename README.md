# PrintCost Studio

Calculadora de costos y precio sugerido para impresión 3D, con backend propio,
base de datos persistente y autenticación por roles. Proyecto nuevo e
independiente de la versión anterior en producción (printcost-studio.netlify.app),
usada solo como referencia funcional/visual.

## Estructura del repositorio

```
frontend/   React + Vite + TypeScript SPA (ver frontend/README.md)
backend/    API REST (ver backend/README.md)
docs/       Alcance, arquitectura y decisiones técnicas
```

## Documentación

- [`docs/scope.md`](docs/scope.md) — alcance funcional y forma de los datos.
- `docs/architecture.md` — diagrama de componentes y flujo de datos (Fase 1).
- `docs/decisions.md` — ADRs (Fase 1).

## Convenciones

- Código en inglés (variables, funciones, clases, tablas, columnas); UI en
  español; documentación en español.
- Nombres de archivo: camelCase para archivos TypeScript de utilidades/hooks,
  PascalCase para componentes React, kebab-case para lo demás.
- Conventional Commits (`feat:`, `fix:`, `refactor:`, `docs:`, `test:`, `chore:`).
- Cada carpeta de feature expone un `index.ts` barrel — no se hacen imports
  profundos entre features.

## Estado

Fase 0 — scaffold inicial. Ver [`docs/scope.md`](docs/scope.md) para el plan de
fases completo.
