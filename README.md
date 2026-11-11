# WebDev 2 — Week 5: Application Architecture & Validation

**Course:** WEBDEV2  
**Term:** Prelim · **Week:** 5  
**Student:** geoxify (`geoffbenedict.carino@lorma.edu`)  
**Repository:** `https://github.com/geoxify/webdev2`  
**Branch:** `week5`  
**Submission URL:** `https://github.com/geoxify/webdev2/tree/week5`  

---

## 📌 Table of Contents
1. [Overview & Learning Outcomes](#-overview--learning-outcomes)
2. [Task 1 — Domain Selection & Requirements (`crud-app-spec.md`)](#-task-1--domain-selection--requirements)
3. [Task 2 — Validated Domain Model (`Product.java`)](#-task-2--validated-domain-model)
4. [Task 3 — Service & Repository Layers](#-task-3--service--repository-layers)
5. [Task 4 — Web Controller & Thymeleaf CRUD Views](#-task-4--web-controller--thymeleaf-crud-views)
6. [Architectural Overview: Controller-Service-Repository](#-architectural-overview)
7. [Automated Test Suite](#-automated-test-suite)
8. [Acceptance Criteria Checklist](#-acceptance-criteria-checklist)
9. [Build & Execution Guide](#-build--execution-guide)

---

## 🎯 Overview & Learning Outcomes
This project expands upon the Week 4 foundation by implementing a full Create, Read, Update, and Delete (CRUD) web application following the **Controller-Service-Repository** architectural pattern and applying **Jakarta Bean Validation**.

### Key Objectives Achieved:
- **Defined CRUD System Requirements:** Formalized single-resource requirements in `crud-app-spec.md` with explicit `REQ-<N>` statements covering the complete lifecycle.
- **Implemented Validated Domain Entity:** Structured `Product` with 6 fields and 5 distinct Jakarta Bean Validation constraints (`@NotBlank`, `@Size`, `@NotNull`, `@Min`, `@DecimalMin`, `@DecimalMax`) with custom error messages.
- **Enforced Separation of Concerns:** Built an in-memory repository (`InMemoryProductRepository`) implementing `ProductRepository` and encapsulated business logic inside `@Service ProductService`.
- **Delivered Interactive Thymeleaf CRUD Views:** Built templates for catalog listing (`products.html`), creation form (`product-form.html`), pre-populated update form (`product-edit.html`), and detailed inspection (`product-detail.html`).
- **Surfaced Inline Field Errors:** Captured validation failures via `BindingResult` and rendered inline error messages adjacent to invalid input fields using Thymeleaf `th:errors` and `th:classappend`.
- **Maintained Clean Git History:** Structured progression directly on the `week5` branch without subfolders, accompanied by granular commits for each task.

---

## 📋 Task 1 — Domain Selection & Requirements

The requirements specification is committed directly to the repository root as [`crud-app-spec.md`](crud-app-spec.md).

### Functional Requirements Summary:
- **REQ-1 (Create):** The system shall allow users to register and persist a new product record with validated attributes (`name`, `category`, `price`, `stockQuantity`, `description`), rejecting invalid inputs with inline Bean Validation error messages.
- **REQ-2 (Read):** The system shall allow users to retrieve and display all registered products in a tabular catalog, and inspect complete specifications for any individual product by its ID.
- **REQ-3 (Update):** The system shall allow users to modify an existing product's details using a pre-populated edit form, re-validating all fields before persisting updates.
- **REQ-4 (Delete):** The system shall allow users to remove an existing product record from the inventory catalog with immediate redirection and visual feedback.
- **REQ-5 (Validation):** The system shall enforce data integrity using Jakarta Bean Validation constraints and render inline error messages adjacent to invalid form controls.
- **REQ-6 (Layered Architecture):** The system shall enforce separation of concerns using the Controller-Service-Repository pattern.

---

## 🛡️ Task 2 — Validated Domain Model

The domain entity [`Product.java`](src/main/java/com/example/webdev2/Product.java) contains 6 fields and applies 5 distinct Bean Validation annotations with custom messages:

```java
public class Product {

    private Long id;

    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Category is required")
    @Size(min = 2, max = 50, message = "Category must be between 2 and 50 characters")
    private String category;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be at least $0.01")
    @DecimalMax(value = "99999.99", message = "Price cannot exceed $99,999.99")
    private Double price;

    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock quantity cannot be negative")
    @Max(value = 100000, message = "Stock quantity cannot exceed 100,000 units")
    private Integer stockQuantity;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
    
    // Constructors, Getters, Setters, toString, equals, and hashCode...
}
```

---

## ⚙️ Task 3 — Service & Repository Layers

Persistence and business operations are decoupled into clean architectural layers:

### 1. Repository Layer
- **Interface:** [`ProductRepository.java`](src/main/java/com/example/webdev2/ProductRepository.java)
- **Implementation:** [`InMemoryProductRepository.java`](src/main/java/com/example/webdev2/InMemoryProductRepository.java) annotated with `@Repository`
- **Data Structure:** Thread-safe `ConcurrentHashMap<Long, Product>` paired with an `AtomicLong` sequence generator.
- **Core Methods:**
  - `findAll()`: Returns all products sorted by ID.
  - `findById(Long id)`: Retrieves product by ID wrapped in `Optional<Product>`.
  - `save(Product product)`: Stores product and automatically assigns next sequence ID.
  - `update(Long id, Product product)`: Updates existing product or throws `NoSuchElementException`.
  - `deleteById(Long id)`: Removes product by ID, returning boolean status.
  - `reset()` / `clear()`: Helper methods to manage in-memory seed state.

### 2. Service Layer
- **Implementation:** [`ProductService.java`](src/main/java/com/example/webdev2/ProductService.java) annotated with `@Service`
- **Dependency Injection:** Injected with `ProductRepository` via constructor injection.
- **Responsibilities:** Validates business rules, delegates data persistence to repository, and isolates business logic from HTTP presentation details.

---

## 🌐 Task 4 — Web Controller & Thymeleaf CRUD Views

The presentation tier is implemented in [`ProductController.java`](src/main/java/com/example/webdev2/ProductController.java) and maps all CRUD operations:

| HTTP Method | Route | Description | View / Response |
|---|---|---|---|
| `GET` | `/products` | List all inventory products | `products.html` |
| `GET` | `/products/{id}` | View detailed product specifications | `product-detail.html` |
| `GET` | `/products/new` | Display new product creation form | `product-form.html` |
| `POST` | `/products` | Create product with `@Valid` check | Redirect to `/products` (or redisplay on error) |
| `GET` | `/products/edit/{id}` | Display pre-populated edit form | `product-edit.html` |
| `POST` | `/products/edit/{id}` | Update product with `@Valid` check | Redirect to `/products` (or redisplay on error) |
| `POST` | `/products/delete/{id}` | Delete product by identifier | Redirect to `/products` with flash alert |
| `GET` | `/products/reset` | Reset catalog to initial seed items | Redirect to `/products` with flash alert |

### View Templates:
1. **List View (`products.html`):** Formatted catalog table with product metrics (price, stock badges), quick action buttons (👁️ View, ✏️ Edit, 🗑️ Delete), empty-state message (`th:if`), and dismissible flash notification alerts.
2. **Create Form (`product-form.html`):** Clean form bound to entity backing object via `th:object="${product}"`, featuring inline field validation error indicators (`th:errors`, `th:classappend`).
3. **Edit Form (`product-edit.html`):** Pre-populated form bound to existing product details, re-validating upon submission and highlighting field-level errors inline.
4. **Detail View (`product-detail.html`):** Comprehensive specification card with breadcrumbs, formatted pricing, stock status, and direct Edit/Delete actions.

---

## 🏗️ Architectural Overview

```
[ Browser / HTTP Client ]
           │
           │  GET /products, POST /products, POST /products/edit/{id}, POST /products/delete/{id}
           ▼
┌─────────────────────────────────────────────────────────────┐
│ Web Layer: ProductController (@Controller)                  │
│ - Request mapping & HTTP parameter binding                  │
│ - @Valid annotation & BindingResult evaluation              │
│ - Model population & Thymeleaf template routing             │
│ - Post/Redirect/Get (PRG) pattern with RedirectAttributes   │
└──────────────────────────────┬──────────────────────────────┘
                               │ Calls service methods
                               ▼
┌─────────────────────────────────────────────────────────────┐
│ Business Layer: ProductService (@Service)                   │
│ - Coordinates domain logic & validation invariants          │
│ - Pure business rules independent of web/HTTP context       │
│ - Constructor-injected repository dependency                │
└──────────────────────────────┬──────────────────────────────┘
                               │ Calls repository contract
                               ▼
┌─────────────────────────────────────────────────────────────┐
│ Data Layer: InMemoryProductRepository (@Repository)         │
│ - Implements ProductRepository interface                    │
│ - Thread-safe storage with ConcurrentHashMap                │
│ - Atomic identifier generation with AtomicLong              │
└─────────────────────────────────────────────────────────────┘
```

---

## 🧪 Automated Test Suite

A comprehensive test suite of **26 automated tests** guarantees correctness and prevents regressions across all application layers:

1. **`ProductValidationTest` (5 tests):**
   - Validates that well-formed products pass all constraint checks.
   - Verifies `@NotBlank` and `@Size` violations on blank product names.
   - Verifies `@Min` violation on negative stock quantities.
   - Verifies `@NotNull` violation on missing price.
   - Verifies `@DecimalMin` violation on non-positive pricing.

2. **`ProductServiceTest` (8 tests):**
   - Verifies product listing, retrieval by existing/non-existent ID.
   - Verifies product creation and auto-incremented sequence ID assignment.
   - Verifies product update and `NoSuchElementException` on non-existent targets.
   - Verifies product deletion and boolean status return.

3. **`ProductControllerTest` (13 tests):**
   - Verifies `GET /products` returns `products` view with model data.
   - Verifies `GET /products?empty=true` renders empty catalog state.
   - Verifies `GET /products/{id}` renders detail view with model attributes.
   - Verifies `GET /products/{id}` handles not-found by redirecting with flash error.
   - Verifies `GET /products/new` renders create form with empty backing model.
   - Verifies `POST /products` succeeds with valid data and redirects (PRG).
   - Verifies `POST /products` catches validation errors and redisplays `product-form`.
   - Verifies `GET /products/edit/{id}` renders pre-populated edit form.
   - Verifies `POST /products/edit/{id}` updates record and redirects.
   - Verifies `POST /products/edit/{id}` redisplays `product-edit` on validation errors.
   - Verifies `POST /products/delete/{id}` deletes product and redirects with status.

---

## ✅ Acceptance Criteria Checklist

| Requirement | Status | Evidence / Verification |
|---|---|---|
| **Domain Selected & Requirements Documented** | ✅ Complete | [`crud-app-spec.md`](crud-app-spec.md) created at repository root with `REQ-1` to `REQ-6` |
| **Validated Domain Entity (≥4 fields, ≥3 constraints)** | ✅ Complete | [`Product.java`](src/main/java/com/example/webdev2/Product.java) has 6 fields and 5 distinct constraints with custom messages |
| **Repository Layer (In-Memory Storage)** | ✅ Complete | [`ProductRepository.java`](src/main/java/com/example/webdev2/ProductRepository.java) & [`InMemoryProductRepository.java`](src/main/java/com/example/webdev2/InMemoryProductRepository.java) |
| **Service Layer (`@Service`)** | ✅ Complete | [`ProductService.java`](src/main/java/com/example/webdev2/ProductService.java) marked with `@Service` and constructor injection |
| **Web Controller (`@Controller`)** | ✅ Complete | [`ProductController.java`](src/main/java/com/example/webdev2/ProductController.java) handles all CRUD routes & `@Valid` |
| **List View (Read)** | ✅ Complete | [`products.html`](src/main/resources/templates/products.html) with table, actions, and empty state |
| **Create Form (Create)** | ✅ Complete | [`product-form.html`](src/main/resources/templates/product-form.html) with inline Bean Validation errors |
| **Edit Form (Update)** | ✅ Complete | [`product-edit.html`](src/main/resources/templates/product-edit.html) pre-populated with inline errors |
| **Delete Action (Delete)** | ✅ Complete | Handled via POST route with confirmation prompt and redirect |
| **Clean Git Commits (≥4 commits on `week5`)** | ✅ Complete | 4+ descriptive commits directly on branch `week5` |

---

## 🚀 Build & Execution Guide

### Prerequisites
- Java Development Kit (JDK) 17+ (Tested on Java 24)
- Maven Wrapper (`mvnw` / `mvnw.cmd` included)

### Compile and Run Tests
```powershell
.\mvnw.cmd test
```

### Start the Spring Boot Web Server
```powershell
.\mvnw.cmd spring-boot:run
```

Once running, access the application in your browser:
- **Product Catalog (Read List):** `http://localhost:8080/products`
- **New Product Form (Create):** `http://localhost:8080/products/new`
- **Product Detail (Read One):** `http://localhost:8080/products/1`
- **Edit Product (Update):** `http://localhost:8080/products/edit/1`
- **Reset Catalog:** `http://localhost:8080/products/reset`
- **Clear Catalog:** `http://localhost:8080/products/clear`
