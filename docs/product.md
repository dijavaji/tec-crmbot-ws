---
description: Visión del producto, usuarios objetivo, principios de diseño y conceptos del dominio
alwaysApply: true
---

# Product Overview

**Smart Ventas IA** es un microservicio backend Java Spring Boot que transforma imágenes de cuadernos de ventas en datos estructurados mediante IA (Microservicio tec-enrichmentAI-ws API), expuesto como API REST para ser consumido por aplicaciones cliente (web, móvil, WhatsApp bots).

**Propuesta de valor:** Reducir el tiempo de registro manual de ventas de 1 hora a 30 segundos, eliminando errores de cálculo y proporcionando datos estructurados para análisis.

---

## Para quién

**Clientes del microservicio (desarrolladores):**
- Equipos frontend que consumen la API (React, Angular, Vue, Flutter, iOS, Android)
- Integradores de WhatsApp Business API
- Otros microservicios del ecosistema

**Usuarios finales (indirecto):**
- Pequeños comerciantes (tiendas, cafés, restaurantes) en Ecuador/LatAm
- Edad: 18-60 años, nivel tecnológico básico
- Registran ventas en cuaderno físico, dedican 5-7 horas/semana a cuadrar caja
- No usan software contable por costo/complejidad

---

## Principios

1. **API-first:** El microservicio expone contratos REST claros y versionados
2. **Confianza con transparencia:** El OCR retorna nivel de confianza por campo; el cliente decide si validar manualmente
3. **Fail gracefully:** Si Gemini falla, el sistema degrada a entrada manual (no bloquea al usuario)
4. **Stateless:** Autenticación JWT, sin sesiones en servidor (escalabilidad horizontal)
5. **Separation of concerns:** Capas claras (Controller → Service → Repository), sin lógica de negocio en controllers
6. **Observabilidad:** Logs estructurados, métricas exportadas (Prometheus), health checks

---

## Qué NO es

❌ **No es una aplicación con UI:** Es un backend puro; los clientes construyen su propia experiencia de usuario  
❌ **No es un sistema contable completo:** Solo registra ventas; no hace facturación electrónica, inventario o nómina  
❌ **No reemplaza el cuaderno:** Complementa el hábito existente con automatización post-registro  
❌ **No es 100% autónomo:** Requiere validación humana cuando la confianza de OCR es baja (<70%)  
❌ **No es offline-first:** Requiere conexión a internet para procesamiento de IA y sincronización

---

## Domain Concepts

**Negocio:** Entidad que representa un comercio (café, tienda). Tiene usuarios asociados y registra ventas.

**Venta:** Transacción del día con fecha, hora, total y lista de ítems. Puede originarse de IA (`fuente: "ia_ocr"`) o entrada manual (`fuente: "manual"`).

**ItemVenta:** Producto individual dentro de una venta con: producto (string), cantidad (int), precioUnitario (decimal), total (decimal), confianzaOCR (0.0-1.0).

**ImagenCuaderno:** Foto del cuaderno almacenada en Google Cloud Storage, vinculada a una venta para auditoría.

**ValidationResult:** Respuesta del sistema indicando si los datos extraídos por IA son confiables (`GUARDADO_AUTOMATICO`) o requieren revisión humana (`REQUIERE_VALIDACION`).

**ConfianzaOCR:** Métrica (0.0-1.0) que indica qué tan seguro está Agente OCR IA de cada campo extraído. Umbrales: >0.90 alta confianza, <0.70 baja confianza.
