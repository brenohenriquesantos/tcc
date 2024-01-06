FROM openjdk:17-jdk-alpine

WORKDIR /app
COPY target/tcc-0.0.1-SNAPSHOT.jar /app

EXPOSE 8080

CMD ["java", "-jar", "tcc-0.0.1-SNAPSHOT.jar"]
