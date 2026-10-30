# Build stage
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /workspace

COPY pom.xml .
COPY src ./src
RUN mvn --batch-mode --no-transfer-progress clean package

# Runtime stage
FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
COPY --from=build /workspace/target/characterforge-0.0.1-SNAPSHOT.jar app.jar

USER spring:spring
EXPOSE 8080
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-XX:+UseContainerSupport", "-jar", "/app/app.jar"]
