# Veterinary Backend (Spring Boot)

Backend sencillo en Spring Boot para una pequeña aplicación veterinaria con interfaz Thymeleaf y APIs REST protegidas por JWT.

Tecnologías: Spring Boot, Spring Security, Spring Data JPA (Hibernate), Thymeleaf, jjwt, MySQL (Docker)

Inicio rápido

1) Iniciar MySQL con Docker (hay un Dockerfile en `docker/mysql/Dockerfile`)

- Construir la imagen (opcional):

```bash
docker build -t my-mysql:latest -f docker/mysql/Dockerfile docker/mysql
```

- Ejecutar el contenedor (ejemplo):

```bash
docker run --name my-mysql -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=password \
  -e MYSQL_DATABASE=mydatabase \
  -e MYSQL_USER=myuser \
  -e MYSQL_PASSWORD=password \
  my-mysql:latest
```

Si necesitas crear manualmente la base de datos y el usuario desde el cliente MySQL (por ejemplo con `docker exec -it my-mysql mysql -u root -p` e ingresar el password), puedes ejecutar:

```sql
CREATE DATABASE IF NOT EXISTS mydatabase; \
CREATE USER IF NOT EXISTS 'myuser'@'%' IDENTIFIED BY 'password'; \
GRANT ALL PRIVILEGES ON mydatabase.* TO 'myuser'@'%'; \
FLUSH PRIVILEGES;
```

2) Configurar la aplicación

Edita `src/main/resources/application.properties` para indicar la base de datos. Ejemplo usado en este proyecto:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/mydatabase?useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.username=myuser
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

3) Compilar y ejecutar

```bash
./mvnw -DskipTests package
./mvnw spring-boot:run
```

4) Uso

- Interfaz (Thymeleaf): http://localhost:8080/
  - Inicio: `/` (plantilla: `templates/home.html`)
  - Login: `/login` (plantilla: `templates/auth/login.html`)
  - Pacientes: `/patients` (plantillas en `templates/patients/`)
  - Citas: `/appointments` (plantillas en `templates/appointments/`)

- APIs REST (protegidas por token):
  - `POST /login` — recibe campos de formulario `username` y `password`. Devuelve en el cuerpo la cadena `Bearer <token>` y además crea una sesión en el servidor para las vistas Thymeleaf.
  - `GET /api/patients` — listar pacientes (requiere autenticación o token)
  - `POST /api/patients` — crear paciente (requiere rol USER/VET/ADMIN)
  - `GET /api/appointments` — listar citas
  - `POST /api/appointments` — crear cita

Estructura del proyecto (resumen)

- Paquetes Java principales:
  - `com.example.backend.model` — entidades JPA (`User`, `Patient`, `Appointment`)
  - `com.example.backend.controller` — controladores web y REST
  - `com.example.backend.auth` — utilidades JWT y filtro de autorización
  - `com.example.backend.repository` — repositorios Spring Data
  - `com.example.backend.config` — configuración de seguridad

- Plantillas: organizadas en `src/main/resources/templates/`:
  - `auth/` (login)
  - `patients/` (list, form)
  - `appointments/` (list, form)
  - `home.html` permanece en la raíz de `templates`

- Recursos estáticos: `src/main/resources/static/css/` (estilos)

