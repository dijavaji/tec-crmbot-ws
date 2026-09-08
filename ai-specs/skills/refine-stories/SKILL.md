---
name: "refine-stories"
description: "Refina historias de usuario usando código fuente como contexto. Analiza la implementación para identificar criterios de aceptación faltantes, casos borde y oportunidades de mejora. Usar cuando se quiera mejorar user stories basándose en código existente."
license: "MIT"
compatibility: "Compatible con todos los agentes de IA (Kiro, Claude, Cursor, Codex, Gemini, OpenCode). Requiere acceso a feature_list.json y documentación del proyecto."
metadata:
  author: "technoloqie"
  version: "1.0.0"
  created: "2026-09-25"
  framework: "sdd-openspec"
  project-type: "java-spring-boot"
allowed-tools: "Read Write"
---

# Refine Stories Skill

## Overview

Esta skill transforma historias de usuario de alto nivel en especificaciones técnicas detalladas y listas para implementación, adecuadas para flujos de trabajo de **Specification-Driven Development (SDD)**.

Analiza el código fuente existente, la arquitectura del proyecto y las mejores prácticas documentadas para enriquecer historias con:
- Descripción funcional completa
- Criterios de aceptación explícitos
- Especificaciones de API (endpoints, schemas)
- Cambios en base de datos
- Archivos a modificar según arquitectura
- Requisitos no funcionales (seguridad, rendimiento)
- Requisitos de testing (unit, integration)
- Actualizaciones de documentación

**Cuándo usar esta skill:**
- Convertir requisitos de producto en especificaciones técnicas
- Preparar historias para generación de specs (requirements.md → design.md → tasks.md)
- Asegurar que las historias tienen suficiente detalle para implementación autónoma
- Transición de estado `pending` a `history_ready` en el flujo SDD

## Instrucciones

Por favor, analice y refine el ticket de historia de usuario: **$ARGUMENTS**

### Paso 1: Obtener detalles del ticket

Usa el archivo `feature_list.json` para obtener los detalles del ticket.

**Parámetro de entrada:** Puede ser:
- ID/número del ticket (ejemplo: `US-01`, `FEAT-123`)
- Palabra clave que identifique el ticket (ejemplo: `"authentication"`)
- Estado del ticket (ejemplo: `"pending"` para obtener el primer ticket pendiente)

**Estructura de feature_list.json:**
```json
{
  "features": [
    {
      "id": "US-01",
      "name": "User Authentication",
      "description": "Implement JWT-based authentication",
      "status": "pending",
      "sdd": true
    }
  ]
}
```

**Acción:** Leer `feature_list.json` y extraer:
- `id`: Identificador del ticket
- `name`: Nombre de la feature
- `description`: Descripción actual
- `status`: Estado actual
- `sdd`: Si requiere proceso SDD

**Validaciones:**
- Si el ticket no se encuentra, informar al usuario
- Si el ticket ya está en estado `history_ready`, informar que ya fue refinado
- Si `sdd: false`, esta skill no es necesaria

### Paso 2: Actuar como experto en producto con conocimientos técnicos

**Rol:** Experto en producto + Arquitecto de software

**Conocimientos requeridos:**
- Mejores prácticas de desarrollo Spring Boot
- Arquitectura del proyecto (capas, separación de responsabilidades)
- Estándares de código del equipo
- Patrones de diseño aplicables
- Estrategias de testing

### Paso 3: Comprender el problema descrito

**Análisis del ticket:**
1. ¿Qué problema de negocio resuelve?
2. ¿Quién es el usuario objetivo?
3. ¿Cuál es el flujo de usuario esperado?
4. ¿Qué funcionalidad específica se requiere?
5. ¿Hay dependencias con otras features?

**Contexto técnico a revisar:**

Lee los siguientes archivos de documentación:
- **`feature_list.json`** - Lista de features del proyecto
- **`docs/backend-standards.md`** - Estándares de código, convenciones, testing
- **`docs/base-standards.md`** - Mapa de navegación del proyecto
- **`docs/structure.md`** (si existe) - Arquitectura, paquetes, flujo de dependencias
- **`docs/tech.md`** (si existe) - Stack tecnológico, frameworks, librerías
- **`docs/product.md`** (si existe) - Contexto del producto, usuarios, objetivos
- **`docs/data-model.md`** (si existe) - Modelo de datos, entidades, relaciones

