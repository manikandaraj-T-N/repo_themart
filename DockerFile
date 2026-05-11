# ── Stage 1: Build ──────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copy pom.xml first (layer caching — only re-downloads deps if pom changes)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source and build
COPY src ./src
RUN mvn clean package -DskipTests -B

# ── Stage 2: Run ────────────────────────────────────────────
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Create image directories (products, profiles, offers etc.)
RUN mkdir -p images/products images/profiles images/offers images/categories

# Copy jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Run
ENTRYPOINT ["java", "-jar", "app.jar"]