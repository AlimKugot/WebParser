FROM openjdk:16-alpine
ARG JAR_FILE=target/client-app-repackage.jar
COPY ${JAR_FILE} client-app-repackage.jar
ENTRYPOINT ["java", "-jar", "/client-app-repackage.jar"]