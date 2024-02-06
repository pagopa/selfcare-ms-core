# syntax=docker/dockerfile:1.6

# FROM maven@sha256:dcfe7934d3780beda6e1f0f641bc7db3fa9b8818899981f6eddae052c78394c7
# ADD https://github.com/microsoft/ApplicationInsights-Java/releases/download/3.1.1/applicationinsights-agent-3.1.1.jar /applicationinsights-agent.jar
# VOLUME /tmp
# COPY target/*.jar app.jar
# ENTRYPOINT ["java","-jar","app.jar"]

FROM maven:3-eclipse-temurin-17 AS builder

COPY . .

RUN mvn clean package -DskipTests=true

FROM openjdk:17-jdk AS runtime

ENV LANG='en_US.UTF-8' LANGUAGE='en_US:en'

WORKDIR /app

COPY --from=builder ./target/*.jar ./app.jar

EXPOSE 8080
USER 1001

ENTRYPOINT ["java","-jar","app.jar"]
