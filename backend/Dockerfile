FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw -B dependency:go-offline
COPY src/ src/
RUN ./mvnw -B -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
# Render injects PORT; server.port reads it (falls back to 8080 locally).
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
