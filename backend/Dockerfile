FROM gradle:8.5-jdk17 as build

WORKDIR /app
COPY . /app/

RUN gradle build --no-daemon

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app
COPY --from=build /app/build/libs/*-all.jar app.jar

EXPOSE 8888

ENTRYPOINT ["java", "-jar", "app.jar"]