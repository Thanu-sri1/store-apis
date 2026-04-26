# FROM maven:3.8.4-openjdk-17-slim AS build
# WORKDIR /app
# COPY pom.xml .
# RUN mvn dependency:go-offline
# COPY src ./src
# RUN mvn package -DskipTests

# FROM openjdk:17-jdk-slim
# WORKDIR /app
# COPY --from=build /app/target/*.jar app.jar
# EXPOSE 8081
# ENTRYPOINT ["java", "-jar", "app.jar"]

# -------- Build Stage --------
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy only pom first (for caching dependencies)
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Copy source code
COPY src ./src

# Build jar
RUN mvn -B clean package -DskipTests


# -------- Run Stage --------
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# Copy built jar
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 8081

# Run application
ENTRYPOINT ["java","-jar","/app/app.jar"]
