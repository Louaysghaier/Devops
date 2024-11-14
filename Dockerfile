FROM openjdk:17-jdk-alpine
WORKDIR /yacineJar
EXPOSE 8082
COPY target/*.jar /yacineJar.jar
ENTRYPOINT ["java","-jar","/yacineJar.jar"]

