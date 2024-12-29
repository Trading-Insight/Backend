FROM openjdk:17-alpine
COPY build/libs/*.jar app.jar
EXPOSE 80
ENTRYPOINT ["java","-Duser.timezone=Asia/Seoul","-jar","-Dspring.profiles.active=dev","/app.jar"]
