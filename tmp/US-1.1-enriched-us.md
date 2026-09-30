# US-1.1 - JPA Entities (Sale, SaleItem, NotebookImage)

## Descripción Original
**Como** capa de persistencia **Quiero** definir las entidades JPA del dominio **Para** mapear el modelo de datos en MySQL según data-model.md

## Descripción Funcional Enriquecida

### Funcionalidad Principal
Esta feature implementa el núcleo del modelo de dominio del microservicio `tec-crmbot-ws` utilizando JPA (Java Persistence API). Se encarga de definir cómo se representan y persisten las ventas, sus ítems desglosados y las imágenes de cuadernos procesadas en la base de datos MySQL.

### Flujo de Usuario
1. El sistema recibe una solicitud de procesamiento de imagen o registro de venta.
2. La capa de negocio interactúa con estas entidades para organizar los datos extraídos por la IA.
3. JPA gestiona la persistencia de estos objetos, asegurando que las relaciones (como una venta con sus múltiples ítems) se mantengan íntegras.
4. El usuario final (a través de otros servicios) podrá consultar el historial de ventas persistido de forma estructurada.

### Comportamiento del Sistema
- **UUIDs:** Todas las entidades principales utilizarán UUIDs como claves primarias para garantizar unicidad global y evitar la exposición de secuencias incrementales.
- **Auditoría:** Se incluirán campos de auditoría automáticos (`createdAt`, `uploadedAt`) para rastrear el tiempo de creación de los registros.
- **Integridad:** La eliminación de una `Sale` eliminará en cascada sus `SaleItem`s asociados (orphan removal).
- **Consistencia:** Los montos se manejarán con `BigDecimal` para evitar errores de precisión de punto flotante.

### Valor de Negocio
Establece la base estructural para todo el microservicio. Sin un modelo de datos robusto y bien mapeado, el procesamiento de IA no tendría dónde aterrizar los resultados de forma persistente y consultable, impidiendo la generación de KPIs y el historial de ventas para los negocios.

## Criterios de Aceptación

### Escenarios de Éxito
1. **CUANDO** se instancia una entidad `Sale` con datos válidos **ENTONCES** JPA debe persistirla en la tabla `sale` generando un UUID automáticamente.
2. **CUANDO** se guarda una `Sale` que contiene una lista de `SaleItem`s **ENTONCES** todos los ítems deben persistirse en la tabla `sale_item` con el `sale_id` correcto (FK).
3. **CUANDO** se guarda una `NotebookImage` vinculada a una venta **ENTONCES** el registro debe almacenarse con el estado inicial `PENDING` y la URL de GCS correspondiente.
4. **CUANDO** se consulta una `Sale` por su ID **ENTONCES** el sistema debe permitir cargar de forma perezosa (Lazy) sus ítems y metadatos de imagen asociados.

### Escenarios de Error
1. **CUANDO** se intenta persistir un `SaleItem` sin una `Sale` asociada (y la relación es obligatoria) **ENTONCES** el sistema debe lanzar una excepción de violación de integridad de datos.
2. **CUANDO** el `totalAmount` de una venta es nulo **ENTONCES** la validación JPA debe impedir la persistencia.

### Casos Borde
1. **Venta sin ítems:** El sistema debe permitir persistir una `Sale` con una lista de ítems vacía inicialmente (ej. durante el proceso de carga).
2. **Confianza OCR baja:** La entidad `SaleItem` debe soportar valores de `ocrConfidence` cercanos a 0 sin fallar en la persistencia.

## Especificación de API
*(Nota: US-1.1 se enfoca en persistencia interna, la API se define formalmente en US-1.11, pero aquí se listan los modelos de datos que la sustentarán)*

### Entidades Core

#### Sale
- **Campos:** `id` (UUID), `businessId` (String), `userId` (String), `saleDate` (LocalDate), `saleTime` (LocalTime), `totalAmount` (BigDecimal), `itemCount` (Integer), `source` (String), `averageConfidence` (Double), `createdAt` (LocalDateTime).

#### SaleItem
- **Campos:** `id` (UUID), `saleId` (FK), `productName` (String), `quantity` (Integer), `totalAmount` (BigDecimal), `ocrConfidence` (Double), `manuallyEdited` (Boolean).

#### NotebookImage
- **Campos:** `id` (UUID/String), `saleId` (FK, optional), `storageUrl` (String), `processingStatus` (Enum: PENDING, PROCESSED, ERROR), `widthPx` (Integer), `heightPx` (Integer), `uploadedAt` (LocalDateTime).

## Cambios en Base de Datos

### Nuevas Tablas

**Tabla:** `sale`
- `id` CHAR(36) PRIMARY KEY
- `business_id` VARCHAR(50) NOT NULL
- `user_id` VARCHAR(50) NOT NULL
- `sale_date` DATE NOT NULL
- `sale_time` TIME
- `total_amount` DECIMAL(19,2)
- `item_count` INT
- `source` VARCHAR(20)
- `average_confidence` DOUBLE
- `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP

**Tabla:** `sale_item`
- `id` CHAR(36) PRIMARY KEY
- `sale_id` CHAR(36) NOT NULL
- `product_name` VARCHAR(255)
- `quantity` INT
- `total_amount` DECIMAL(19,2)
- `ocr_confidence` DOUBLE
- `manually_edited` BOOLEAN DEFAULT FALSE
- FOREIGN KEY (`sale_id`) REFERENCES `sale`(`id`) ON DELETE CASCADE

**Tabla:** `notebook_image`
- `id` VARCHAR(100) PRIMARY KEY
- `sale_id` CHAR(36)
- `storage_url` VARCHAR(512)
- `processing_status` VARCHAR(20)
- `width_px` INT
- `height_px` INT
- `uploaded_at` TIMESTAMP
- FOREIGN KEY (`sale_id`) REFERENCES `sale`(`id`)

## Guía de Implementación

### Archivos a Crear

#### Entities (src/main/java/ec/com/technoloqie/crmbot/api/model/)
- `Sale.java`: Entidad principal con `@OneToMany` hacia `SaleItem`.
- `SaleItem.java`: Entidad de detalle con `@ManyToOne` hacia `Sale`.
- `NotebookImage.java`: Entidad de metadatos de imagen.
- `ProcessingStatus.java`: Enum para los estados de procesamiento.

## Requisitos de Testing

### Tests Unitarios
- `SaleTest`: Validar que las anotaciones JPA y el mapeo de columnas sean correctos.
- `PersistenceMappingTest`: Usar `@DataJpaTest` para verificar que las relaciones OneToMany funcionan y persisten en cascada.

## Definición de "Done"
- Entidades `Sale`, `SaleItem`, `NotebookImage` creadas y anotadas correctamente.
- Relaciones JPA establecidas (cascada y orphan removal).
- Tests de persistencia básica pasando.
- Código sigue `backend-standards.md` (Tabs, PascalCase, etc.).
