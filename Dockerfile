
FROM amazonlinux:2023 AS builder
WORKDIR /app

RUN yum update -y && yum install -y \
    tar gzip wget unzip shadow-utils java-17-amazon-corretto-devel findutils

ENV JAVA_HOME=/usr/lib/jvm/java-17-amazon-corretto
RUN export JAVA_HOME && java -version

COPY . /app

RUN chmod +x /app/gradlew

RUN JAVA_HOME=/usr/lib/jvm/java-17-amazon-corretto /app/gradlew clean bootJar --no-daemon

FROM amazoncorretto:17
WORKDIR /app

RUN java -version

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENV JAVA_HOME=/usr/lib/jvm/java-17-amazon-corretto
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
