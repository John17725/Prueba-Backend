# Etapa 1: Compilación con Maven y JDK 21
FROM maven:3.9.4-eclipse-temurin-21 AS builder

WORKDIR /build

# Copiar el wrapper y pom para aprovechar la cache de Docker si no hay cambios
COPY orders/mvnw orders/pom.xml ./orders/

# Copiar el resto del código
COPY orders ./orders

# Entrar al directorio del módulo y compilar el proyecto
WORKDIR /build/orders
RUN mvn clean verify && ls -la target/

# Etapa 2: Contenedor de runtime solo con JDK
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copiamos solo el JAR desde la etapa de compilación
COPY --from=builder /build/orders/target/orders-0.0.1-SNAPSHOT.jar app.jar

# Puerto expuesto
EXPOSE 8080

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]
