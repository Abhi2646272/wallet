# Stage 1: Build the application with Maven
FROM maven:3.8.4-openjdk-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and source code to the container
COPY pom.xml .
COPY src ./src

# Run the Maven build
RUN mvn clean package -DskipTests

# Stage 2: Create the final image
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the Spring Boot JAR file from the build stage
COPY --from=build /app/target/wallet*.jar app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8080

# Command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]