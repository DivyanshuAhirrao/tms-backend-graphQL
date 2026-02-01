FROM eclipse-temurin:21-jdk-alpine AS build

# Set working dir
WORKDIR /workspace

# Copy only Maven config first (for better caching)
COPY pom.xml mvnw ./
COPY .mvn .mvn

# Download dependencies to cache
RUN ./mvnw dependency:go-offline -B

# Copy entire project
COPY src ./src

# Build application artifact
RUN ./mvnw clean package -DskipTests

# ---------- Run Stage ----------
FROM eclipse-temurin:21-jre-alpine

# Create app dir
WORKDIR /app

# Copy jar file from build stage
COPY --from=build /workspace/target/*.jar app.jar

# Expose port (Spring Boot default)
EXPOSE 8080

# Set environment (optional)
ENV JAVA_TOOL_OPTIONS="-Xmx512m"

# Command to run
ENTRYPOINT ["java", "-jar", "app.jar"]
