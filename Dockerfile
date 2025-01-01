# Use a base image for Java
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the Spring Boot JAR file to the container
COPY target/wallet-app.jar app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8080

# Command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
