# Quantity Measurement Microservices

This directory contains a Eureka-based microservices split of the original `QuantityMeasurementApp` monolith.

## Services

- `eureka-server`
- `api-gateway`
- `auth-service`
- `qma-service`
- `redis` (external service, not a Maven module)

## Responsibilities

- `eureka-server`: service discovery for gateway, auth, and qma.
- `api-gateway`: single public entry point, path routing, CORS, request logging.
- `auth-service`: register, login, JWT issuing, current user profile.
- `qma-service`: quantity operations, user-scoped history, Redis caching.
- `redis`: cache quantity results and history queries.

## Current API shape

Gateway routes:

- `/api/auth/**` -> `auth-service`
- `/api/users/**` -> `auth-service`
- `/api/v1/quantities/**` -> `qma-service`
- `/api/v1/history/**` -> `qma-service`

Auth endpoints:

- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/auth/profile`
- `GET /api/auth/me`
- `GET /api/users/me`

QMA endpoints:

- `POST /api/v1/quantities/convert`
- `POST /api/v1/quantities/compare`
- `POST /api/v1/quantities/add`
- `POST /api/v1/quantities/subtract`
- `POST /api/v1/quantities/divide`
- `GET /api/v1/history/me`
- `GET /api/v1/history/me/operation/{operation}`
- `GET /api/v1/history/me/type/{type}`
- `GET /api/v1/history/me/count/{operation}`
- `GET /api/v1/history/me/errors`

## Local startup order

From the `microservices` directory:

1. `mvn -pl eureka-server spring-boot:run`
2. `mvn -pl auth-service spring-boot:run`
3. `mvn -pl qma-service spring-boot:run`
4. `mvn -pl api-gateway spring-boot:run`

Redis must be running before `qma-service`.

Default local ports:

- Eureka: `8761`
- Gateway: `8080`
- Auth: `8081`
- QMA: `8082`
- Redis: `6379`

## Build

From `microservices/`:

```bash
mvn clean package -DskipTests
```

## Railway deployment

Create separate Railway services for:

1. `eureka-server`
2. `auth-service`
3. `qma-service`
4. `api-gateway`
5. Redis
6. Postgres for auth
7. Postgres for qma

Recommended deployment order:

1. Eureka
2. Auth
3. QMA
4. Gateway
5. Frontend

### Railway build/start commands

Use repository root as source, but set commands from `microservices/`.

For `eureka-server`:

- Build: `cd microservices && mvn -pl eureka-server -am package -DskipTests`
- Start: `cd microservices && java -jar eureka-server/target/eureka-server-1.0-SNAPSHOT.jar`

For `auth-service`:

- Build: `cd microservices && mvn -pl auth-service -am package -DskipTests`
- Start: `cd microservices && java -jar auth-service/target/auth-service-1.0-SNAPSHOT.jar`

For `qma-service`:

- Build: `cd microservices && mvn -pl qma-service -am package -DskipTests`
- Start: `cd microservices && java -jar qma-service/target/qma-service-1.0-SNAPSHOT.jar`

For `api-gateway`:

- Build: `cd microservices && mvn -pl api-gateway -am package -DskipTests`
- Start: `cd microservices && java -jar api-gateway/target/api-gateway-1.0-SNAPSHOT.jar`

### Required env vars

Set these on `auth-service` and `qma-service`:

- `APP_JWT_SECRET`
- `APP_CORS_ALLOWED_ORIGINS`
- `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE`

Set these on `auth-service`:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.postgresql.Driver`
- `SPRING_JPA_HIBERNATE_DDL_AUTO=update`

Set these on `qma-service`:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.postgresql.Driver`
- `SPRING_JPA_HIBERNATE_DDL_AUTO=update`
- `SPRING_DATA_REDIS_HOST`
- `SPRING_DATA_REDIS_PORT`
- `SPRING_DATA_REDIS_PASSWORD` if Railway Redis requires it
- `APP_CACHE_RESULT_TTL_MINUTES=30`
- `APP_CACHE_HISTORY_TTL_MINUTES=5`

Set these on `api-gateway`:

- `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE`
- `APP_CORS_ALLOWED_ORIGIN_PATTERNS`

Set this on all deployed services where Railway provides dynamic ports:

- `SERVER_PORT=${PORT}`

### Railway URL notes

- Gateway should be the only public backend URL used by the frontend.
- After deploy, set frontend `VITE_API_BASE_URL` to the Railway URL of `api-gateway`.

Example:

```env
VITE_API_BASE_URL=https://your-api-gateway-production.up.railway.app
```

## Notes

- JWTs now include `userId`, `email`, `name`, and `role` claims.
- `qma-service` validates JWT locally using the shared secret.
- Quantity history is user-scoped in `qma-service`.
- Result caching is handled manually through Redis so history is still persisted even on cache hits.
- History query caching uses Spring Cache with Redis.
