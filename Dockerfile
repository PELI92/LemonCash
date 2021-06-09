# Alpine Linux with OpenJDK JRE
FROM openjdk:16-alpine3.13
RUN apk add --no-cache bash

# copy Jar
COPY target/lemoncash-0.0.1-SNAPSHOT.jar /lemoncash.jar

COPY bin/sh/run.sh /run.sh

EXPOSE 3306

ENTRYPOINT ["/run.sh"]