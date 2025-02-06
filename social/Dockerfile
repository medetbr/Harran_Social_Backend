# Base image with OpenJDK 17 (Java 17)
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Install Maven
RUN apt-get update && apt-get install -y maven

# Copy the project files to the container
COPY . .

# Make sure mvnw (Maven Wrapper) is executable (optional if using Maven wrapper)
RUN chmod +x mvnw

# Build the project using Maven Wrapper
RUN ./mvnw clean package

# Expose the port your application will run on (default Spring Boot port is 8080)
EXPOSE 8080

# Command to run the app
CMD ["java", "-jar", "target/social-0.0.1-SNAPSHOT.jar"]
