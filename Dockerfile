FROM openjdk:8-jdk

WORKDIR /month
COPY . /month

RUN ./gradlew clean build

ENTRYPOINT ["java", "-jar", "month-app/build/libs/month-app.jar"]