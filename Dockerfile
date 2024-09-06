FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/accomodation-service-0.0.1-SNAPSHOT.jar /app/
EXPOSE 8082
CMD ["java", "-jar", "accomodation-service-0.0.1-SNAPSHOT.jar"]