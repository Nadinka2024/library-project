FROM amazoncorretto:21
WORKDIR /app
COPY target/library-project-2-0.0.1-SNAPSHOT.jar library-project.jar
ENTRYPOINT ["java", "-jar", "library-project.jar"]