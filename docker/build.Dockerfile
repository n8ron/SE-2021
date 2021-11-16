FROM gradle:6-jdk11-alpine AS plameet_build
WORKDIR /home/gradle/build
COPY . .
RUN gradle build -x check --no-daemon
