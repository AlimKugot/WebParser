FROM openjdk:16-alpine
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} mail-sender-api.jar
ENTRYPOINT ["java", "-jar", "/mail-sender-api.jar"]