# Stage: Build
FROM openjdk:8-jdk AS BUILD

WORKDIR /app

COPY . /app

RUN ./gradlew clean build

# Stage: Deploy
FROM openjdk:8-jre

COPY --from=BUILD /app/month-app/build/libs/month-app.jar /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]