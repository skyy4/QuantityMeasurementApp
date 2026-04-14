# Quantity Measurement Microservices on Render

This guide deploys 4 backend services on Render:

1. `auth-service`
2. `qma-service`
3. `history-service`
4. `api-gateway`

Gateway is the only backend URL used by frontend.

## 1) Prerequisites

- Backend repo pushed to GitHub with latest microservice changes.
- Frontend URL ready (for CORS), for example:
  - `https://quantitymeasurementapp-frontend-s49x.onrender.com`
- 2 PostgreSQL databases:
  - one for `auth-service`
  - one for `history-service`

## 2) Deploy `auth-service` (Render Web Service, Docker)

- Root directory: `microservices/auth-service`
- Dockerfile path: `Dockerfile`
- Health check path: `/actuator/health`

Set env vars:

```text
APP_JWT_SECRET=<same-secret-for-auth-qma-history>
APP_CORS_ALLOWED_ORIGINS=https://<frontend>.onrender.com,http://localhost:5173
SPRING_DATASOURCE_URL=jdbc:postgresql://<host>:<port>/<db>
SPRING_DATASOURCE_USERNAME=<user>
SPRING_DATASOURCE_PASSWORD=<password>
SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.postgresql.Driver
SPRING_JPA_HIBERNATE_DDL_AUTO=update
```

## 3) Deploy `history-service` (Render Web Service, Docker)

- Root directory: `microservices/history-service`
- Dockerfile path: `Dockerfile`
- Health check path: `/actuator/health`

Set env vars:

```text
APP_JWT_SECRET=<same-secret-for-auth-qma-history>
APP_CORS_ALLOWED_ORIGINS=https://<frontend>.onrender.com,http://localhost:5173
SPRING_DATASOURCE_URL=jdbc:postgresql://<host>:<port>/<db>
SPRING_DATASOURCE_USERNAME=<user>
SPRING_DATASOURCE_PASSWORD=<password>
SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.postgresql.Driver
SPRING_JPA_HIBERNATE_DDL_AUTO=update
HISTORY_SERVICE_API_KEY=<long-random-shared-key>
```

## 4) Deploy `qma-service` (Render Web Service, Docker)

- Root directory: `microservices/qma-service`
- Dockerfile path: `Dockerfile`
- Health check path: `/actuator/health`

Set env vars:

```text
APP_JWT_SECRET=<same-secret-for-auth-qma-history>
APP_CORS_ALLOWED_ORIGINS=https://<frontend>.onrender.com,http://localhost:5173
SPRING_CACHE_TYPE=simple
HISTORY_SERVICE_BASE_URL=https://<history-service>.onrender.com
HISTORY_SERVICE_API_KEY=<same-key-as-history-service>
```

## 5) Deploy `api-gateway` (Render Web Service, Docker)

- Root directory: `microservices/api-gateway`
- Dockerfile path: `Dockerfile`
- Health check path: `/actuator/health`

Set env vars:

```text
APP_CORS_ALLOWED_ORIGINS=https://<frontend>.onrender.com,http://localhost:5173
AUTH_SERVICE_URI=https://<auth-service>.onrender.com
QMA_SERVICE_URI=https://<qma-service>.onrender.com
HISTORY_SERVICE_URI=https://<history-service>.onrender.com
```

## 6) Frontend variable

Set frontend env:

```text
VITE_API_BASE_URL=https://<api-gateway>.onrender.com
```

## 7) Smoke tests via gateway

Use gateway base URL:

- `GET /actuator/health`
- `POST /api/auth/register`
- `POST /api/auth/login`
- `POST /api/v1/quantities/convert` (Bearer token)
- `GET /api/v1/history/me` (Bearer token)

## Notes

- Keep `APP_JWT_SECRET` identical on `auth-service`, `qma-service`, and `history-service`.
- Keep `HISTORY_SERVICE_API_KEY` identical on `qma-service` and `history-service`.
- `APP_CORS_ALLOWED_ORIGINS` in gateway expects exact origins (comma-separated), not wildcard patterns.

