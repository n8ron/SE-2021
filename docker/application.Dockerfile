FROM gradle:6-jdk11-alpine AS plameet_build
WORKDIR /home/gradle
COPY . .
RUN gradle build -x check --no-daemon

FROM openjdk:11-jre-slim
COPY --from=plameet_build /home/gradle/build/libs/*.jar my-cool-app/app.jar
ENTRYPOINT ["java", "-jar", "my-cool-app/app.jar"]
