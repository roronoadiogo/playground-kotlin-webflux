FROM openjdk:17-jdk-alpine as build

# directory of build o file java
WORKDIR /build
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY src src

# permission to execute
RUN chmod +x gradlew
# build the application
RUN ./gradlew bootJar

# only application built
FROM openjdk:17-jdk-alpine
WORKDIR /app

COPY --from=build /build/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]