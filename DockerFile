# Use a Java base image
FROM openjdk:17-slim

# Copy your JAR file to the image
COPY Algo.main.jar /app.jar

# Set the working directory
WORKDIR /app

# Expose the port your application listens on (optional)
EXPOSE 8080  # Replace 8080 with your applications port

# Run the application using the JAR file
CMD ["java", "-jar", "app.jar"]
