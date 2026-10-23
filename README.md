# API de Constancias de Estudios

API REST con Spring Boot para tramitar constancias de estudios. Una sola solicitud valida al estudiante, registra el pago, genera el PDF de la constancia y envía la notificación por correo. Todo corre dentro de una misma transacción: si falla cualquier paso, se hace rollback.

> La generación del PDF y el envío del correo (SMTP) están **simulados**: el PDF solo devuelve una ruta y el correo se guarda como notificación en la base de datos.

## Integrantes

- Angel David De Grnada Santos
- Maria Fernanda León Díaz 

## Descripción de la solución

### Descomposición

| Actividad | Descripción | Operación candidata |
|---|---|---|
| 1 | Recibir la solicitud de la constancia por parte del estudiante | `solicitarConstancia` |
| 2 | Verificar que el estudiante se encuentre inscrito y activo | `verificarInscripcion` |
| 3 | Realizar y/o verificar el cobro del trámite | `cobrarTramite` |
| 4 | Generar el documento PDF de la constancia de estudios | `generarDocumentoPDF` |
| 5 | Enviar la constancia generada por correo electrónico al estudiante | `enviarCorreo` |

### Servicios candidatos

| Tipo | Servicio | Dominio | Operación | Justificación | Implementación |
|---|---|---|---|---|---|
| Tarea | TramiteConstancia | Académico | `solicitarConstancia` | Coordina y orquesta el flujo de pasos para completar la solicitud | `TramiteConstanciaService` |
| Entidad | Estudiante | Académico | `verificarInscripcion` | Gestiona la lógica e información de la entidad Estudiante | `EstudianteService` |
| Entidad | Pago | Finanzas | `cobrarTramite` | Administra los datos y la lógica de cobros y transacciones | `PagoService` |
| Utilidad | GeneradorDocumento | Servicios Técnicos | `generarDocumentoPDF` | Función técnica reutilizable para crear archivos PDF | `PdfGeneratorService` |
| Utilidad | Notificación | Comunicación | `enviarCorreo` | Tareas de comunicación reutilizables por distintas áreas de la universidad | `NotificacionService` |

### Matriz de trazabilidad

| Actividad | TramiteConstancia (Tarea) | Estudiante (Entidad) | Pago (Entidad) | GeneradorDocumento (Utilidad) | Notificación (Utilidad) |
|---|---|---|---|---|---|
| Solicitar constancia | `solicitarConstancia` | | | | |
| Verificar inscripción | | `verificarInscripcion` | | | |
| Cobrar trámite | | | `cobrarTramite` | | |
| Generar PDF | | | | `generarDocumentoPDF` | |
| Enviar por correo | | | | | `enviarCorreo` |

## Tecnologías

- Java 21
- Spring Boot 4.1.1 (Web MVC y Data JPA)
- MySQL 8 (`mysql-connector-j`)
- Maven (incluye el wrapper `mvnw`)

## Estructura

```
src/main/java/com/api/constancia
├── controllers/    Endpoints REST
├── models/         Entidades JPA (Estudiante, Pago, SolicitudConstancia, Notificacion)
├── repositories/   Repositorios Spring Data
└── services/       Lógica de negocio (validación, pago, PDF, notificación, trámite)
```

## Modelo de datos

| Tabla | Llave | Campos principales |
|---|---|---|
| `estudiante` | `matricula` (9 caracteres) | `nombre`, `correo`, `activo` |
| `pago` | `id` (autoincremental) | `monto`, `fecha_pago`, `estatus`, `matricula_estudiante` |
| `solicitud_constancia` | `folio` (autoincremental) | `fecha_solicitud`, `estatus`, `ruta_pdf`, `matricula_estudiante`, `pago_id` |
| `notificacion` | `id` (autoincremental) | `asunto`, `mensaje`, `fecha_envio`, `enviado`, `matricula_estudiante` |

Hibernate crea las tablas solo (`spring.jpa.hibernate.ddl-auto=update`).

## Configuración

