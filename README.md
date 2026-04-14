# QuantityMeasurementApp (Backend + Core + Microservices)

This repository contains the core/backend evolution of the Quantity Measurement App and currently includes the Spring Boot monolith and microservices implementation.

## Actual UC Flow

1. UC1-UC18 were implemented in this repository.
2. UC19 and UC20 (frontend work) were implemented in a separate repository: `QuantityMeasurementApp-Frontend`.
3. UC21 (Microservices Architecture) was implemented directly in this repository.
4. UC22 (CI/CD + Cloud Deployment) was done with Docker/Jenkins/cloud deployment using JWT-based auth flow (no Google OAuth in deployment flow).

## UC Coverage Summary

### UC1-UC14: Core Quantity Model and Arithmetic
- Equality, conversion, arithmetic, and generic measurement model.
- Categories: Length, Weight, Volume, Temperature.
- DRY refactoring, enum-based behavior, immutability/value-object style design.

### UC15-UC18: Backend Architecture and Security
- UC15: N-tier architecture (Controller/Service/Repository/DTO).
- UC16: Persistence integration and database-driven operation history.
- UC17: Spring Boot REST backend with validation/exception handling.
- UC18: Authentication/user management with Spring Security + JWT.

### UC21-UC22: Platform Architecture and Delivery
- UC21: Microservices using Eureka, API Gateway, service separation.
- UC22: CI/CD and deployment pipeline (Docker/Jenkins/cloud), using JWT auth strategy.

## Repositories

- Backend/Core/Microservices (this repo): `https://github.com/skyy4/QuantityMeasurementApp`
- Frontend (UC19/UC20): `https://github.com/skyy4/QuantityMeasurementApp-Frontend`

## Backend Project Structure

```text
QuantityMeasurementApp/
|- src/                         # Spring Boot monolith source
|- microservices/               # UC21 microservices modules
|  |- eureka-server/
|  |- api-gateway/
|  |- auth-service/
|  |- qma-service/
|  |- history-service/
|- pom.xml
```

## Run (Monolith)

```bash
mvn spring-boot:run
```

## Run (Microservices parent)

```bash
cd microservices
mvn clean install
```
