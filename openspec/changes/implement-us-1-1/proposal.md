# Proposal

## Why

Establishing a robust persistence layer is critical for storing sales data and image processing results. Using JPA entities ensures type-safe interaction with the MySQL database and maintains data integrity through defined relationships.

## What Changes

- **ADDED**: JPA entity `Sale` to represent a sales transaction.
- **ADDED**: JPA entity `SaleItem` to represent items within a sale.
- **ADDED**: JPA entity `NotebookImage` to track image metadata and processing status.
- **ADDED**: Enum `ProcessingStatus` for image lifecycle tracking.
- **ADDED**: JPA relationships (Sale @OneToMany SaleItem, Sale @OneToMany NotebookImage).

## Capabilities

### New Capabilities
- `sales/entities`: Defines the core domain entities for sales tracking and image management, including their JPA mappings and relationships.

### Modified Capabilities
<!-- No requirement changes to existing capabilities. -->

## Impact

- `ec.com.technoloqie.crmbot.api.model` package: New JPA entity classes.
- Database schema: MySQL tables mapped via JPA.
- Persistence logic: Standardized UUID generation and auditing fields.
