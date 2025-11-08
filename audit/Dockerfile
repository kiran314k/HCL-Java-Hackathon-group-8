# Use lightweight OpenJDK 17 image (Temurin)
FROM eclipse-temurin:17-jdk-jammy

# Set working directory
WORKDIR /app

# Copy the JAR (make sure it matches your Maven finalName)
COPY target/auditjar.jar auditjar.jar

# Expose Spring Boot port
EXPOSE 8080

# Optional environment variables
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS=""

# Run the JAR
ENTRYPOINT ["java","-jar","auditjar.jar"]
