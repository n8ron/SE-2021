FROM gradle:jdk11-alpine AS plameet_test
WORKDIR /home/gradle/build
ADD . .
RUN gradle test -x check --no-daemon
