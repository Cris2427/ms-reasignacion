# ms-reasignacion

Microservicio de **Reasignación Automática de Citas** del sistema **Servicio de Salud RedNorte**.

Cuando una cita médica se cancela, este microservicio consulta la lista de espera (mediante **OpenFeign** hacia `ms-listaEspera`) y **reasigna automáticamente** la hora liberada al siguiente paciente prioritario, registrando el resultado del proceso (COMPLETADA, FALLIDA, etc.).

Forma parte de una arquitectura de microservicios:

```
ms-front (React) -> bff-gateway -> ms-reasignacion --(Feign)--> ms-listaEspera
```

---

## Stack tecnológico

| Tecnología | Versión / Detalle |
| --- | --- |
| Java | 21 |
| Spring Boot | 4.0.6 |
| Build | Maven (con wrapper `mvnw`) |
| Persistencia | Spring Data JPA |
| Base de datos | MySQL (H2 en memoria para los tests) |
| Comunicación entre servicios | Spring Cloud OpenFeign |
| Utilidades | Lombok |
| Documentación API | springdoc / Swagger UI |
| Cobertura de pruebas | JaCoCo |

---

## Requisitos previos

- **JDK 21** instalado.
- **MySQL** corriendo en `localhost:3306` (ej. XAMPP), usuario `root` y sin contraseña. La base de datos `db_reasignacion` se crea automáticamente al iniciar.
- Para el **flujo completo** de reasignación, `ms-listaEspera` debe estar corriendo en el puerto `8082`.
- No es necesario instalar Maven: el proyecto incluye el wrapper `mvnw`.

---

## Configuración

La configuración está en `src/main/resources/application.properties`:

- Puerto: **8084**
- Base de datos: `jdbc:mysql://localhost:3306/db_reasignacion`
- URL del servicio de lista de espera: `ms.lista-espera.url=http://localhost:8082`

Para los **tests** se usa un perfil separado (`src/test/resources/application-test.properties`) con **H2 en memoria**, por lo que las pruebas **no requieren MySQL**.

---

## Cómo ejecutar

```bash
# Construir
./mvnw.cmd clean install

# Ejecutar
./mvnw.cmd spring-boot:run
```

El microservicio queda disponible en `http://localhost:8084`.

---

## API REST

Ruta base: `/api/v1/reasignaciones`

| Método | Endpoint | Descripción | Respuesta |
| --- | --- | --- | --- |
| `POST` | `/api/v1/reasignaciones` | Inicia la reasignación de una cita cancelada | 201 Created |
| `GET` | `/api/v1/reasignaciones` | Lista todas las reasignaciones | 200 OK |
| `GET` | `/api/v1/reasignaciones/{id}` | Obtiene una reasignación por id | 200 OK / 404 |
| `GET` | `/api/v1/reasignaciones/estado/{estado}` | Filtra reasignaciones por estado | 200 OK / 400 |

Estados posibles: `PENDIENTE`, `COMPLETADA`, `FALLIDA`, `CANCELADA`.

### Documentación interactiva (Swagger)

Con el servicio corriendo: **http://localhost:8084/swagger-ui.html**

### Manejo de errores

| Código | Situación |
| --- | --- |
| `400` | Datos inválidos (validación `@Valid`) |
| `404` | Cita, paciente o reasignación no encontrada |
| `500` | Error interno del servidor |

---

## Pruebas y cobertura

```bash
# Ejecutar todas las pruebas unitarias
./mvnw.cmd test
```

Las pruebas usan **JUnit 5 + Mockito** (sin base de datos real, gracias a H2 + perfil `test`).

Reporte de **cobertura (JaCoCo)**:

```
target/site/jacoco/index.html
```

Cobertura actual: **~94% de líneas** (sobre el mínimo exigido del 60%), con 29 pruebas.

---

## Estructura del proyecto

```
src/main/java/com/rednorte/ms_reasignacion/
├── model/         -> Entidades JPA (Reasignacion, Cita, Paciente)
├── repository/    -> Repositorios JPA (Repository Pattern)
├── dto/           -> DTOs de entrada/salida e integración
├── mapper/        -> ReasignacionMapper (entidad <-> DTO)
├── client/        -> ListaEsperaClient (Feign, comunicación con ms-listaEspera)
├── service/       -> ReasignacionService (interfaz) + ReasignacionServiceImpl
├── controller/    -> ReasignacionController (endpoints REST)
├── exception/     -> Excepciones propias + GlobalExceptionHandler + ErrorResponseDTO
└── config/        -> OpenApiConfig (Swagger)
```

## Patrones y comunicación

- **Repository Pattern** — acceso a datos desacoplado mediante interfaces `JpaRepository`.
- **Comunicación inter-servicios con OpenFeign** — `ListaEsperaClient` consulta el siguiente paciente prioritario de `ms-listaEspera` de forma declarativa.

---

## Equipo

Proyecto académico — Desarrollo Fullstack III (DSY1106), Duoc UC.
Responsable del microservicio: **Cristian T.**
