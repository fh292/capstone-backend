FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests && \
    ls -la target/*.jar

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*[^sources].jar cvrd.jar

# Install PostgreSQL client and curl for database operations and healthcheck
RUN apt-get update && \
    apt-get install -y postgresql-client curl && \
    rm -rf /var/lib/apt/lists/*

# Copy seed file to a known location
COPY src/main/resources/seed/seed.sql /app/seed/seed.sql

EXPOSE 8080
ENTRYPOINT ["java","-jar","cvrd.jar"]
