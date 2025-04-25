# Etapa 1: Construcción con Maven
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: Imagen final solo con el JAR
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/inventory-service-0.0.1-SNAPSHOT.jar app.jar

# Exponer puerto y ejecutar la aplicación
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]