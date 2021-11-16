FROM gradle:6-jdk11-alpine AS plameet_test
WORKDIR /home/gradle/build
COPY . .
RUN gradle test -x check --no-daemon
