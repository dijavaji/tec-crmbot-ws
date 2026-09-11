# Tasks

## 1. Domain Model Implementation

- [x] 1.1 Create `ProcessingStatus` enum in `ec.com.technoloqie.crmbot.api.model` with values PENDING, PROCESSED, ERROR and verify it compiles.
- [x] 1.2 Implement `Sale` entity in `ec.com.technoloqie.crmbot.api.model` with UUID PK and all fields from `data-model.md`; verify compilation.
- [x] 1.3 Implement `SaleItem` entity in `ec.com.technoloqie.crmbot.api.model` with UUID PK and all fields from `data-model.md`; verify compilation.
- [x] 1.4 Implement `NotebookImage` entity in `ec.com.technoloqie.crmbot.api.model` with all fields from `data-model.md` and verify it compiles.

## 2. JPA Relationships and Validation

- [x] 2.1 Configure `@OneToMany` relationship between `Sale` and `SaleItem` with cascade and orphan removal; verify mapping in a unit test.
- [x] 2.2 Configure `@OneToMany` relationship between `Sale` and `NotebookImage`; verify mapping in a unit test.
- [x] 2.3 Create a `@DataJpaTest` in `src/test/java` that persists a full `Sale` object with multiple items and an image, then verifies successful retrieval and cascading delete.
