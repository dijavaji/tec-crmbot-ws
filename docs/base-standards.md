---
description: Este documento contiene todas las reglas y directrices de desarrollo para este proyecto, aplicables a todos los agentes de IA (Claude, Cursor, Codex, Gemini, Kiro, OpenCode, etc.).
alwaysApply: true
---

# Mapa de navegación para agentes de IA

> Este archivo es el **punto de entrada** para cualquier agente que trabaje en este
> repositorio. NO es una biblia de reglas: es un **mapa**. Lee solo lo que
> necesites cuando lo necesites (divulgación progresiva).

---

## 1. Antes de empezar (obligatorio)

1. Ejecuta `./mvnw clean install -Dmaven.test.skip=true` y verifica que termina sin errores. Si falla, **para**
   y resuelve el entorno antes de tocar código.
2. Lee `progress/current.md` para entender en qué estado quedó la última sesión.
3. Lee `feature_list.json`. Toda feature nueva (`"sdd": true`) pasa por
   **Spec Driven Development** — ver §4 de este archivo.
4. Si existe `openspec/changes/<id>/`, lee `proposal.md` y `tasks.md` de esa carpeta
   antes de tocar cualquier código. OpenSpec es la fuente de verdad del spec activo.

---

## 2. Mapa del repositorio

| Archivo / carpeta                          | Qué contiene                                                                                | Cuándo leerlo                          |
|--------------------------------------------|--------------------------------------------------------------------------------------------|----------------------------------------|
| `feature_list.json`                        | Lista de tareas con estado (`pending` / `history_ready` / `in_progress` / `done` / `blocked`) | Siempre, al empezar                    |
| `tmp/<id>-enriched-us.md`                  | Historia de usuario enriquecida (output del skill `refine-stories`)                        | Antes de generar el spec               |
| `openspec/specs/`                          | Source of truth: specs consolidadas del sistema (post-archive)                             | Para entender el estado actual del sistema |
| `openspec/changes/<name>/`                 | Cambio activo: `proposal.md`, `specs/`, `design.md`, `tasks.md`                            | Antes de implementar cualquier feature |
| `openspec/changes/archive/`                | Historial de cambios completados                                                           | Para auditoría y referencia            |
| `openspec/config.yaml`                     | Configuración de OpenSpec (schema, profile, contexto del proyecto)                         | Al inicializar o cambiar workflow      |
| `docs/structure.md`                        | Arquitectura, paquetes, flujo de dependencias                                              | Antes de implementar                   |
| `docs/product.md`                          | Qué es el producto, usuarios, objetivos                                                    | Siempre, al empezar                    |
| `docs/tech.md`                             | Stack, librerías, restricciones, modelo de datos                                           | Siempre, al empezar                    |
| `docs/backend-standards.md`                | Reglas de estilo, nombres, estructura, testing                                             | Antes de escribir código               |
| `docs/verification.md`                     | Cómo verificar que tu trabajo funciona (niveles de testing, trazabilidad)                  | Antes de declarar una tarea como `done` |
| `docs/data-model.md`                       | Modelo de datos                                                                            | Antes de generar el spec               |
| `CHECKPOINTS.md`                           | Criterios objetivos de "estado final correcto"                                             | Para auto-evaluarte                    |
| `ai-specs/agents/`                         | Definiciones de subagentes (`leader`, `implementer`, `reviewer`)                           | Si orquestas trabajo                   |
| `ai-specs/skills/refine-stories/SKILL.md`  | Skill para enriquecer historias de usuario (`refine-stories`)                              | Para refinar historias antes del spec  |
| `src/main/java/`                           | Código de la aplicación                                                                    | Para implementar                       |
| `src/test/java/`                           | Tests automáticos (JUnit 5, Mockito, Testcontainers)                                       | Para verificar                         |
| `pom.xml`                                  | Dependencias Maven y configuración de build                                                | Si necesitas agregar dependencias      |

---

## 3. Reglas duras (no negociables)

- **Una sola feature a la vez.** No mezcles cambios de varias tareas en la misma sesión.
- **No declares una tarea `done` sin pruebas verdes.** Ejecuta `./mvnw clean verify` y
  asegúrate de que todos los tests pasan al 100%.
- **No saltes la fase de refinamiento.** Toda feature con `"sdd": true` debe
  pasar primero por el skill `refine-stories` antes de generar el spec con OpenSpec.
- **No saltes la puerta de aprobación humana.** El leader detiene el flujo
  en `history_ready` y espera a que el humano ejecute `/opsx:propose` y apruebe el spec.
