#FROM openjdk:16-alpine
#RUN mkdir src
#COPY . /src
#WORKDIR /src
##RUN ./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=springio/gs-spring-boot-docker

FROM maven:3.8.5-openjdk-17-slim
WORKDIR /web-app
COPY . .
RUN mvn clean install
EXPOSE 8080
CMD mvn spring-boot:run