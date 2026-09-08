# Plan de Implementación - Smart Ventas IA MVP

**Fecha de generación:** 11 de septiembre de 2026  
**Basado en Design Doc:** `docs/superpowers/specs/2026-09-11-smart-ventas-ia-design-v2.md`  
**Timeline:** 48 horas  
**Stack:** Spring Boot 3 + Java 17, React 18, Gemini API, Firebase + PostgreSQL, Google Cloud Run  
**Concurso:** GDG Quito - Innovating Together (DevFest 2026)  
**Track:** Builder  
**Deadline:** 18 de septiembre, 23:59 (formulario oficial)

---

## 📊 RESUMEN EJECUTIVO

| Métrica | Valor |
|---------|-------|
| **Total de tareas** | 28 |
| **Tiempo total estimado** | 30h 15min |
| **Timeline disponible** | 48h |
| **Holgora (buffer)** | 17h 45min ✅ |
| **Tareas en ruta crítica** | 9 |
| **Fecha límite absoluta** | 18 sept, 23:59 |

---

## 🎯 RUTA CRÍTICA

Estas tareas **NO PUEDEN ATRASARSE** sin impactar el deadline de 48h:

```
FASE 0 → FASE 1 → FASE 3 → FASE 5
  ↓        ↓        ↓        ↓
0.1 → 1.1 → 1.2 → 1.3 → 1.4 → 3.1 → 5.2 → 5.3 → 5.4
  ↓        ↓        ↓
0.2 → 0.4 → 1.6 → 1.4
  ↓        ↓
0.5 → 5.1
```

**Prioridad MÁXIMA:** Tareas 0.1, 1.2, 3.1, 5.2, 5.4

---

## 📋 FASES DEL PLAN

### FASE 0: SETUP INICIAL (Horas 0-4)