- **Documenta lo que haces** en `progress/current.md` mientras trabajas, no al final.
- **Deja el repositorio limpio** antes de cerrar la sesión (ver §5).
- **Si no sabes algo, busca en `docs/` o en `openspec/specs/`** antes de inventarlo.
- **Constructor injection únicamente** — nunca `@Autowired` en fields.
- **Analyzers son lógica pura** — no importan Spring, JPA ni RabbitMQ.
- **Controllers delgados** — sin lógica de negocio, sin acceso directo a repository.

---

## 4. Flujo de trabajo (SDD + OpenSpec)

```
pending
  │
  ▼
[refine-stories skill]  →  tmp/<id>-enriched-us.md
  │
  ▼
history_ready  ──  ⏸ HUMANO revisa historia enriquecida
  │
  ▼
[/opsx:propose <id>]    →  openspec/changes/<id>/
  │                           ├── proposal.md
  │                           ├── specs/<domain>/spec.md
  │                           ├── design.md
  │                           └── tasks.md
  │
  ▼
⏸ HUMANO revisa y aprueba el spec (proposal.md + tasks.md)
  │
  ▼
in_progress  ──  [implementer ejecuta /opsx:apply]
  │
  ▼
[reviewer verifica]  →  /opsx:verify
  │
  ├─► RECHAZA →  implementer corrige
  │
  └─► APRUEBA →  /opsx:archive  →  openspec/changes/archive/<id>/
                                     openspec/specs/ actualizado
  │
  ▼
done  ──  progress/history.md actualizado
```

### Paso a paso detallado

1. El leader detecta la primera feature `pending` con `"sdd": true` en `feature_list.json`.

2. El leader (o el humano) invoca el skill:
   ```
   refine-stories <id>
   ```
   El skill enriquece la historia y genera `tmp/<id>-enriched-us.md`.
   Cambia el status a `history_ready`.

3. **Pausa.** El humano revisa `tmp/<id>-enriched-us.md`.

4. El humano ejecuta en el chat del agente:
   ```
   /opsx:propose <id>
   ```
   OpenSpec genera `openspec/changes/<id>/` con los cuatro artefactos
   (`proposal.md`, `specs/`, `design.md`, `tasks.md`).  
   El agente puede usar `/opsx:explore <id>` primero si la idea necesita madurar.

5. **Pausa.** El humano revisa y aprueba `proposal.md` y `tasks.md`.
   Si hay correcciones: `/opsx:update <id>`.

6. El humano indica "aprobado". El leader cambia el status a `in_progress` y lanza `implementer`.

7. El implementer ejecuta:
   ```
   /opsx:apply <id>
   ```
   Trabaja `tasks.md` una tarea a la vez, marcándolas `[x]`.

8. El reviewer verifica trazabilidad requirements ↔ test ↔ tasks completas:
   ```
   /opsx:verify <id>
   ```
   Aprueba o rechaza. Si rechaza, el implementer corrige y vuelve al paso 7.

9. Si aprueba:
   ```
   /opsx:archive <id>
   ```
   Los delta specs se fusionan en `openspec/specs/`. El cambio se mueve a
   `openspec/changes/archive/YYYY-MM-DD-<id>/`.

10. El leader marca `done` en `feature_list.json` y mueve el resumen a
    `progress/history.md`.

### Comandos OpenSpec de referencia rápida

| Comando           | Dónde se ejecuta | Qué hace                                             |
|-------------------|------------------|------------------------------------------------------|
| `openspec init`   | Terminal         | Inicializa `openspec/` en el proyecto (una sola vez) |
| `openspec update` | Terminal         | Regenera instrucciones del agente (tras actualizaciones) |
| `/opsx:explore`   | Chat del agente  | Exploración libre antes de proponer un cambio        |
| `/opsx:propose`   | Chat del agente  | Crea el cambio con todos los artefactos de planning  |
| `/opsx:apply`     | Chat del agente  | Implementa las tareas de `tasks.md`                  |
| `/opsx:update`    | Chat del agente  | Revisa y mantiene coherentes los artefactos          |
| `/opsx:verify`    | Chat del agente  | Valida que la implementación coincide con el spec    |
| `/opsx:sync`      | Chat del agente  | Fusiona delta specs en `openspec/specs/` sin archivar |
| `/opsx:archive`   | Chat del agente  | Archiva el cambio completado                         |

> **Nota sobre sintaxis:** Kiro usa `/opsx:propose`. Cursor y Copilot usan `/opsx-propose`.
> Codex usa `$openspec-propose`. El comando correcto para tu herramienta se imprime al
> ejecutar `openspec init`.

---

## 5. Cierre de sesión (lifecycle)

Antes de terminar:

1. Ejecuta `./mvnw clean verify` — todo verde.
2. Si la tarea está acabada:
   - Ejecuta `/opsx:archive <id>` para consolidar los specs.
   - Marca `status: "done"` en `feature_list.json`.
