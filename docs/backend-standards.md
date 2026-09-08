---
description: Estándares de código, convenciones de nombres, estructura y mejores prácticas para proyectos Java Spring Boot
globs:
  - "src/main/java/**/*.java"
  - "src/main/resources/**"
  - "pom.xml"
alwaysApply: true
---

# Estándares de Código - Java Spring Boot

## Principios Fundamentales

- Clean Code
- SOLID
- DRY (Don't Repeat Yourself)
- KISS (Keep It Simple, Stupid)
- YAGNI (You Aren't Gonna Need It)
- Dependency Injection
- Separation of Concerns

## Reglas de Estilo

## Estilo
*   **Indentación:** Uso estricto de **Tabulaciones (Tabs)**. No utilizar espacios para indentar.
*   **Llaves:** Se sigue el estilo K&R, donde la llave de apertura de un bloque (`{`) se coloca en la misma línea que la declaración y la llave de cierre (`}`) en una nueva línea.
*   **Longitud de Línea:** Las líneas de código **DEBEN** ser máximo 120 caracteres.
*	**Finales de Línea (EOL):** Estándar de Linux (**LF** - `\n`).
*	**Codificación:** **UTF-8**
*   **Espacios en Blanco:**
    *   Se deben usar espacios alrededor de los operadores (`=`, `+`, `-`, `*`, `/`, `==`, `!=`, `>`, `<`, `>=`, `<=`, `&&`, `||`).
    *   Se debe usar un espacio después de las comas en las listas de parámetros, declaraciones de variables, etc.
    *   Se debe usar un espacio después de las palabras clave como `if`, `for`, `while`, `catch`.
## Nombres
### Clases e Interfaces
*   **Clases:** Deben utilizar `PascalCase` (cada palabra comienza con mayúscula, sin espacios) y ser sustantivos o frases sustantivas.
    *   Ejemplo: `UserService`, `OrderDto`, `PaymentController`.
*   **Interfaces:** Deben utilizar `PascalCase` y comenzar con la letra `I` seguida del nombre de la interfaz.
    *   Ejemplo: `IUserService`, `IPaymentGateway`.
*   **DTOs (Data Transfer Objects):** Deben terminar con el sufijo `Dto`.
    *   Ejemplo: `UserDto`, `OrderDto`, `PaymentRequestDto`.
*   **Implementaciones:** Las clases que implementan una interfaz **DEBEN** terminar con el sufijo `Impl`.
    *   Ejemplo: `UserServiceImpl`, `PaymentGatewayImpl`.
*   **Excepciones:** Deben terminar con el sufijo `Exception`.
    *   Ejemplo: `UserNotFoundException`, `PaymentException`.
### Funciones / Métodos
*   Deben utilizar `camelCase` (la primera palabra en minúscula, las siguientes palabras comienzan con mayúscula) y ser verbos o frases verbales que describan la acción que realiza el método.
    *   Ejemplo: `findUser()`, `processPayment()`, `validateOrder()`.
*   Los parámetros de los métodos también deben seguir `camelCase`.
    *   Ejemplo: `User user`, `String orderId`.
### Variables
*   Deben utilizar `camelCase`.
    *   Ejemplo: `userId`, `orderTotal`, `paymentMethod`.
### Constantes
*   Deben utilizar `UPPER_SNAKE_CASE` (todas las letras en mayúscula, palabras separadas por guiones bajos).
*   Se deben declarar como `public static final`.
*   Preferiblemente, agrupar las constantes en clases `final` con un constructor privado para evitar instanciación.
    *   Ejemplo: `MAX_RETRY_ATTEMPTS`, `DEFAULT_TIMEOUT_SECONDS`, `API_VERSION`.
## Estructura de Archivo
*   El proyecto sigue la estructura estándar de Maven/Spring Boot (`src/main/java`, `src/main/resources`).
*   Los paquetes deben organizarse de forma lógica por dominio y tipo de componente.

## Manejo de Errores
*   **Excepciones Personalizadas:** Se utilizan excepciones personalizadas que extienden `RuntimeException` para encapsular errores específicos de la aplicación.
    *   Ejemplo: `BusinessException`, `ValidationException`, `ResourceNotFoundException`.
*   **Bloques `try-catch`:** Se utilizan para manejar excepciones de forma controlada, especialmente en capas de servicio y controladores donde se interactúa con componentes que pueden lanzar excepciones.
*   **Logging de Errores:** Se utiliza el framework de logging (Lombok `@Slf4j` y SLF4J/Logback) para registrar mensajes de error, **DEBE** incluir la traza de la pila (`e`) cuando sea apropiado.
    *   Ejemplo: `log.error("Error al procesar solicitud para orderId={}", orderId, e);`.
*   **Respuestas Estandarizadas en Controladores:** Los controladores **DEBEN** devolver `ResponseEntity<T>` con los códigos de estado HTTP apropiados:
    *   `200 OK` - Operación exitosa
    *   `201 CREATED` - Recurso creado
    *   `400 BAD_REQUEST` - Error de validación
    *   `404 NOT_FOUND` - Recurso no encontrado
    *   `500 INTERNAL_SERVER_ERROR` - Error del servidor
*   **DTOs de Respuesta de Error:** Se utiliza un DTO de respuesta estandarizado para comunicar mensajes de error, códigos de error y timestamp cuando ocurre una excepción en la API.
    *   Ejemplo de estructura: `ErrorResponse` con campos `message`, `errorCode`, `timestamp`.
## Comentarios
*   **Encabezados de Archivo:** Los archivos pueden incluir un bloque de comentarios inicial con información de derechos de autor y licencia (opcional, según lo requiera la organización).
*   **Comentarios de Documentación (Javadoc):**
    *   **DEBE** documentar todas las clases e interfaces públicas.
    *   **DEBE** documentar todos los métodos públicos, incluyendo:
        *   Propósito del método
        *   `@param` para cada parámetro
        *   `@return` para valor de retorno
        *   `@throws` para excepciones lanzadas
    *   En los controladores REST, se utilizan anotaciones de Swagger/OpenAPI para documentar la API:
        *   `@Tag` - Agrupar endpoints relacionados
        *   `@Operation` - Describir la operación
        *   `@ApiResponses` - Documentar respuestas posibles
*   **Comentarios en Línea:** Se deben usar **SOLO** cuando la lógica no sea evidente del código mismo. Preferir código auto-explicativo sobre comentarios.
*   **Evitar Código Comentado:** **NO** dejar grandes bloques de código comentado que no estén en uso. Si el código no es necesario, debe eliminarse; el control de versiones (Git) puede recuperarlo si es necesario.
*   **TODOs:** Si se utilizan comentarios `//TODO`, deben incluir contexto claro y, preferiblemente, crear un issue en el sistema de seguimiento del proyecto.


## Reglas Específicas de Spring Boot

### Inyección de Dependencias
*   **DEBE** usar constructor injection (nunca field injection con `@Autowired`).
*   Ejemplo:
    ```java
    @Service
    public class UserServiceImpl implements IUserService {
        private final UserRepository userRepository;
        
        public UserServiceImpl(UserRepository userRepository) {
            this.userRepository = userRepository;
        }
    }
    ```

### Controllers
*   **DEBE** ser delgados (thin controllers).
*   **NO** deben contener lógica de negocio.
*   **NO** deben acceder directamente a repositories.
*   Delegar toda lógica a services.

### Services
*   Contienen la lógica de negocio.
*   Son transaccionales (`@Transactional` cuando sea necesario).
*   Dependen de repositories para persistencia.
*   Implementan interfaces (patrón Strategy/Dependency Inversion).

### Repositories
*   Extender `JpaRepository` o `CrudRepository`.
*   Solo query methods derivados o `@Query` personalizados.
*   **NO** deben contener lógica de negocio.

### Configuración
*   Usar clases anotadas con `@Configuration` para beans de configuración.
*   Externalizar configuraciones en `application.properties` o `application.yml`.
*   Usar `@Value` o `@ConfigurationProperties` para inyectar propiedades.

## Control de Versiones

### Commits
*   Mensajes claros y descriptivos.
*   Formato recomendado: `tipo: descripción breve`
*   Tipos: `feat`, `fix`, `refactor`, `test`, `docs`, `chore`

### Branches
*   `main`/`master`: código en producción
*   `develop`: integración de features
*   `feature/nombre-feature`: desarrollo de nuevas features
*   `bugfix/nombre-bug`: corrección de bugs

### Code Review
*   Todo código **DEBE** pasar por revisión antes de merge.
*   Verificar adherencia a estos estándares.
*   Validar pruebas unitarias e integración.
