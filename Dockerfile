# Stage: Build
FROM openjdk:8-jdk-alpine AS BUILD

WORKDIR /app

COPY . /app

RUN ./gradlew clean build

# Stage: Deploy
FROM openjdk:8-jre-alpine

COPY --from=BUILD /app/month-app/build/libs/month-app.jar /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]