# Base image with a slim JDK
FROM openjdk:17-slim AS builder

# Set working directory for building
WORKDIR /app

# Copy your Uber JAR file (replace "your-uber-jar.jar" with the actual name)
COPY your-uber-jar.jar .

# Build stage (optional, if your JAR dependencies need building)
# This stage builds the JAR and copies it to the final image
# Remove these lines if your JAR is pre-built
RUN ["mvn", "package"]  # Adjust command for your build tool (e.g., gradle)

# Final image (slim runtime)
FROM openjdk:17-jre-slim

# Set working directory for running the application
WORKDIR /app

# Copy the JAR file from the build stage (or directly if pre-built)
COPY --from=builder your-uber-jar.jar .

# Expose the port your application listens on (replace 8080 with your actual port)
EXPOSE 8080

# Command to run your JAR file
CMD ["java", "-jar", "your-uber-jar.jar"]
