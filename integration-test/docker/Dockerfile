FROM openjdk:17-jdk-alpine
VOLUME /tmp
ADD integration-test-1.0.0.jar app.jar
EXPOSE 8081
EXPOSE 8082
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]