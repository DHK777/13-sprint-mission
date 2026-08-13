FROM amazoncorretto:17

RUN yum install -y findutils

WORKDIR /app

COPY . .

RUN chmod +x ./gradlew

RUN ./gradlew clean bootJar -x test

EXPOSE 80
ENV PROJECT_NAME=discodeit
ENV PROJECT_VERSION=1.2-M8

ENV JVM_OPTS=""

ENTRYPOINT ["sh", "-c", "java ${JVM_OPTS} -jar $(find build/libs -name '*.jar' ! -name '*-plain.jar')"]