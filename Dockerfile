# Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:21-jre
WORKDIR /app
# Create data directory and give it permissions
RUN mkdir -p data && chmod 777 data
COPY --from=build /app/target/QuantityMeasurementApp-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
# Use a specific H2 file path that is always reachable in Docker
ENTRYPOINT ["java", "-jar", "app.jar"]