### Paso 4: Evaluar completitud de la historia

**Criterios de calidad para historias de usuario:**

La historia DEBE incluir:

#### ✅ Descripción Funcional Completa
- [ ] Descripción clara de la funcionalidad (mínimo 200 palabras)
- [ ] Flujo de interacción del usuario paso a paso
- [ ] Comportamiento del sistema esperado

#### ✅ Lista Exhaustiva de Campos/Datos
- [ ] Todos los campos de entrada identificados (nombre, tipo, validaciones)
- [ ] Todos los campos de salida especificados
- [ ] Reglas de negocio para cada campo

#### ✅ Estructura y URLs de Endpoints
- [ ] Método HTTP (GET, POST, PUT, DELETE, PATCH)
- [ ] Path del endpoint (`/api/v1/resource`)
- [ ] Parámetros de path, query, headers
- [ ] Schema de request body (con ejemplo JSON)
- [ ] Schema de response body (con ejemplo JSON)
- [ ] Códigos HTTP de respuesta (éxito y error)
- [ ] Requisitos de autenticación/autorización

#### ✅ Archivos a Modificar según Arquitectura
- [ ] Controllers (REST endpoints)
- [ ] Service interfaces
- [ ] Service implementations
- [ ] Repositories (JPA/JDBC)
- [ ] DTOs (Data Transfer Objects)
- [ ] Entities (JPA models)
- [ ] Custom Exceptions
- [ ] Configuration classes (si aplica)
- [ ] Archivos de configuración (application.properties, pom.xml)

#### ✅ Pasos para Completitud
- [ ] Definición de "Done" clara
- [ ] Criterios de aceptación verificables
- [ ] Casos de éxito especificados
- [ ] Casos de error especificados
- [ ] Casos borde identificados

#### ✅ Actualizaciones de Documentación
- [ ] Documentación de API (Swagger/OpenAPI)
- [ ] README del proyecto (si aplica)
- [ ] Guías de usuario (si aplica)
- [ ] Diagramas de arquitectura (si aplica)

#### ✅ Pruebas Unitarias
- [ ] Tests de Controllers (endpoints)
- [ ] Tests de Services (lógica de negocio)
- [ ] Tests de Repositories (queries)
- [ ] Tests de validación de DTOs
- [ ] Cobertura esperada (ej. > 80%)

#### ✅ Requisitos No Funcionales
- [ ] **Seguridad:** Autenticación, autorización, validación de entrada, prevención de inyección SQL/XSS
- [ ] **Rendimiento:** Tiempos de respuesta esperados (ej. < 200ms p95)
- [ ] **Escalabilidad:** Diseño stateless, pooling de conexiones
- [ ] **Confiabilidad:** Manejo de errores, reintentos, circuit breakers
- [ ] **Observabilidad:** Logging, métricas, health checks

**Decisión:** ¿La historia tiene todos estos elementos?
- **SÍ** → Pasar al Paso 8 sin modificaciones
- **NO** → Continuar al Paso 5 para enriquecerla

### Paso 5: Proporcionar historia enriquecida

Si la historia carece de detalles técnicos necesarios, crea una versión enriquecida.

**Formato de la historia enriquecida (Markdown):**

