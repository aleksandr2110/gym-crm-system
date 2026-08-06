FROM maven:3.9.8-amazoncorretto-17 AS build
WORKDIR /build/

COPY pom.xml ./
COPY .mvn .mvn

COPY src ./src

RUN mvn clean package -e -DskipTests

FROM amazoncorretto:17

ARG VERSION=1.0-SNAPSHOT
LABEL org.opencontainers.image.title="gym crm system"
LABEL org.opencontainers.image.version="${VERSION}"
LABEL org.opencontainers.image.description="gym crm Management System"

WORKDIR /app/

COPY --from=build /build/target/gym-crm-system*.jar ./gym-crm-system.jar

EXPOSE 8082

CMD ["java", "-jar", "-cp", "gym-crm-system.jar", "epam.GymApplication", "--spring.profiles.active=prod"]
