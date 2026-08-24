FROM eclipse-temurin:17.0.5_8-jre-focal
WORKDIR application

ADD ./target/gym-crm-system-1.0-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]