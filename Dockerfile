# ---------- Build Stage ----------
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /build

# Copy pom first for dependency caching
COPY pom.xml .

RUN mvn -B dependency:go-offline

# Copy source code
COPY src ./src

# Package the application
RUN mvn -B clean package -DskipTests

# ---------- Run Stage ----------
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy built jar from build stage
COPY --from=build /build/target/*.jar app.jar

EXPOSE 8080

ENV JAVA_TOOL_OPTIONS="-Xmx512m"

ENTRYPOINT ["java", "-jar", "app.jar"]

