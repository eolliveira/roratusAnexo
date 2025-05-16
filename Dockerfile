FROM eclipse-temurin:21-jre-alpine
COPY ./target/RoratusHelpdeskService.jar /
WORKDIR /

RUN apk update && \
    apk add tzdata && \
    cp /usr/share/zoneinfo/America/Sao_Paulo /etc/localtime && \
    echo "America/Sao_Paulo" > /etc/timezone && \
    apk del tzdata

EXPOSE 8081
CMD java $JAVA_OPTS -jar roratusAnexo.jar