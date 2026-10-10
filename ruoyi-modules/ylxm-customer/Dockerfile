FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER ataoxz

RUN mkdir -p /ylxm/customer
RUN mkdir -p /ylxm/customer/logs
RUN mkdir -p /ylxm/customer/temp

WORKDIR /ylxm/customer

ENV SERVER_PORT=8061

EXPOSE ${SERVER_PORT}

ADD ./target/ylxm-customer.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dserver.port=${SERVER_PORT}", "-jar", "app.jar"]