```markdown
# [ID] - [Nombre de la Feature]

## Descripción Original
[Transcripción textual de la descripción original del ticket]

## Descripción Funcional Enriquecida

[Descripción detallada de 200-500 palabras que incluya:]
- **Funcionalidad principal:** Qué hace la feature
- **Flujo de usuario:** Pasos que sigue el usuario
- **Comportamiento del sistema:** Cómo responde el sistema
- **Valor de negocio:** Por qué es importante esta feature

## Criterios de Aceptación

[Lista numerada de criterios verificables usando formato EARS cuando sea posible]

### Escenarios de Éxito
1. **CUANDO** [condición] **ENTONCES** el sistema **DEBE** [comportamiento]
2. [Más criterios...]

### Escenarios de Error
1. **CUANDO** [condición de error] **ENTONCES** el sistema **DEBE** [manejo del error]
2. [Más criterios...]

### Casos Borde
1. [Casos límite identificados]
2. [Más casos...]

## Especificación de API

### Endpoint 1: [Nombre Descriptivo]
- **Método:** POST
- **Path:** `/api/v1/recurso`
- **Autenticación:** Requerida (JWT)
- **Request Body:**
  ```json
  {
    "campo1": "string",
    "campo2": 123,
    "campo3": true
  }
  ```
- **Validaciones:**
  - `campo1`: Requerido, max 255 caracteres
  - `campo2`: Requerido, rango 1-1000
- **Response 200 OK:**
  ```json
  {
    "id": 1,
    "campo1": "valor",
    "timestamp": "2026-09-25T10:00:00Z"
  }
  ```
- **Response 400 Bad Request:**
  ```json
  {
    "message": "Validation failed",
    "errorCode": "VALIDATION_ERROR",
    "timestamp": "2026-09-25T10:00:00Z"
  }
  ```
- **Response 401 Unauthorized:**
  ```json
  {
    "message": "Authentication required",
    "errorCode": "AUTH_REQUIRED",
    "timestamp": "2026-09-25T10:00:00Z"
  }
  ```

[Repetir para cada endpoint necesario]

## Cambios en Base de Datos

### Nuevas Tablas
**Tabla:** `nombre_tabla`
- `id` BIGINT PRIMARY KEY AUTO_INCREMENT
- `campo1` VARCHAR(255) NOT NULL
- `campo2` INT DEFAULT 0
- `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
- `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP

**Índices:**
- `idx_campo1` ON `campo1`

### Tablas Modificadas
**Tabla:** `tabla_existente`
- **Agregar columna:** `nuevo_campo` VARCHAR(100) NULL
- **Agregar índice:** `idx_nuevo_campo` ON `nuevo_campo`

### Relaciones
- `tabla_a.foreign_key_id` → `tabla_b.id` (FOREIGN KEY, ON DELETE CASCADE)

## Guía de Implementación

### Archivos a Crear

#### Controllers
- `src/main/java/ec/com/technoloqie/crmbot/api/controller/[Feature]Controller.java`
  - Endpoints REST
  - Validación de entrada
  - Manejo de respuestas

#### Services
- `src/main/java/ec/com/technoloqie/crmbot/api/service/I[Feature]Service.java`
  - Interfaz del servicio
- `src/main/java/ec/com/technoloqie/crmbot/api/service/impl/[Feature]ServiceImpl.java`
  - Implementación de lógica de negocio
  - Transacciones

#### Repositories
- `src/main/java/ec/com/technoloqie/crmbot/api/repository/[Feature]Repository.java`
  - JPA Repository
  - Query methods

#### DTOs
- `src/main/java/ec/com/technoloqie/crmbot/api/dto/[Feature]RequestDto.java`
  - Request DTO con validaciones
- `src/main/java/ec/com/technoloqie/crmbot/api/dto/[Feature]ResponseDto.java`
  - Response DTO

#### Entities
- `src/main/java/ec/com/technoloqie/crmbot/api/model/[Feature].java`
  - JPA Entity
  - Relaciones

#### Exceptions
- `src/main/java/ec/com/technoloqie/crmbot/api/exception/[Feature]Exception.java`
  - Excepción personalizada

### Archivos a Modificar
- `src/main/resources/application.properties` - Agregar configuración [especificar qué]
- `pom.xml` - Agregar dependencias [especificar cuáles, si aplica]

### Dependencias
- **Requiere:** [Lista de features prerequisito]
- **Bloquea:** [Lista de features que dependen de esta]

## Requisitos No Funcionales

### Seguridad
- **Autenticación:** JWT token requerido en header `Authorization: Bearer <token>`
- **Autorización:** Roles permitidos: [listar roles]
- **Validación de entrada:** Sanitizar todos los inputs para prevenir SQL injection y XSS
- **Encriptación:** Campos sensibles [listar campos] deben encriptarse

### Rendimiento
- **Tiempo de respuesta:** < 200ms (percentil 95)
- **Throughput:** Mínimo 100 req/seg
- **Optimización de queries:** Usar índices en campos [listar campos]
- **Paginación:** Endpoints de listado deben paginar (default: 20 items/página)

### Escalabilidad
- **Diseño stateless:** No guardar estado en memoria
- **Connection pooling:** min=10, max=50 conexiones
- **Procesamiento asíncrono:** [Si aplica, especificar]

### Confiabilidad
- **Manejo de errores:** Try-catch en capas service y controller
- **Logging:** Usar `@Slf4j`, loggear errores con stacktrace
- **Reintentos:** [Si aplica, especificar estrategia]
- **Transacciones:** Usar `@Transactional` en métodos de servicio

## Requisitos de Testing

### Tests Unitarios
- [ ] `[Feature]ControllerTest` - Tests de endpoints (MockMvc)
- [ ] `[Feature]ServiceImplTest` - Tests de lógica de negocio (mocks)
- [ ] `[Feature]RepositoryTest` - Tests de queries (@DataJpaTest)
- [ ] Tests de validación de DTOs

**Escenarios mínimos:**
- Happy path (caso exitoso)
- Validación de entrada inválida
- Manejo de errores de negocio
- Casos borde

### Tests de Integración
- [ ] Test end-to-end de API (@SpringBootTest)
- [ ] Test de integración con base de datos
- [ ] Test de autenticación/autorización

### Verificación Manual
1. Iniciar aplicación: `./mvnw spring-boot:run`
2. Llamar endpoint con curl/Postman: [ejemplo de request]
3. Verificar respuesta esperada
4. Verificar datos en base de datos
5. Probar escenarios de error

## Actualizaciones de Documentación

### Documentación de API
- [ ] Agregar anotaciones Swagger en Controller (`@Operation`, `@ApiResponses`, `@Tag`)
- [ ] Actualizar especificación OpenAPI (si se mantiene manualmente)

### README
- [ ] Agregar feature a sección "Features"
- [ ] Actualizar lista de endpoints
- [ ] Agregar ejemplos de uso

### Documentación Técnica
- [ ] Actualizar diagramas de arquitectura (si aplica)
- [ ] Crear ADR (Architecture Decision Record) si hay decisiones arquitectónicas importantes

## Definición de "Done"

Esta feature se considera completa cuando:
- [ ] Todos los endpoints implementados y funcionando
- [ ] Todos los tests unitarios pasando (cobertura > 80%)
- [ ] Tests de integración pasando
- [ ] Documentación API actualizada (Swagger)
- [ ] Code review aprobado
- [ ] Sin violaciones de estándares de código
- [ ] README actualizado
- [ ] Verificación manual exitosa
```

