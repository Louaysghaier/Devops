FROM openjdk:17-jdk-alpine
EXPOSE 8082
ADD target/tpFoyer-17-0.0.5.jar tpFoyer-17-0.0.5.jar
ENTRYPOINT ["java","-jar","/tpFoyer-17-0.0.5.jar"]