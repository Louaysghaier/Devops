FROM openjdk:17
WORKDIR /app
ADD target/tp-foyer-0.0.5.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]