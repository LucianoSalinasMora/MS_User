# MS_User - Microservicio de Autenticación y Gestión de Usuarios

Este microservicio forma parte del ecosistema de la plataforma académica **SigueTuCarrera**. Es el componente crítico encargado de centralizar la seguridad del sistema, gestionando el registro de usuarios, la autenticación mediante tokens JWT (JSON Web Tokens) y el control de accesos por roles (`ADMIN`, `ESTUDIANTE`, `DOCENTE`).

---

## Tecnologías y Dependencias Core

* **Java 17** (Eclipse Temurin)
* **Spring Boot 3.4.1**
* **Spring Security** (Protección de endpoints y filtros)
* **JJWT v0.12.6** (Librería para generación y validación de tokens JWT)
* **Spring Data JPA & MySQL Driver** (Persistencia de datos)
* **Springdoc OpenAPI v3** (Documentación interactiva de la API con Swagger)
* **Lombok** (Reducción de código boilerplate)

---

##  Arquitectura de Carpetas

El proyecto sigue una estructura limpia basada en capas funcionales:

```text
src/main/java/com/example/MS_User/
├── config/         # Configuración global (SwaggerConfig)
├── controller/     # Controladores REST expuestos
├── dto/            # Objetos de Transferencia de Datos (Login, Registro, Autenticación)
├── model/          # Entidades JPA (User, enumerado Role)
├── repository/     # Interfaces de acceso a datos (UserRepository)
├── security/       # Filtros, utilitarios JWT y configuraciones de Spring Security
└── service/        # Lógica de negocio y detalles de usuario
