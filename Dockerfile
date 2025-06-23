FROM openjdk:11-jdk-slim
WORKDIR /app
COPY target/estore-microservice-1.0.0.jar /app
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "estore-microservice-1.0.0.jar"]