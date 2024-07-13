# Use a Java base image
FROM openjdk:17-slim

# Copy your JAR file to the image
COPY Algo.main.jar /app.jar

# Set the working directory
WORKDIR /app

# Run the application using the JAR file
CMD ["java", "-jar", "app.jar"]
