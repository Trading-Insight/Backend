FROM eclipse-temurin:21-jre
WORKDIR /app

COPY Tradin-Api/build/libs/*.jar app.jar

ENV SPRING_PROFILES_ACTIVE=dev

ENTRYPOINT ["java", "-jar", "app.jar"]