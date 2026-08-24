FROM eclipse-temurin:17.0.5_8-jre-focal
WORKDIR application
ADD ./target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]