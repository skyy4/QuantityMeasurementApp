# QuantityMeasurementApp

This log documents the daily progress of tasks completed during the Quantity Measurement App development, identifying work done on each date with thematic headings and detailed summaries.

## Folder Structure

The repository is organized in a modular way following the standard Maven directory structure.

Currently, the application code resides in the **src/main/java** directory, and the test code resides in the **src/test/java** directory.

```text
QuantityMeasurementApp/
|
+-- src/main/java (Application Code)
|
+-- src/test/java (Test Cases)
|
+-- pom.xml (Maven Configuration)
|
+-- README.md
```

## Week 1: Quantity Measurement System
**TDD Approach & Unit Conversion**

*   **18-Feb-2026 (Wednesday):** Created the repository and set up the project structure. Started implementing the Quantity Measurement System using Test Driven Development (TDD). Addressed UC1 (Feet Equality) to handle feet measurement comparisons. Extended functionality to support **Inches Equality** (UC2), allowing comparison of inch values with 100% test coverage.
    *   [Browse UC1 Implementation](https://github.com/skyy4/QuantityMeasurementApp/tree/feature/UC1-FeetEquality)
    *   [Browse UC2 Implementation](https://github.com/skyy4/QuantityMeasurementApp/tree/feature/UC2-FeetAndInchesEquality)

*   **19-Feb-2026 (Thursday):** Refactored the code to use a generic `Quantity` class and `LengthUnit` enum (UC3), implementing the DRY principle. Extended the system to support `Yard` and `Centimeter` units (UC4) with comprehensive test coverage for cross-unit comparisons.
    *   [Browse UC3 Implementation](https://github.com/skyy4/QuantityMeasurementApp/tree/feature/UC3-GenericQuantity)
    *   [Browse UC4 Implementation](https://github.com/skyy4/QuantityMeasurementApp/tree/feature/UC4-ExtendedUnitSupport)

*   **23-Feb-2026 (Monday):** Implemented unit-to-unit conversions between different length units (UC5). Added `convert()` and `convertTo()` methods to the `Quantity` class and ensured robust input validation and floating-point precision compatibility across comprehensive test cases.
    *   [Browse UC5 Implementation](https://github.com/skyy4/QuantityMeasurementApp/tree/feature/UC5-UnitConversion)

*   **09-Mar-2026 (Monday):** Expanded system capabilities drastically by adding quantity addition (UC6 & UC7), improving entity cohesion by refactoring `LengthUnit` (UC8), supporting a secondary `WeightUnit` category (UC9), and finally refactoring the entire architecture to completely generic `Quantity<U extends IMeasurable>` bounds, solving categorical duplication (UC10).
    *   [Browse UC6 Implementation](https://github.com/skyy4/QuantityMeasurementApp/tree/feature/UC6-Addition)
    *   [Browse UC7 Implementation](https://github.com/skyy4/QuantityMeasurementApp/tree/feature/UC7-AdditionWithTargetUnit)
    *   [Browse UC8 Implementation](https://github.com/skyy4/QuantityMeasurementApp/tree/feature/UC8-EnumRefactoring)
    *   [Browse UC9 Implementation](https://github.com/skyy4/QuantityMeasurementApp/tree/feature/UC9-WeightMeasurement)
    *   [Browse UC10 Implementation](https://github.com/skyy4/QuantityMeasurementApp/tree/feature/UC10-GenericQuantity)

*   **10-Mar-2026 (Tuesday):** Extended the capabilities to include the `VolumeUnit` measurement category (UC11). Added support for Subtraction and Division arithmetic operations across categories (UC12). Refactored arithmetic logic into a centralized `ArithmeticOperation` enum to maintain the DRY principle (UC13). Finally, added `TemperatureUnit` with a `SupportsArithmetic` interface inside `IMeasurable` to cleanly disable operations on non-linear units (UC14).
    *   [Browse UC11 Implementation](https://github.com/skyy4/QuantityMeasurementApp/tree/feature/UC11-VolumeMeasurement)
    *   [Browse UC12 Implementation](https://github.com/skyy4/QuantityMeasurementApp/tree/feature/UC12-ArithmeticOperations)
    *   [Browse UC13 Implementation](https://github.com/skyy4/QuantityMeasurementApp/tree/feature/UC13-CentralizedArithmetic)
    *   [Browse UC14 Implementation](https://github.com/skyy4/QuantityMeasurementApp/tree/feature/UC14-TemperatureMeasurement)

*   **12-Mar-2026 (Thursday):** Refactored the application to an **N-Tier Architecture** (UC15) by introducing distinct Controller, Service, Repository, and DTO layers. This separation of concerns improves maintainability and scalability, with `QuantityMeasurementController` driving the application flow, `IQuantityMeasurementService` defining the service contract, and `QuantityDTO` as the data transfer object between layers.
    *   [Browse UC15 Implementation](https://github.com/skyy4/QuantityMeasurementApp/tree/feature/UC15-NTierArchitecture)

*   **13-Mar-2026 (Friday):** Integrated a **JDBC Database Repository** (UC16) to persist all quantity measurement operations. Replaced the in-memory repository with `QuantityMeasurementDatabaseRepository`, introduced a `ConnectionPool` for efficient connection management, and added `ApplicationConfig` for centralised JDBC configuration. All operations (compare, convert, add, subtract, divide) are now saved to and retrieved from the database.
    *   [Browse UC16 Implementation](https://github.com/skyy4/QuantityMeasurementApp/tree/feature/UC16-DataBaseIntegration)

*   **17-Mar-2026 (Tuesday):** Migrated the application to a full **Spring Boot REST Service** (UC17) by leveraging the Spring Framework ecosystem. Replaced raw JDBC with **Spring Data JPA** (H2 in-memory database), exposed all operations as RESTful HTTP endpoints via `QuantityMeasurementController` (`/api/v1/quantities/...`), added **Spring Security** configuration, centralised exception handling with `@ControllerAdvice`, and integrated **Swagger/OpenAPI** for interactive API documentation. All 7 tests (MockMvc + Integration) pass.
    *   [Browse UC17 Implementation](https://github.com/skyy4/QuantityMeasurementApp/tree/feature/UC17-SpringIntegration)

*   **30-Mar-2026 (Monday):** Integrated **Google OAuth2** authentication and **JWT** (JSON Web Token) generation (UC18) to secure the application. Protected all REST API measurement endpoints by requiring a valid Bearer token. Implemented an `OAuth2SuccessHandler` and a `UserManagement` system targeting an H2 database via Spring Data JPA to seamlessly map Google users to local profiles. Fixed IDE lint warnings by enforcing strict null-safety checks and removed local secrets by relying on GitHub Push Protection standards.
    *   [Browse UC18 Implementation](https://github.com/skyy4/QuantityMeasurementApp/tree/feature/UC18-google-auth-jwt-user-management)

## Project Continuation After UC18 (Approved Flow)

*   **UC19-UC20 (Frontend):** Implemented in separate repository as approved.
    *   [Frontend Repository](https://github.com/skyy4/QuantityMeasurementApp-Frontend)
    *   [UC19 branch (html-css-js)](https://github.com/skyy4/QuantityMeasurementApp-Frontend/tree/html-css-js)
    *   [UC20 branch (react)](https://github.com/skyy4/QuantityMeasurementApp-Frontend/tree/react)

*   **UC21 (Microservices Architecture):** Implemented directly in this repository under `microservices/`.
    *   [Microservices Folder](https://github.com/skyy4/QuantityMeasurementApp/tree/main/microservices)

*   **UC22 (CI/CD + Cloud Deployment):** Implemented and validated with Docker/Jenkins/cloud deployment flow. Final deployment auth flow uses JWT (without Google OAuth in deployment), and this is mentor-approved.
