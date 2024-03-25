FROM openjdk:17.0.2-jdk-slim-buster
COPY build/libs/minio-service.jar minio-service.jar
ENTRYPOINT ["java", "-jar", "minio-service.jar"]