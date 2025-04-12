# UIS CSC478 Capstone Project - Spring 2025 - Group 8
# Job Tracker Application API Dockerfile

# -------------------------------------------------------
# Stage 1: Build the application
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .

# download Maven dependencies (so they are cached)
RUN mvn dependency:go-offline

# Copy source and build the application
COPY src ./src
RUN mvn package -DskipTests

# -------------------------------------------------------
# Stage 2: Create the runtime image
FROM eclipse-temurin:17-jre-alpine

# Create a non-root user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
RUN chown -R appuser:appgroup /app

# Use the non-root user
USER appuser

# Add metadata
LABEL org.opencontainers.image.title="Job Tracker Application API"
LABEL org.opencontainers.image.version="1.0.0"
LABEL org.opencontainers.image.description="A RESTful API for tracking jobs - UIS CSC478 Capstone Project Spring 2025"
LABEL org.opencontainers.image.authors="UIS CSC478 Spring 2025 Group 8"
LABEL org.opencontainers.image.vendor="University of Illinois Springfield"
LABEL org.opencontainers.image.source="https://github.com/deddecco/senior-capstone-spring-2025"

# Spring Boot configuration
ENV SERVER_PORT=8080
EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
