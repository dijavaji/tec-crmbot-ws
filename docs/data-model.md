## MODELO DE DATOS ERD

### Diagrama Entidad-Relación Completo

```mermaid
erDiagram
    SALE ||--|{ SALE_ITEM : contains
    SALE ||--o{ NOTEBOOK_IMAGE : has
    
    SALE {
        String id PK "UUID"
        String businessId FK "N:1 con NEGOCIO"
        String userId FK "Quien registró"
        LocalDate saleDate "Índice para búsquedas rápidas"
        LocalTime saleTime
        BigDecimal totalAmount "Suma de items"
        Integer itemCount
        String source "AI_OCR, MANUAL"
        Double averageConfidence "Métrica de calidad"
        LocalDateTime createdAt
    }
    
    SALE_ITEM {
        String id PK "UUID"
        String saleId FK
        String productName
        Integer quantity
        BigDecimal totalAmount
        Double ocrConfidence
        Boolean manuallyEdited
    }
    
    NOTEBOOK_IMAGE {
        String id PK
        String saleId FK "1:1 opcional"
        String storageUrl "URL en GCS"
        String processingStatus "PENDING, PROCESSED, ERROR"
        Integer widthPx "Metadatos de imagen"
        Integer heightPx
        LocalDateTime uploadedAt
    }
    
    
```

### Estadísticas de la Base de Datos

| Tabla | Registros (Proyectado 30 días) | Tamaño Estimado |
|-------|--------------------------------|-----------------|
| NEGOCIO | 10 | < 1 MB |
| USUARIO | 20 | < 1 MB |
| VENTA | 300 (10 negoc. × 30 días) | ~5 MB |
| ITEM_VENTA | 1,500 (5 items/venta promedio) | ~15 MB |
| IMAGEN_CUADERNO | 200 | ~500 MB (imágenes en GCS) |
| **TOTAL** | **2,030 registros** | **~520 MB** |

---