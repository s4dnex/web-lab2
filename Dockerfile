FROM gradle:9.0.0 AS build
WORKDIR /workspace
COPY gradle/ gradle/
COPY libs/ libs/
COPY src/main src/main
COPY build.gradle.kts settings.gradle.kts ./
RUN gradle clean build --no-daemon

FROM eclipse-temurin:21
EXPOSE 52000
RUN mkdir /app
WORKDIR /app
COPY --from=build /workspace/build/libs/web-lab1.jar /app/app.jar
ENTRYPOINT ["java", "-DFCGI_PORT=52000", "-jar", "app.jar"]