# Design

## Context

Implementation of the core JPA persistence layer. See `proposal.md` for motivation. The project is a Spring Boot 3.2.0 application using Java 17 and MySQL.

## Goals / Non-Goals

**Goals:**
- Map the domain entities (`Sale`, `SaleItem`, `NotebookImage`) to MySQL tables.
- Establish JPA relationships between entities.
- Ensure all fields from `data-model.md` are represented.
- Adhere to `backend-standards.md` (e.g., using Tabs, proper naming).

**Non-Goals:**
- Implementation of Service layer or Controllers.
- Database migration scripts (handled in US-1.3).

## Decisions

### 1. Primary Key Generation
- **Decision:** Use `UUID` with Hibernate's `@GeneratedValue(strategy = GenerationType.UUID)`.
- **Rationale:** Consistent with `data-model.md`. Hibernate 6+ provides native support for UUID generation.
- **Alternatives:** `@GenericGenerator` (older approach), Long IDs (rejected).

### 2. Relationship Management (Sale -> SaleItem)
- **Decision:** Bi-directional `@OneToMany` with `mappedBy` in `Sale` and `@ManyToOne` in `SaleItem`. Use `CascadeType.ALL` and `orphanRemoval = true`.
- **Rationale:** Allows easy access to items from a sale and ensures items are cleaned up if the sale is deleted or if they are removed from the collection.

### 3. Relationship Management (Sale -> NotebookImage)
- **Decision:** Uni-directional `@OneToMany` from `Sale` to `NotebookImage`.
- **Rationale:** A sale can have multiple images associated with it, but an image doesn't necessarily need to know about the sale in a bi-directional way for current requirements.

### 4. Auditing and Metadata
- **Decision:** Use `@Column(updatable = false)` for `createdAt` and `uploadedAt`.
- **Rationale:** These timestamps should only be set once upon creation.

## Risks / Trade-offs

- **[Risk] N+1 Query Problem** → **Mitigation**: Use `FetchType.LAZY` for all collections and ensure service layer uses proper join fetching when needed.
- **[Risk] Precision Loss** → **Mitigation**: Use `BigDecimal` for `totalAmount` with explicit scale/precision in `@Column`.
