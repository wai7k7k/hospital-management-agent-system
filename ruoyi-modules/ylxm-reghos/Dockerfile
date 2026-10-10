FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER ataoxz

RUN mkdir -p /ylxm/reghos
RUN mkdir -p /ylxm/reghos/logs
RUN mkdir -p /ylxm/reghos/temp

WORKDIR /ylxm/reghos

ENV SERVER_PORT=8071

EXPOSE ${SERVER_PORT}

ADD ./target/ylxm-reghos.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dserver.port=${SERVER_PORT}", "-jar", "app.jar"]
