FROM openjdk:17

WORKDIR /app

COPY target/property-management-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENV SPRING_DATASOURCE_URL=jdbc:mysql://54.225.47.98:3306/property_management?createDatabaseIfNotExist=true
ENV SPRING_DATASOURCE_USERNAME=root
ENV SPRING_DATASOURCE_PASSWORD=farruko67R%&?

ENTRYPOINT ["java", "-jar", "app.jar"]