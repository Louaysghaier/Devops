FROM openjdk:17
WORKDIR /app
ADD target/tpFoyer-17-0.0.5.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]