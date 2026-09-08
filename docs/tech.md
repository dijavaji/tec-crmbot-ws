---
description: Stack tecnológico, librerías, versiones y restricciones técnicas del proyecto
alwaysApply: false
---

# Stack Tecnológico

## Proyecto Actual: AuditBot AI

AuditBot AI es un microservicio Java Spring Boot responsable de auditar conversaciones usando analizadores de IA.

El backend consume eventos de RabbitMQ, ejecuta múltiples estrategias de análisis de IA a través de Ollama Cloud, persiste resultados de auditoría en MySQL, y expone APIs REST consumidas por el dashboard.

## Core Technologies

- **Java 17**
- **Spring Boot 3**
- **Spring Web**
- **Spring AMQP**
- **Spring Data JPA**
- **Hibernate ORM**
- **Maven**

## Database

- **MySQL 8**
- Hibernate ORM
- JPA repositories

## Messaging

- **RabbitMQ**
- Event-driven architecture

## AI Integration

- **Ollama Cloud** (análisis de IA)
- Estrategias extensibles de analizadores

## Arquitectura

El proyecto prioriza:

- **Separación de responsabilidades:** Controladores delgados, servicios con lógica de negocio, repositorios para persistencia
- **Comunicación basada en eventos:** Consumo de mensajes desde RabbitMQ
- **Resiliencia ante fallos externos:** Manejo robusto de excepciones en integraciones
- **Extensibilidad de analizadores de IA:** Arquitectura de estrategias para múltiples proveedores
- **Código mantenible y testeable:** Inyección de dependencias, pruebas unitarias e integración
