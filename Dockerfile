FROM openjdk:17-jdk-slim
# Добавляем метаданные для образа
MAINTAINER Karen Petrosian
ADD target/CrudSpringBoot-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
