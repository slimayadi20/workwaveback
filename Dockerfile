# Use an OpenJDK Runtime as a parent image
FROM openjdk:8-jre-alpine

# Define environment variables
ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS JAVA_OPTS=""

# Set the working directory to /app
WORKDIR /app

# Copy the executable into the container at /app
COPY target/*.jar app.jar

# Make port 8090 available to the world outside this container
EXPOSE 8090

# Run app.jar when the container launches
CMD ["java", "-jar", "app.jar"]
