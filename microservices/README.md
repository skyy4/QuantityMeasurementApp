# Quantity Measurement Microservices

This directory contains a microservices split of the original `QuantityMeasurementApp` monolith.

## Services

- `eureka-server`
- `api-gateway`
- `auth-service`
- `qma-service`
- `history-service`
- `redis` (external service, not a Maven module)

## Responsibilities

- `eureka-server`: optional service discovery for gateway and backend services.
- `api-gateway`: single public entry point, path routing, CORS, request logging.
- `auth-service`: register, login, JWT issuing, current user profile.
- `qma-service`: quantity operations only.
- `history-service`: quantity history persistence and query.
- `redis`: optional cache layer.

## Current API shape

Gateway routes:

- `/api/auth/**` -> `auth-service`
- `/api/users/**` -> `auth-service`
- `/api/v1/quantities/**` -> `qma-service`
- `/api/v1/history/**` -> `history-service`

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

History endpoints:

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
4. `mvn -pl history-service spring-boot:run`
5. `mvn -pl api-gateway spring-boot:run`

Redis must be running before `qma-service`.

Default local ports:

- Eureka: `8761`
- Gateway: `8080`
- Auth: `8081`
- QMA: `8082`
- History: `8083`
- Redis: `6379`

## Build

From `microservices/`:

```bash
mvn clean package -DskipTests
```

## Railway deployment

Create separate Railway services for the cheapest deploy path:

1. `auth-service`
2. `qma-service`
3. `history-service`
4. `api-gateway`
5. Postgres for auth
6. Postgres for history

Optional for the full architecture:

6. `eureka-server`
7. Redis

Recommended deployment order for the cheapest deploy:

1. Auth
2. QMA
3. History
4. Gateway
5. Frontend

Recommended deployment order for the full architecture:

1. Eureka
2. Auth
3. QMA
4. History
5. Gateway
6. Frontend

### Railway build/start commands

Use repository root as source, but set commands from `microservices/`.

For `eureka-server`:

- Build: `cd microservices && mvn -pl eureka-server -am package -DskipTests`
- Start: `cd microservices && java -jar eureka-server/target/eureka-server-1.0-SNAPSHOT.jar`

For `auth-service`:

- Build: `cd microservices && mvn -pl auth-service -am package -DskipTests`
- Start: `cd microservices && java -jar auth-service/target/auth-service-1.0-SNAPSHOT.jar`
- Dockerfile path (Render): `microservices/auth-service/Dockerfile`

For `qma-service`:

- Build: `cd microservices && mvn -pl qma-service -am package -DskipTests`
- Start: `cd microservices && java -jar qma-service/target/qma-service-1.0-SNAPSHOT.jar`
- Dockerfile path (Render): `microservices/qma-service/Dockerfile`

For `history-service`:

- Build: `cd microservices && mvn -pl history-service -am package -DskipTests`
- Start: `cd microservices && java -jar history-service/target/history-service-1.0-SNAPSHOT.jar`
- Dockerfile path (Render): `microservices/history-service/Dockerfile`

For `api-gateway`:

- Build: `cd microservices && mvn -pl api-gateway -am package -DskipTests`
- Start: `cd microservices && java -jar api-gateway/target/api-gateway-1.0-SNAPSHOT.jar`
- Dockerfile path (Render): `microservices/api-gateway/Dockerfile`

### Required env vars

Set these on `auth-service`, `qma-service`, and `history-service`:

- `APP_JWT_SECRET`
- `APP_CORS_ALLOWED_ORIGINS`
- `EUREKA_CLIENT_ENABLED=false`

Set these on `auth-service`:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.postgresql.Driver`
- `SPRING_JPA_HIBERNATE_DDL_AUTO=update`

Set these on `qma-service`:

- `SPRING_CACHE_TYPE=simple`
- `HISTORY_SERVICE_BASE_URL=<public history-service URL>`
- `HISTORY_SERVICE_API_KEY=<shared internal key>`

If you also deploy Redis, then use:

- `SPRING_CACHE_TYPE=redis`
- `SPRING_DATA_REDIS_HOST`
- `SPRING_DATA_REDIS_PORT`
- `SPRING_DATA_REDIS_PASSWORD` if Railway Redis requires it
- `APP_CACHE_RESULT_TTL_MINUTES=30`

Set these on `history-service`:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.postgresql.Driver`
- `SPRING_JPA_HIBERNATE_DDL_AUTO=update`
- `HISTORY_SERVICE_API_KEY=<shared internal key>`

Set these on `api-gateway`:

- `EUREKA_CLIENT_ENABLED=false`
- `APP_CORS_ALLOWED_ORIGIN_PATTERNS`
- `AUTH_SERVICE_URI=<public auth-service URL>`
- `QMA_SERVICE_URI=<public qma-service URL>`
- `HISTORY_SERVICE_URI=<public history-service URL>`

Set this on all deployed services where Railway provides dynamic ports:

- `SERVER_PORT=${PORT}`

If you deploy the full Eureka setup later, replace those direct URIs with:

- `EUREKA_CLIENT_ENABLED=true`
- `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=<your eureka URL>/eureka/`

### Railway URL notes

- Gateway should be the only public backend URL used by the frontend.
- After deploy, set frontend `VITE_API_BASE_URL` to the Railway URL of `api-gateway`.

Example:

```env
VITE_API_BASE_URL=https://your-api-gateway-production.up.railway.app
```

## Notes

- JWTs now include `userId`, `email`, `name`, and `role` claims.
- `qma-service` and `history-service` validate JWT locally using the shared secret.
- Quantity history is owned by `history-service`.
- `qma-service` persists history through an internal API key.
- Result caching is handled in `qma-service`; Redis is optional infrastructure.
- For a low-cost cloud demo, Eureka and Redis can be disabled by env vars while keeping the 4-service architecture in source.
