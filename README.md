![Java](https://img.shields.io/badge/Java-21-blue)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.3-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)
![Docker](https://img.shields.io/badge/Dockerized-Yes-blue)

# 📦 Prueba Técnica Backend - Spring Boot

Este proyecto es una API REST desarrollada con **Spring Boot 3.5.3**, que gestiona órdenes de logística, conductores y asignaciones. Además, incluye autenticación mediante **JWT** y documentación con **Swagger UI**.

---

## 🚀 Tecnologías

- Java 21
- Spring Boot 3.5.3
- Spring Security
- JWT
- PostgreSQL
- Swagger OpenAPI
- Docker
- JUnit 5 + Mockito

---

## 🐳 Levantar el proyecto con Docker

### 1. Requisitos previos

- Docker & Docker Compose instalados
- WSL habilitado (en caso de usar Windows)

### 2. Clona el repositorio

```bash
git clone https://github.com/John17725/Prueba-Backend.git
cd Prueba-Backend
```

### 3. ⚙️ Variables de entorno (`docker-compose.yml`)

Es importante definir las credenciales deseadas, este archivo se encuentra en la raiz del proyecto

- `POSTGRES_DB`: Nombre de la base de datos deseada ejemplo: `dborders`
- `POSTGRES_USER`: Nombre de usuario ejemplo: `donchuy`
- `POSTGRES_PASSWORD`: Password a usar ejemplo: `password_application_order`

- `SPRING_DATASOURCE_URL`: Complementar el path con el nombre de la base de datos al final
- `SPRING_DATASOURCE_USERNAME`: Usar el mismo valor definido anteriormente en `POSTGRES_USER`
- `SPRING_DATASOURCE_PASSWORD`: Usar el mismo valor definido anteriormente en `POSTGRES_PASSWORD`

```yaml
POSTGRES_DB={dbName}
POSTGRES_USER={username}
POSTGRES_PASSWORD={password}

SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/{dbName}
SPRING_DATASOURCE_USERNAME={username}
SPRING_DATASOURCE_PASSWORD={password}
```

## ⚙️ Variables de entorno (`application.yml`)

⚠️ Asegurarse de definir las siguientes propiedades en `application.yml`:

Este archivo se encuentra en el siguiente directorio: `Prueba-Backend/orders/src/main/resources/application.yml`

# spring.cloud.aws

Valores de configuracion de aws

- `access-key`: Credencial de acceso de usuario aws.
- `secret-key`: Clave privada de autenticación de usuario aws.
- `bucket`: Nombre del contenedor S3.

# spring.security.user

Valores de configuracion de Spring Security

⚠️ Es importante definir un usuario y password para spring security ya que estos son usados para cargarlos en base de datos para poder realizar login y obtener un token JWT

- `name`: Nombre de usuario para Spring Security.
- `password`: Contraseña para Spring Security.

# spring.datasource

Valores de configuración para conexión a base de datos

- `url`: Complementar con el nombre de la base de datos definida anteriormente en `POSTGRES_DB` ejemplo: `jdbc:postgresql://postgres:5432/dborders`.
- `username`: Usar el mismo username definido en `POSTGRES_USER`.
- `password`: Usar la misma contraseña definida en `POSTGRES_PASSWORD`.

# jwt

Valores de configuración para la autenticación JWT

- `secret`: Cadena secreta usada para firmar y verificar los tokens JWT, usar al menos 32 caracteres alfanuméricos.
- `expiration`: Especifica cuánto tiempo (en milisegundos o segundos) es válido el token antes de expirar ejemplo: `86400000` tiene una duración de 1 día

```yaml
spring:
  cloud:
    aws:
      region:
        static: us-east-1
      credentials:
        access-key:
        secret-key:
      s3:
        bucket:
  security:
    user:
      name:
      password:
  datasource:
    url: jdbc:postgresql://postgres:5432/{dbName}
    username:
    password:
  ...

jwt:
  secret:
  expiration:

...
```

---

### 4. Ejecuta con Docker Compose

```bash
docker-compose up --build
```

Esto levantará dos contenedores:

- `postgres_db`: Base de datos PostgreSQL en puerto `5432`
- `orders_service`: API en el puerto `8080`

La API estará disponible en:  
📍 [http://localhost:8080](http://localhost:8080)

---

## 🔐 Autenticación

La autenticación se realiza mediante un endpoint de login:

```http
POST /auth/login
```

**Body**:

```json
{
  "username": "admin",
  "password": "secret123"
}
```

**Respuesta**:

```json
{
  "token": "Bearer eyJhbGciOiJIUzI1NiJ9..."
}
```

Este token debe ser enviado en el header `Authorization` para todas las peticiones seguras.

---

## 📚 Documentación Swagger

Una vez iniciado el proyecto, puedes acceder a la documentación completa desde:  
👉 [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## 📬 Colección de Postman

Para facilitar las pruebas de los endpoints, se incluye una colección de Postman con todas las operaciones disponibles (ordenes, conductores, asignaciones, login, etc).

🔗 Puedes importar la colección desde el siguiente archivo:

- [`Prueba-Backend.postman_collection.json`](./postman/Prueba-Backend.postman_collection.json)

### 🛠 ¿Cómo usarla?

1. Abre Postman.
2. Haz clic en "Import".
3. Selecciona el archivo `Prueba-Backend.postman_collection.json`.
4. Verifica que esté seleccionado el entorno local (`http://localhost:8080`).
5. Realiza login con el endpoint `/auth/login` y copia el token JWT.
6. Copia el valor del campo `token` que recibes en la respuesta.
7. En la colección (lado izquierdo de Postman), haz clic derecho sobre el nombre de la colección `Prueba` → Editar.
8. Ve a la pestaña Authorization.
9. Elige el tipo Bearer Token.
10. Pega el token JWT en el campo Token y guarda los cambios.

Ahora el token se usará automáticamente en todos los endpoints que requieren autenticación.

## ✅ Pruebas Unitarias

Puedes ejecutar las pruebas localmente con:

```bash
./mvnw test
```

---

## 📂 Estructura del proyecto

```
orders/
├── controller/          # Controladores REST
├── dto/                 # Objetos de transferencia
├── entity/              # Entidades JPA
├── exception/           # Manejo de excepciones
├── mapper/              # MapStruct mappers
├── payload/             # Respuestas estándar
├── repository/          # Interfaces de persistencia
├── security/            # JWT y filtros
├── service/             # Lógica de negocio
├── OrdersApplication.java
```
