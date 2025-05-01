#!/bin/bash

# Espera a que Api Gateway esté disponible
echo "Esperando a que Gateway esté disponible en api-gateway:8080..."
until curl -s http://api-gateway:8080/ > /dev/null; do
  sleep 2
done

# Espera a que Keycloak esté disponible
echo "Esperando a que Keycloak esté disponible en keycloak-server:8080..."
until curl -s http://keycloak-server:8080/auth/ > /dev/null; do
  sleep 2
done

echo "Esperando a que Kafka esté disponible en kafka-server:9092..."
until nc -z -v -w30 kafka-server 9092; do
  echo "Esperando a que Kafka esté disponible..."
  sleep 2
done

# Lanza la aplicación Java
echo "Iniciando aplicación Spring Boot..."
exec java -jar app.jar