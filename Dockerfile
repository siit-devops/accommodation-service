FROM openjdk:17-jdk-slim
WORKDIR /app
COPY wait-for-it.sh /app/wait-for-it.sh
COPY target/accomodation-service-0.0.1-SNAPSHOT.jar /app/accomodation-service-0.0.1-SNAPSHOT.jar
RUN chmod +x /app/wait-for-it.sh
EXPOSE 8080
CMD ["/app/wait-for-it.sh", "db:5432", "--", "java", "-jar", "accomodation-service-0.0.1-SNAPSHOT.jar"]