**Instrucciones para enriquecer:**
1. Usa el contexto técnico de la documentación del proyecto
2. Sigue las convenciones de nombres del proyecto
3. Respeta la arquitectura de capas (Controller/Service/Repository)
4. Aplica los estándares de código documentados
5. Sé específico: nombres de archivos, rutas completas, ejemplos concretos
6. Incluye todos los casos de error, no solo el happy path

### Paso 6: Guardar historia enriquecida

**Ubicación:** `tmp/<id>-enriched-us.md`

**Ejemplo:** `tmp/US-01-enriched-us.md`

**Acciones:**
1. Crear el directorio `tmp/` si no existe
   ```bash
   mkdir -p tmp
   ```
2. Escribir el contenido enriquecido en formato Markdown
3. Verificar que el archivo se guardó correctamente

**Nota:** La carpeta `tmp/` ya debe estar incluida en `.gitignore` para evitar commits accidentales.

### Paso 7: Actualizar estado en feature_list.json

**Cambio requerido:**
```json
{
  "status": "pending"  // ANTES
}
```
↓
```json
{
  "status": "history_ready"  // DESPUÉS
}
```

**IMPORTANTE:**
- ✅ **SOLO** modificar el campo `"status"`
- ❌ **NO** modificar `id`, `name`, `description`, `sdd` ni ningún otro campo
- ✅ Mantener el formato JSON válido
- ✅ Preservar la indentación y estructura

**Validación:**
- Verificar que el JSON sigue siendo válido después del cambio
- Confirmar que solo se modificó el status del ticket correcto

### Paso 8: Informar al usuario

**Mensaje de salida:**

```
✅ Historia refinada exitosamente!

📄 Archivo generado: `tmp/<id>-enriched-us.md`
📊 Estado actualizado: `pending` → `history_ready`

🔍 Próximos pasos:
1. Revisa la historia enriquecida en `tmp/<id>-enriched-us.md`
2. Valida que todos los detalles son correctos
3. Realiza ajustes manuales si es necesario
4. Ejecuta el flujo de spec de Kiro (sesión Spec) cuando estés listo para generar:
   - requirements.md
   - design.md
   - tasks.md

Una vez aprobado el spec, el implementer puede comenzar el desarrollo autónomo.
```

