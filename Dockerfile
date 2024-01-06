FROM adoptopenjdk:17-jre-hotspot

WORKDIR /app
COPY target/sua-aplicacao.jar /app

EXPOSE 8080

CMD ["java", "-jar", "sua-aplicacao.jar"]
