# Build stage
FROM maven:3.8.5-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
# Download dependencies first for caching purposes
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jre
WORKDIR /app
# COPY the jar from build stage
COPY --from=build /app/target/lost-and-found-0.0.1-SNAPSHOT.jar app.jar
# Expose the port (matches default in application.properties)
EXPOSE 8080
# Run the application with possible port override
ENTRYPOINT ["java", "-Dserver.port=${PORT:-8080}", "-jar", "app.jar"]
