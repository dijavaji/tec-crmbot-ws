# Spec Delta

## Purpose

Defines the core domain entities for sales tracking and image management, including their attributes and relationships, ensuring proper persistence in the MySQL database.

## ADDED Requirements

### Requirement: Sale Persistence
The system SHALL persist sales information including business ID, user ID, sale date, sale time, total amount, item count, source, and average confidence.

#### Scenario: Successful Sale Persistence
- **WHEN** a new sale is created and saved
- **THEN** it is stored in the database with a unique UUID and all fields are correctly populated.

### Requirement: SaleItem Persistence
The system SHALL persist individual items linked to a sale, storing product name, quantity, total amount, and OCR confidence.

#### Scenario: Successful SaleItem Persistence
- **WHEN** items are associated with a sale and saved
- **THEN** they are persisted in the database with a foreign key referencing the parent sale.

### Requirement: NotebookImage Persistence
The system SHALL persist image metadata, including storage URL, processing status, and dimensions, linked to an optional sale.

#### Scenario: Successful Image Metadata Persistence
- **WHEN** image metadata is saved
- **THEN** it is stored in the database with a initial processing status of PENDING.

### Requirement: Cascade Operations
The system SHALL automatically persist or delete sale items when the parent sale is persisted or deleted.

#### Scenario: Cascade Delete Sale Items
- **WHEN** a sale is deleted
- **THEN** all associated sale items are also removed from the database.
