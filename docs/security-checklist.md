# Checklist de seguridad — OWASP Top 10 (2021)

| # | Categoría | Estado | Justificación |
|---|---|---|---|
| A01 | Broken Access Control | ✅ Cumplido | Toda ruta salvo `/actuator/health`, `/api/auth/login` y `/api/auth/refresh` exige `ROLE_ADMIN`, verificado en el backend (`SecurityConfig`) — nunca solo ocultando UI en el frontend. |
| A02 | Cryptographic Failures | ✅ Cumplido | Contraseñas con BCrypt (`SecurityConfig.passwordEncoder`), nunca en texto plano ni en logs. JWT firmado HS256 con secreto de 32+ bytes vía variable de entorno. HTTPS obligatorio en Netlify y en el proveedor de backend elegido. |
| A03 | Injection | ✅ Cumplido | Spring Data JPA con consultas parametrizadas/derivadas (`findByNameContainingIgnoreCase`, etc.) — cero SQL concatenado en el código de la app. |
| A04 | Insecure Design | ✅ Cumplido | Costos de cotización congelados como snapshot (ver `docs/decisions.md`) evita que un cambio de configuración altere compromisos de precio ya hechos con un cliente. |
| A05 | Security Misconfiguration | ✅ Cumplido | CORS restringido a un allowlist explícito (`CORS_ALLOWED_ORIGINS`), cabeceras de seguridad (`X-Frame-Options`, `X-Content-Type-Options`, `Referrer-Policy`, CSP en Netlify), sin credenciales por defecto en producción (`ADMIN_EMAIL`/`ADMIN_PASSWORD` vía entorno). |
| A06 | Vulnerable and Outdated Components | ✅ Cumplido | Dependabot configurado para `npm` (frontend), `maven` (backend) y GitHub Actions (`.github/dependabot.yml`), revisión semanal. |
| A07 | Identification and Authentication Failures | ✅ Cumplido | JWT de corta duración (15 min) + refresh (7 días), rate limiting en `/api/auth/login` (5 intentos/minuto por IP), sesión stateless (sin cookies de sesión que fijar). |
| A08 | Software and Data Integrity Failures | ⚠️ Parcial | CI corre lint + tests antes de cualquier build; no hay todavía firma de artefactos ni verificación de proveniencia del build — razonable para el tamaño actual del proyecto, a revisar si se añaden pipelines de despliegue automatizado. |
| A09 | Security Logging and Monitoring Failures | ✅ Cumplido | `AuditLog` registra login y cada creación/actualización/borrado sensible (materiales, clientes, configuración, cotizaciones) con `userId`, acción, entidad y timestamp. Logging estructurado de Spring Boot en stdout, listo para el log drain del proveedor de hosting. |
| A10 | Server-Side Request Forgery (SSRF) | N/A | El backend no realiza llamadas salientes a URLs proporcionadas por el usuario; no hay superficie SSRF en el alcance actual. |

## Otros controles pedidos explícitamente en el alcance

- **Validación de entrada:** Bean Validation (`@field:NotBlank`, `@DecimalMin`, etc.) en cada DTO de request, además de la validación de formulario en el cliente.
- **Gestión de secretos:** `.env.example` en `frontend/` y `backend/`, `.gitignore` cubre `.env*`; nada de claves committeadas.
- **Backups de base de datos:** delegados al proveedor gestionado (Neon incluye point-in-time restore en su free tier) — documentado en el README raíz, no hay backup casero que mantener.
- **Habeas Data (Ley 1581 de 2012, Colombia):** el mini-CRM de clientes almacena nombre/contacto/notas. Antes de que el formulario de clientes quede visible al usuario final se debe mostrar un aviso de tratamiento de datos personales (finalidad, responsable, derechos ARCO) — pendiente como parte de la UI de clientes en Fase 1/2, ver `docs/scope.md`.
