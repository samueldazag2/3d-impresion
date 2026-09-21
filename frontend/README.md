# PrintCost Studio — Frontend

React + Vite + TypeScript SPA. Consumes the `backend/` API — no data is stored in
`localStorage`.

## Stack

- React 19, React Router, TanStack Query for server state, Axios for HTTP.
- Tailwind CSS v4 (tokens in `src/styles/tokens.css`) — dark theme, orange accent,
  WCAG 2.1 AA contrast checked.
- ESLint (flat config) + Prettier, `eslint-plugin-jsx-a11y` for accessibility linting.

## Folder structure

Feature-based, not type-based:

```
src/
  app/            routing, providers, app shell
  features/       one folder per domain (auth, quotes, materials, clients, settings, ...)
  shared/         cross-feature API client and UI primitives
  styles/         design tokens
```

Each feature folder exposes an `index.ts` barrel — import from the feature root, not
from its internals.

## Getting started

```bash
npm install
cp .env.example .env.local   # point VITE_API_BASE_URL at the backend
npm run dev
```

## Scripts

- `npm run dev` — start the dev server.
- `npm run build` — type-check and build for production.
- `npm run lint` — ESLint.
- `npm run format` / `npm run format:check` — Prettier.

## Deployment

Deployed to Netlify as a separate site from the legacy PrintCost Studio app.
`netlify.toml` configures the SPA fallback (`/* -> /index.html`) and baseline
security headers.
