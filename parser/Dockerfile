FROM openjdk:16-alpine
VOLUME /tmp
ARG JAR_FILE=target/parsing-api-repackage.jar
COPY ${JAR_FILE} parsing-api.jar
ENTRYPOINT ["java", "-jar", "/parsing-api.jar"]