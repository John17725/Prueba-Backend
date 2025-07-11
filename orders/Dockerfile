# Usa JDK 21
FROM eclipse-temurin:21-jdk

# Crea el directorio de trabajo
WORKDIR /app

# Copia el .jar generado por Maven
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]
