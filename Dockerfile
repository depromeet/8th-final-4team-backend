FROM openjdk:8-jdk

WORKDIR /month-app
COPY ./month-app/build/libs/month-app.jar /month-app

ENTRYPOINT ["java", "-jar", "month-app.jar"]