Si la historia **ya estaba completa** en el Paso 4:
```
✅ Historia ya está completa!

La historia de usuario `<id>` ya contiene todos los detalles técnicos necesarios para implementación autónoma.

No se realizaron cambios. Puedes proceder directamente con el flujo de spec de Kiro.
```

## Contexto Técnico Requerido

Esta skill requiere acceso a los siguientes archivos del proyecto:

### Archivos Obligatorios
- **`feature_list.json`** - Lista de features con estados
- **`docs/backend-standards.md`** - Estándares de código y testing
- **`docs/base-standards.md`** - Mapa de navegación del proyecto

### Archivos Opcionales (si existen)
- **`docs/structure.md`** - Arquitectura del proyecto
- **`docs/tech.md`** - Stack tecnológico
- **`docs/product.md`** - Contexto del producto
- **`docs/data-model.md`** - Modelo de datos

## Validación de Calidad

Una historia bien refinada debe tener:
- ✅ Descripción funcional clara (> 200 palabras)
- ✅ Mínimo 5 criterios de aceptación
- ✅ Especificación completa de API (todos los endpoints)
- ✅ Cambios en base de datos documentados
- ✅ Lista de archivos a crear/modificar (> 5 archivos)
- ✅ Requisitos no funcionales especificados
- ✅ Requisitos de testing definidos
- ✅ Actualizaciones de documentación identificadas

## Troubleshooting

### Problema: feature_list.json no encontrado
**Solución:** Verificar que estás ejecutando desde la raíz del proyecto donde se encuentra `feature_list.json`.

### Problema: Ticket no encontrado
**Solución:** Verificar el ID del ticket. Usar `"pending"` como argumento para obtener el primer ticket pendiente.

### Problema: Historia ya en estado history_ready
**Solución:** Este ticket ya fue refinado. Revisar `tmp/<id>-enriched-us.md` para ver la versión enriquecida existente.

### Problema: No se puede escribir en tmp/
**Solución:** 
```bash
mkdir -p tmp
chmod 755 tmp
```

### Problema: Documentación faltante
**Solución:** Crear los archivos de documentación faltantes antes de ejecutar la skill:
- `docs/backend-standards.md`
- `docs/base-standards.md`

## Mejores Prácticas

1. **Sé específico:** Usa nombres concretos de archivos, campos, y tipos de datos
2. **Piensa end-to-end:** Considera el flujo completo desde API hasta base de datos
3. **Considera casos borde:** Documenta escenarios de error y condiciones límite
4. **Referencia la arquitectura:** Alinea con la estructura existente del proyecto
5. **Aplica estándares:** Sigue convenciones de nombres y estilo del proyecto
6. **Sé verificable:** Cada criterio de aceptación debe ser testeable

## Integración con SDD Workflow

Esta skill es parte del flujo Specification-Driven Development:

```
┌──────────────────────────────────────────────────────────┐
│ Flujo SDD                                                │
├──────────────────────────────────────────────────────────┤
│ pending → [refine-stories] → history_ready → ⏸ HUMANO  │
│                                             REVISA       │
│             ↓ (si aprueba)                               │
│        in_progress → [implementer] → [reviewer] → done   │
└──────────────────────────────────────────────────────────┘
```

**Rol de refine-stories:**
- **Input:** Historia de alto nivel (status: pending)
- **Output:** Especificación técnica enriquecida (status: history_ready)
- **Puerta humana:** Usuario revisa antes de aprobar para implementación
- **Siguiente paso:** Sesión Spec de Kiro genera requirements.md, design.md, tasks.md

## Ejemplo de Uso

### Comando
```
Refina la historia US-01
```
o
```
Refina el primer ticket pending
```

### Resultado
```
✅ Historia refinada exitosamente!

📄 Archivo generado: `tmp/US-01-enriched-us.md`
📊 Estado actualizado: `pending` → `history_ready`

🔍 Próximos pasos:
1. Revisa la historia enriquecida
2. Ejecuta el flujo de spec de Kiro cuando estés listo
```

## Notas de Compatibilidad

Esta skill es compatible con:
- ✅ Kiro IDE
- ✅ Claude (Anthropic)
- ✅ Cursor IDE
- ✅ GitHub Copilot
- ✅ Codeium
- ✅ Cualquier cliente que soporte Agent Skills specification

## Versión

**v1.0.0** - Versión inicial con soporte para proyectos Java Spring Boot

## Licencia

MIT License - Libre para usar, modificar y distribuir
