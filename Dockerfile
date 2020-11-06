# Stage 1
FROM openjdk:8-jdk-alpine AS BUILD

ENV APP_HOME=/usr/app

WORKDIR $APP_HOME

COPY . $APP_HOME

RUN ./gradlew clean build

# Stage 2
FROM openjdk:8-jre-alpine

ENV APP_HOME=/usr/app

COPY --from=BUILD  $APP_HOME/month-app/build/libs/month-app.jar /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]