3. Mueve el resumen de `progress/current.md` al final de `progress/history.md`.
4. Vacía `progress/current.md` dejando solo la plantilla.
5. No dejes archivos temporales, ni `System.out.println()` de debug, ni TODOs sin contexto.

---

## 6. Si te bloqueas

- Relee la sección relevante de `docs/`.
- Revisa `openspec/specs/` para entender el estado actual del sistema.
- Si la herramienta no hace lo que esperas, **no inventes un workaround**:
  documenta el bloqueo en `progress/current.md` y para la sesión.
- Marca `"status": "blocked"` en `feature_list.json` con la razón en `progress/current.md`.

---

## 7. OpenSpec — Instalación, estructura y uso

### 7.1 Instalación (una sola vez, por máquina)

Requiere Node.js 20.19.0 o superior.

```bash
npm install -g @fission-ai/openspec@latest
```

Para actualizar a la última versión:

```bash
npm install -g @fission-ai/openspec@latest
openspec update   # dentro del proyecto, regenera instrucciones del agente
```

### 7.2 Inicialización del proyecto (una sola vez, por repositorio)

```bash
cd /home/diego/workspace/tec-crmbot-ws
openspec init
```

Esto crea la estructura base:

```
openspec/
├── specs/               ← source of truth (specs consolidadas del sistema)
│   └── <domain>/
│       └── spec.md
├── changes/             ← cambios activos (uno por feature en vuelo)
│   └── <change-name>/
│       ├── proposal.md  ← el "qué" y el "por qué"
│       ├── specs/       ← delta specs (ADDED / MODIFIED / REMOVED)
│       │   └── <domain>/spec.md
│       ├── design.md    ← decisiones técnicas y arquitectura
│       └── tasks.md     ← checklist de implementación
└── config.yaml          ← configuración del proyecto (schema, profile)
```

### 7.3 Relación con la estructura existente del proyecto

```
tec-crmbot-ws/
├── ai-specs/
│   ├── agents/          ← agentes: leader, implementer, reviewer (sin cambios)
│   └── skills/
│       └── refine-stories/SKILL.md  ← skill de refinamiento (sin cambios)
├── openspec/            ← NUEVO: gestionado por OpenSpec CLI
│   ├── specs/           ← source of truth del sistema
│   ├── changes/         ← features en vuelo
│   └── config.yaml
├── tmp/                 ← historias enriquecidas por refine-stories
└── docs/                ← documentación del proyecto
```

`ai-specs/` y `openspec/` coexisten sin conflicto:
- `ai-specs/skills/` define el comportamiento de los agentes del proyecto.
- `openspec/` gestiona el ciclo de vida de cada feature (spec → implementación → archivo).

### 7.4 Configuración recomendada (`openspec/config.yaml`)

Tras `openspec init`, personaliza el archivo con el contexto del proyecto:

```yaml
project:
  name: "Smart Ventas IA - CRMBot"
  description: >
    Chatbot de ventas IA para PYMES Ecuador. Spring Boot 3.2 + React 18 +
    Google Gemini API + Firebase RTDB + Cloud Run.
  language: "es"

schema: spec-driven
profile: core
```

### 7.5 Flujo de artefactos OpenSpec

Cada feature activa tiene su propia carpeta bajo `openspec/changes/<id>/`:

| Artefacto       | Propósito                                                        |
|-----------------|------------------------------------------------------------------|
| `proposal.md`   | Intención, alcance y enfoque del cambio                          |
| `specs/`        | Delta specs en formato ADDED / MODIFIED / REMOVED               |
| `design.md`     | Decisiones técnicas: clases, endpoints, dependencias             |
| `tasks.md`      | Checklist de tareas `[ ]` → `[x]` que ejecuta `/opsx:apply`     |

Al archivar (`/opsx:archive`), los delta specs se fusionan en `openspec/specs/`
y la carpeta de cambio se mueve a `openspec/changes/archive/YYYY-MM-DD-<id>/`.

### 7.6 Comandos CLI útiles

```bash
openspec list                      # lista cambios activos
openspec show <change-name>        # detalle de un cambio
openspec validate <change-name>    # valida formato de specs
openspec view                      # dashboard interactivo
openspec config profile            # selecciona perfil (core / expanded)
```

### 7.7 Recursos

- Documentación oficial: https://github.com/Fission-AI/OpenSpec/tree/main/docs
- Comandos slash: https://github.com/Fission-AI/OpenSpec/blob/main/docs/commands.md
- Getting started: https://github.com/Fission-AI/OpenSpec/blob/main/docs/getting-started.md
