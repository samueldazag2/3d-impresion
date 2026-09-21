# Decisiones técnicas (ADR corto)

## Backend: Kotlin sobre Java

El alcance ofrecía "Spring Boot (Java o Kotlin)". Se eligió **Kotlin** por
null-safety en el modelo de dominio: las fórmulas de costeo (`QuoteCostCalculator`)
son la parte más crítica del sistema — un error ahí significa cotizar mal y
perder dinero real — y el sistema de tipos de Kotlin hace inválidos por
construcción muchos errores de null que en Java solo aparecerían en runtime.

## Spring Boot 4, no 3.x

Spring Initializr entregó **Spring Boot 4.1.1** (Spring Framework 7, Spring
Security 7, Hibernate 7, Kotlin 2.3) en vez de la serie 3.x asumida al
escribir el alcance original. Esto tuvo dos consecuencias concretas:

- Varios artefactos cambiaron de nombre/paquete (`spring-boot-starter-webmvc`
  en vez de `-web`; autoconfiguración dividida en módulos por feature, p.ej.
  `org.springframework.boot.jdbc.autoconfigure.DataSourceProperties` en vez
  de `org.springframework.boot.autoconfigure.jdbc...`).
- **Spring Boot 4 eliminó su autoconfiguración de Flyway** (no existe ningún
  `spring-boot-flyway*` en el árbol de dependencias). Se resolvió en
  `DataSourceConfig`: el bean `DataSource` corre `Flyway.migrate()` sobre sí
  mismo antes de devolverse, y como `EntityManagerFactory` depende de ese
  mismo bean `DataSource`, Spring garantiza el orden de arranque sin
  `BeanDefinitionRegistryPostProcessor` ni `@DependsOn` manual.

## JWT: Nimbus (Spring Security OAuth2 Resource Server) en vez de una librería JWT de terceros

Se evaluó `io.jsonwebtoken:jjwt`, pero su módulo `jjwt-jackson` depende de
Jackson 2 (`com.fasterxml.jackson`), mientras que Spring Boot 4 corrió a
Jackson 3 (`tools.jackson`) como librería JSON por defecto — mezclar ambas
habría sido una fuente de conflictos de classpath. En su lugar:

- Emisión de tokens: `com.nimbusds:nimbus-jose-jwt` directamente (ya viene
  transitivamente con `spring-boot-starter-oauth2-resource-server`).
- Validación: `NimbusJwtDecoder.withSecretKey(...)` de Spring Security, con
  un `OAuth2TokenValidator` adicional que rechaza cualquier JWT cuyo claim
  `typ` no sea `access` — así un refresh token nunca sirve como credencial
  de API.

**Trade-off aceptado:** el refresh token es stateless (no hay tabla de
tokens revocables). Es más simple y evita una tabla extra, pero significa
que un refresh token robado sigue siendo válido hasta que expira (7 días) —
no se puede invalidar antes. Para producción con más superficie de ataque,
la mejora natural es una tabla `refresh_tokens` con revocación explícita al
hacer logout o al detectar reuso.

## Costos "congelados" en la cotización, no recalculados

`Quote` guarda un snapshot de cada componente de costo (material,
electricidad, desgaste, mano de obra, empaque) en el momento de creación, en
vez de recalcular desde `Material`/`Settings` cada vez que se lee. Así un
cambio posterior en el precio de un filamento o en la tarifa eléctrica nunca
altera cotizaciones ya emitidas — coherente con que una cotización es un
compromiso de precio con el cliente en un momento dado.

## Rate limiting con un filtro en memoria, no Bucket4j/Redis

El único endpoint que necesita rate limiting en Fase 1 es `/api/auth/login`.
Un filtro `ConcurrentHashMap` por IP con ventana deslizante cubre el caso de
uso completo sin añadir una dependencia (Bucket4j) ni infraestructura externa
(Redis). **Limitación conocida:** es por instancia — si el backend escala a
más de una instancia detrás de un balanceador, cada instancia lleva su
propio contador y el límite efectivo se multiplica. Migrar a un store
compartido (Redis) es la mejora natural si eso llega a importar.

## Base de datos y hosting (a definir en el despliegue de Fase 1)

- **PostgreSQL gestionado:** Neon — tiene un free tier con backups
  automáticos (point-in-time restore) y soporta bien conexiones desde
  Render/Railway.
- **Backend:** Render o Railway, desplegado desde `backend/Dockerfile`.
- **Frontend:** Netlify, sitio nuevo y separado del legacy.

Estos tres quedan pendientes de aprovisionar manualmente (requieren cuentas
del usuario) — ver el README raíz para el paso a paso.