Edita [src/main/resources/application.properties](src/main/resources/application.properties) con tus credenciales de MySQL:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/constancia?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=TU_PASSWORD
```

La base de datos `constancia` se crea sola si no existe.

## Ejecución

```bash
# Windows
mvnw.cmd spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```

La API queda en `http://localhost:8080`.

## Datos de prueba

La API no tiene endpoint para dar de alta estudiantes. Después de arrancar la aplicación una vez (para que se creen las tablas), ejecuta esto en MySQL:

```sql
USE constancia;

INSERT INTO estudiante (matricula, nombre, correo, activo) VALUES
  ('202400001', 'Ana López García',   'ana.lopez@correo.com',   true),
  ('202400002', 'Carlos Pérez Ruiz',  'carlos.perez@correo.com', false),
  ('202400003', 'María Torres Díaz',  'correo-invalido',         true);
```

| Matrícula | Caso de prueba |
|---|---|
| `202400001` | Estudiante activo: el flujo completo funciona |
| `202400002` | Estudiante inactivo: se rechaza |
| `202400003` | Correo sin `@`: falla al enviar la notificación y se hace rollback |

## Endpoints

Todos los parámetros se envían como **query params** (no como body JSON).

### Estudiantes

**`GET /api/estudiantes/{matricula}/verificar`**: verifica que el estudiante exista y esté activo.

- `200 OK`: devuelve el estudiante.
- `400 Bad Request`: la matrícula no tiene 9 caracteres, o el estudiante no existe o está inactivo (mensaje en texto plano).

```json
{
  "matricula": "202400001",
  "nombre": "Ana López García",
  "correo": "ana.lopez@correo.com",
  "activo": true
}
```

### Pagos

**`POST /api/pagos/procesar?matricula={matricula}&monto={monto}`**: registra un pago directo.

- El costo mínimo del trámite es **$150.00**.
- `200 OK`: devuelve el pago con estatus `PAGADO`.
- `400 Bad Request`: estudiante no válido o monto insuficiente.

### Trámite de constancia

**`POST /api/tramites/constancia/solicitar?matricula={matricula}&monto={monto}`**: ejecuta el flujo completo:

1. Valida al estudiante (matrícula de 9 caracteres, existe y está activo).
2. Procesa el pago (monto ≥ $150.00).
3. Crea la solicitud con estatus `PROCESANDO_DOCUMENTO`.
4. Genera el PDF (`/documentos/constancias/constancia_{folio}_{matricula}.pdf`).
5. Envía la notificación por correo.
6. Marca la solicitud como `ENVIADO_Y_COMPLETADO`.

Respuestas:

- `201 Created`: devuelve la solicitud completa con su pago y estudiante.
- `400 Bad Request`: error de validación (`IllegalArgumentException`): matrícula mal formada, monto insuficiente o correo inválido.
  ```json
  { "error": "Validación fallida", "mensaje": "El monto proporcionado ($100.0) es insuficiente. El costo del trámite es de $150.0" }
  ```
- `500 Internal Server Error`: cualquier otro error, incluido un estudiante inexistente o inactivo.
  ```json
  { "error": "Error en el procesamiento de la constancia", "detalle": "El estudiante con matrícula 999999999 no fue encontrado o no se encuentra activo/inscrito." }
  ```

### Notificaciones

**`GET /api/notificaciones/estudiante/{matricula}`**: historial de notificaciones enviadas a un estudiante.

- `200 OK`: lista de notificaciones (lista vacía si no hay).

## Pruebas con Postman

Importa [constancia.postman_collection.json](constancia.postman_collection.json) en Postman (**Import → File**). La colección trae:

- Variables `baseUrl` (`http://localhost:8080`), `matricula`, `matriculaInactiva`, `matriculaCorreoInvalido` y `matriculaInexistente`.
- Peticiones para todos los endpoints, con casos de éxito y de error.
- Tests automáticos que revisan el código de estado y los campos clave de cada respuesta.

Para correr toda la colección: clic derecho en la colección → **Run collection**. Antes, carga los [datos de prueba](#datos-de-prueba).
