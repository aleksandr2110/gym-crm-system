

FROM amazoncorretto:17

ARG VERSION=1.0-SNAPSHOT
LABEL org.opencontainers.image.title="gym crm system"
LABEL org.opencontainers.image.version="${VERSION}"
LABEL org.opencontainers.image.description="gym crm Management System"

WORKDIR /app

COPY target/gym-crm-system-1.0-SNAPSHOT.jar app.jar

EXPOSE 8082

CMD ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]
