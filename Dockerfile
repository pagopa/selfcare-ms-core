FROM maven:3-eclipse-temurin-17 AS builder

COPY . .

RUN mvn clean package -DskipTests=true

FROM openjdk:17-jdk AS runtime

ENV LANG='en_US.UTF-8' LANGUAGE='en_US:en'

WORKDIR /app

COPY --from=builder ./target/*.jar ./app.jar

EXPOSE 8080
USER 1001

ENTRYPOINT ["java", "-jar", "app.jar"]
