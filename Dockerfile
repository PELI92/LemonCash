# Alpine Linux with OpenJDK JRE
FROM openjdk:16-alpine3.13

EXPOSE 8080

# copy Jar
COPY target/lemoncash-0.0.1-SNAPSHOT.jar /lemoncash.jar

COPY bin/sh/run.sh /run.sh

ENTRYPOINT "/run.sh" "$scope"