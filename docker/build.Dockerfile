FROM gradle:jdk11-alpine AS plameet_build
WORKDIR /home/gradle/build
ADD . .
RUN gradle build -x check --no-daemon