| ID | Tarea | Tiempo | Dependencias | Estado |
|----|-------|--------|--------------|--------|
| [0.1](#tarea-01) | Crear proyecto Spring Boot | 30 min | Ninguna | ⬜ Pendiente |
| [0.2](#tarea-02) | Configurar credenciales GCP | 45 min | Ninguna | ⬜ Pendiente |
| [0.3](#tarea-03) | Setup Firebase RTDB | 30 min | Ninguna | ⬜ Pendiente |
| [0.4](#tarea-04) | Setup PostgreSQL | 45 min | 0.2 | ⬜ Pendiente |
| [0.5](#tarea-05) | Configurar Dockerfile | 30 min | 0.1 | ⬜ Pendiente |

### FASE 1: BACKEND SPRING BOOT (Horas 4-16)

| ID | Tarea | Tiempo | Dependencias | Estado |
|----|-------|--------|--------------|--------|
| [1.1](#tarea-11) | VentasController (endpoints) | 90 min | 0.1, 0.2 | ⬜ Pendiente |
| [1.2](#tarea-12) | GeminiOcrService | 120 min | 0.2 | ⬜ Pendiente |
| [1.3](#tarea-13) | ValidationService | 60 min | 1.2 | ⬜ Pendiente |
| [1.4](#tarea-14) | VentasService | 90 min | 1.1, 1.2, 1.3 | ⬜ Pendiente |
| [1.5](#tarea-15) | CloudStorageService | 45 min | 0.2 | ⬜ Pendiente |
| [1.6](#tarea-16) | JPA Entities + Repositories | 90 min | 0.4 | ⬜ Pendiente |
| [1.7](#tarea-17) | Spring Retry config | 30 min | 1.2 | ⬜ Pendiente |

### FASE 2: FRONTEND REACT (Horas 16-24)

| ID | Tarea | Tiempo | Dependencias | Estado |
|----|-------|--------|--------------|--------|
| [2.1](#tarea-21) | DashboardVentas module | 90 min | 0.1 | ⬜ Pendiente |
| [2.2](#tarea-22) | Gráfico últimos 7 días | 60 min | 2.1 | ⬜ Pendiente |
| [2.3](#tarea-23) | UploadModal | 90 min | 2.1 | ⬜ Pendiente |
| [2.4](#tarea-24) | ValidacionTable | 90 min | 2.3, 1.1 | ⬜ Pendiente |
| [2.5](#tarea-25) | VentaList | 60 min | 2.1 | ⬜ Pendiente |

### FASE 3: INTEGRACIÓN END-TO-END (Horas 24-36)

| ID | Tarea | Tiempo | Dependencias | Estado |
|----|-------|--------|--------------|--------|
| [3.1](#tarea-31) | Integrar flujo completo | 120 min | 1.1, 1.2, 1.3, 1.4, 2.1, 2.3, 2.4 | ⬜ Pendiente |
| [3.2](#tarea-32) | Configurar CORS | 30 min | 1.1 | ⬜ Pendiente |
| [3.3](#tarea-33) | Testing manual (3 casos) | 90 min | 3.1 | ⬜ Pendiente |

### FASE 4: TESTING Y QUALITY (Horas 32-40)

| ID | Tarea | Tiempo | Dependencias | Estado |
|----|-------|--------|--------------|--------|
| [4.1](#tarea-41) | Tests unitarios servicios | 90 min | 1.2, 1.3, 1.4 | ⬜ Pendiente |
| [4.2](#tarea-42) | Tests integración controller | 60 min | 1.1, 4.1 | ⬜ Pendiente |
| [4.3](#tarea-43) | GitHub Actions CI/CD | 60 min | 4.1, 4.2 | ⬜ Pendiente |

### FASE 5: DEPLOY Y ENTREGABLE CONCURSO (Horas 36-48)

| ID | Tarea | Tiempo | Dependencias | Estado |
|----|-------|--------|--------------|--------|
| [5.1](#tarea-51) | Deploy a Cloud Run | 60 min | 0.5, 4.3 | ⬜ Pendiente |
| [5.2](#tarea-52) | Grabar video demo (2 min) | 90 min | 3.1 | ⬜ Pendiente |
| [5.3](#tarea-53) | Publicar en Instagram/LinkedIn | 30 min | 5.2 | ⬜ Pendiente |
| [5.4](#tarea-54) | Registrar en formulario oficial | 30 min | 5.3 | ⬜ Pendiente |
| [5.5](#tarea-55) | Documentación final + README | 60 min | 5.1 | ⬜ Pendiente |

---

## 📝 DETALLE DE TAREAS

### Tarea 0.1

**Crear proyecto Spring Boot con Spring Initializr**

- **Descripción:** Inicializar microservicio ventas-api con dependencias básicas
- **Archivos:** 
  - `microservices/ventas-api/pom.xml`
  - `microservices/ventas-api/src/main/java/com/technoloqie/ventas/VentasApplication.java`
- **Dependencias:** Ninguna
- **Tiempo estimado:** 30 min
- **Criterios de aceptación:**
  - ✅ Proyecto Spring Boot compila sin errores (`mvn clean compile`)
  - ✅ Endpoint `/health` retorna 200 OK
  - ✅ Dependencies en pom.xml: spring-boot-starter-web, spring-boot-starter-data-jpa, firebase-admin, google-cloud-storage
- **Comandos:**
  ```bash
  # Opción A: Spring Initializr web
  # Ir a https://start.spring.io/ y generar proyecto
  
  # Opción B: CLI
  curl https://start.spring.io/starter.zip \
    -d type=maven-project \
    -d language=java \
    -d bootVersion=3.2.0 \
    -d baseDir=ventas-api \
    -d groupId=com.technoloqie \
    -d artifactId=ventas-api \
    -d name=ventas-api \
    -d packageName=com.technoloqie.ventas \
    -d javaVersion=17 \
    -d dependencies=web,data-jpa,lombok \
    -o ventas-api.zip
  ```

---

### Tarea 0.2

**Configurar credenciales de Google Cloud**

- **Descripción:** Setup de API keys y service account para Gemini, Firebase, Cloud Storage
- **Archivos:** 
  - `microservices/ventas-api/src/main/resources/application.properties`
  - `.env.gcp`
- **Dependencias:** Ninguna
- **Tiempo estimado:** 45 min
- **Criterios de aceptación:**
  - ✅ `GOOGLE_GEMINI_API_KEY` configurada en application.properties
  - ✅ `FIREBASE_CREDENTIALS` apuntando a JSON de service account
  - ✅ `GOOGLE_CLOUD_STORAGE_BUCKET` definido
  - ✅ PostgreSQL connection string configurado
- **Variables de entorno (.env.gcp):**
  ```properties
  GOOGLE_GEMINI_API_KEY=AIzaSy...
  FIREBASE_CREDENTIALS=/path/to/service-account.json
  GOOGLE_CLOUD_STORAGE_BUCKET=smart-ventas-images
  DATABASE_URL=jdbc:postgresql://localhost:5432/smart_ventas
  DATABASE_USERNAME=postgres
  DATABASE_PASSWORD=xxx
  ```

---

### Tarea 0.3

**Setup de Firebase Realtime Database**

- **Descripción:** Crear instancia de Firebase RTDB y configurar reglas de seguridad
- **Archivos:** 
  - `firebase.rules.json`
  - `firestore.indexes.json`
- **Dependencias:** Ninguna
- **Tiempo estimado:** 30 min
- **Criterios de aceptación:**
  - ✅ Firebase RTDB instance creada en consola de Firebase
  - ✅ Reglas de seguridad permiten lectura/escritura solo a usuarios autenticados
  - ✅ Estructura de datos: `/ventas/{negocioId}/{fecha}` definida
- **Firebase Rules:**
  ```json
  {
    "rules": {
      "ventas": {
        "$negocioId": {
          ".read": "auth != null",
          ".write": "auth != null"
        }
      }
    }
  }
  ```

---

### Tarea 0.4

**Setup de PostgreSQL (Cloud SQL o local)**

- **Descripción:** Crear base de datos y tablas iniciales
- **Archivos:** 
  - `db/migration/V1__initial_schema.sql`
- **Dependencias:** 0.2
- **Tiempo estimado:** 45 min
- **Criterios de aceptación:**
  - ✅ Database 'smart_ventas' creada
  - ✅ Tablas: negocios, usuarios, ventas, items_venta, imagenes_cuaderno creadas
  - ✅ Flyway migration ejecutada exitosamente
  - ✅ Connection pool HikariCP configurado (maxPoolSize=10)
- **Schema SQL:**
  ```sql
  CREATE TABLE negocios (
    id VARCHAR(255) PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    rubro VARCHAR(100),
    direccion TEXT,
    telefono VARCHAR(20),
    propietario_id VARCHAR(255),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT true
  );
  
  CREATE TABLE ventas (
    id VARCHAR(255) PRIMARY KEY,
    negocio_id VARCHAR(255) REFERENCES negocios(id),
    usuario_id VARCHAR(255),
    fecha DATE NOT NULL,
    hora TIME NOT NULL,
    total_dia DECIMAL(10,2),
    numero_items INTEGER,
    fuente VARCHAR(20),
    confianza_promedio DECIMAL(4,3),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
  );
  ```

---

### Tarea 0.5

**Configurar Dockerfile para Cloud Run**

- **Descripción:** Crear Dockerfile multi-stage para Spring Boot
- **Archivos:** 
  - `microservices/ventas-api/Dockerfile`
  - `.dockerignore`
- **Dependencias:** 0.1
- **Tiempo estimado:** 30 min
- **Criterios de aceptación:**
  - ✅ Dockerfile con multi-stage build (build + runtime)
  - ✅ Imagen Docker construye sin errores (`docker build -t ventas-api .`)
  - ✅ Container corre localmente (`docker run -p 8080:8080 ventas-api`)
  - ✅ Health check endpoint accesible desde fuera del container
- **Dockerfile:**
  ```dockerfile
  # Build stage
  FROM maven:3.9-eclipse-temurin-17 AS build
  WORKDIR /app
  COPY pom.xml .
  RUN mvn dependency:go-offline
  COPY src ./src
  RUN mvn clean package -DskipTests
  
  # Runtime stage
  FROM eclipse-temurin:17-jre-alpine
  WORKDIR /app
  COPY --from=build /app/target/*.jar app.jar
  EXPOSE 8080
  ENTRYPOINT ["java", "-jar", "app.jar"]
  ```

---

*(Continúa con el detalle de las 28 tareas completas...)*

---

## 🎯 CHECKLIST DE PROGRESO

Marcar con ✅ a medida que se completan:

### FASE 0: SETUP
- [ ] 0.1 - Crear proyecto Spring Boot
- [ ] 0.2 - Configurar credenciales GCP
- [ ] 0.3 - Setup Firebase RTDB
- [ ] 0.4 - Setup PostgreSQL
- [ ] 0.5 - Configurar Dockerfile

### FASE 1: BACKEND
- [ ] 1.1 - VentasController
- [ ] 1.2 - GeminiOcrService
- [ ] 1.3 - ValidationService
- [ ] 1.4 - VentasService
- [ ] 1.5 - CloudStorageService
- [ ] 1.6 - JPA Entities + Repositories
- [ ] 1.7 - Spring Retry config

### FASE 2: FRONTEND
- [ ] 2.1 - DashboardVentas module
- [ ] 2.2 - Gráfico últimos 7 días
- [ ] 2.3 - UploadModal
- [ ] 2.4 - ValidacionTable
- [ ] 2.5 - VentaList

### FASE 3: INTEGRACIÓN
- [ ] 3.1 - Integrar flujo completo
- [ ] 3.2 - Configurar CORS
- [ ] 3.3 - Testing manual (3 casos)

### FASE 4: TESTING
- [ ] 4.1 - Tests unitarios servicios
- [ ] 4.2 - Tests integración controller
- [ ] 4.3 - GitHub Actions CI/CD

### FASE 5: DEPLOY
- [ ] 5.1 - Deploy a Cloud Run
- [ ] 5.2 - Grabar video demo (2 min)
- [ ] 5.3 - Publicar en Instagram/LinkedIn
- [ ] 5.4 - Registrar en formulario oficial ⚠️ **DEADLINE: 18 SEPT, 23:59**
- [ ] 5.5 - Documentación final + README

---

## 📌 RECORDATORIOS CRÍTICOS

⚠️ **Deadline absoluto:** 18 de septiembre, 23:59 (formulario oficial del concurso)  
⚠️ **Tarea 5.4 es obligatoria:** Sin submit, no hay participación aunque el MVP esté perfecto  
⚠️ **Video demo máximo 2 minutos:** Si excede, es descalificado automáticamente  
⚠️ **Post debe ser PÚBLICO:** Si está privado, no cuenta como participación  

---

## 🔗 ENLACES ÚTILES

- **Design Doc v2:** `docs/superpowers/specs/2026-09-11-smart-ventas-ia-design-v2.md`
- **Formulario oficial del concurso:** [Link pendiente]
- **Bases completas GDG Quito:** [Link al documento]
- **Spring Initializr:** https://start.spring.io/
- **Firebase Console:** https://console.firebase.google.com/
- **Google Cloud Console:** https://console.cloud.google.com/
- **Gemini API Docs:** https://ai.google.dev/docs

---

*Documento generado el 11 de septiembre de 2026 siguiendo la skill writing-plans.*  
*Para actualizar el estado de las tareas, marcar con ✅ en la columna "Estado" de cada fase.*
