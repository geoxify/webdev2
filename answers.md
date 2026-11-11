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
2. [Project Architecture & Directory Structure](#-project-architecture--directory-structure)
3. [Task 1 — Domain Selection & Requirements (`crud-app-spec.md`)](#-task-1--domain-selection--requirements)
4. [Task 2 — Validated Domain Model (`Product.java`)](#-task-2--validated-domain-model)
5. [Task 3 — Service & Repository Layers](#-task-3--service--repository-layers)
6. [Task 4 — Web Controller & Thymeleaf CRUD Views](#-task-4--web-controller--thymeleaf-crud-views)
7. [In-Depth Architectural Analysis: Controller-Service-Repository](#-in-depth-architectural-analysis)
8. [Automated Verification & Unit/Integration Tests](#-automated-verification)
9. [Acceptance Criteria Checklist](#-acceptance-criteria-checklist)
10. [Build & Execution Guide](#-build--execution-guide)

---

## 🎯 Overview & Learning Outcomes
This lab focuses on the implementation of enterprise-grade software architecture in Spring Boot by applying the **Controller-Service-Repository** pattern and **Jakarta Bean Validation**.

Expanding on the previous codebase, the single-resource system manages a **Product Inventory Catalog** with full Create, Read, Update, and Delete (CRUD) capabilities, complete separation of concerns, and robust client/server-side validation with inline error messaging.

### Key Objectives Achieved:
1. **Domain Specification:** Created `crud-app-spec.md` with explicit `REQ-<N>` statements covering all CRUD operations.
2. **Validated Entity Model:** Configured `Product` with 6 fields and 5 distinct Jakarta Bean Validation annotations (`@NotBlank`, `@Size`, `@NotNull`, `@Min`, `@DecimalMin`, `@DecimalMax`) with custom feedback messages.
3. **Layered Separation of Concerns:**
   - **Repository:** Thread-safe in-memory storage (`InMemoryProductRepository`) implementing `ProductRepository`.
   - **Service:** Business coordination layer (`ProductService`) annotated with `@Service`.
   - **Controller:** Presentation layer (`ProductController`) annotated with `@Controller`.
4. **Interactive Thymeleaf CRUD Views:**
   - `products.html`: Tabular listing with empty state and quick action buttons.
   - `product-form.html`: Creation form with inline field-level validation messages.
   - `product-edit.html`: Pre-populated update form with re-validation and error handling.
   - `product-detail.html`: Detailed item view with breadcrumbs and actions.
5. **Git Branching Integrity:** Created and committed all work directly on the `week5` branch without subfolders, maintaining clean, atomic commits for each task.

---

## 📁 Project Architecture & Directory Structure

```text
webdev2/
├── .gitattributes
├── .gitignore
├── HELP.md
├── mvnw
├── mvnw.cmd
├── pom.xml
├── crud-app-spec.md                <-- Task 1: Domain & CRUD Specification
├── README.md                       <-- Project README for Week 5
├── answers.md                      <-- Comprehensive Week 5 Lab Report
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── webdev2/
│   │   │               ├── Webdev2Application.java
│   │   │               ├── HomeController.java
│   │   │               ├── Product.java                     <-- Task 2: Validated Domain Model
│   │   │               ├── ProductRepository.java           <-- Task 3: Repository Interface
│   │   │               ├── InMemoryProductRepository.java   <-- Task 3: In-Memory Implementation
│   │   │               ├── ProductService.java              <-- Task 3: Business Service Layer
│   │   │               └── ProductController.java           <-- Task 4: Web Controller
│   │   └── resources/
│   │       ├── application.properties
│   │       └── templates/
│   │           ├── products.html                            <-- Task 4: List View (Read)
│   │           ├── product-form.html                        <-- Task 4: Create Form (Create)
│   │           ├── product-edit.html                        <-- Task 4: Edit Form (Update)
│   │           ├── product-detail.html                      <-- Task 4: Detail View (Read)
│   │           └── fragments/
│   │               └── navbar.html                          <-- Reusable Navbar Fragment
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── webdev2/
│                       ├── ProductValidationTest.java       <-- Task 2: Bean Validation Tests
│                       ├── ProductServiceTest.java          <-- Task 3: Service Layer Tests
│                       └── ProductControllerTest.java       <-- Task 4: Controller CRUD Tests
```

---

## 📋 Task 1 — Domain Selection & Requirements

The requirements specification is stored in `crud-app-spec.md` at the root directory of the repository.

### Functional Requirements:
- **`REQ-1 (Create):`** The system shall allow users to create and persist a new product record by submitting valid product attributes (`name`, `category`, `price`, `stockQuantity`, `description`) through a web form. Submissions with invalid or missing data must be rejected with descriptive inline validation error messages.
- **`REQ-2 (Read):`** The system shall allow users to retrieve and display all registered products in a structured catalog table, as well as inspect the complete details of any individual product by its unique identifier (`id`).
- **`REQ-3 (Update):`** The system shall allow users to modify the attributes of an existing product record through a pre-populated edit form, re-validating all updated fields against domain constraints before saving changes.
- **`REQ-4 (Delete):`** The system shall allow users to remove an existing product record from the inventory catalog by its unique identifier, with immediate redirection and visual feedback on the updated product list.
- **`REQ-5 (Validation):`** The system shall apply Jakarta Bean Validation constraints (`@NotBlank`, `@Size`, `@NotNull`, `@Min`, `@DecimalMin`, `@DecimalMax`) on domain entity fields, capture binding errors via `BindingResult`, and render actionable error messages adjacent to invalid form controls.
- **`REQ-6 (Layered Architecture):`** The system shall enforce separation of concerns using the Controller-Service-Repository pattern.

---

## 🛡️ Task 2 — Validated Domain Model

The core domain model is implemented in `Product.java`.

### Constraints Table:

| Field | Type | Bean Validation Annotations | Custom Error Message |
|---|---|---|---|
| `id` | `Long` | Primary identifier (system assigned) | N/A |
| `name` | `String` | `@NotBlank`<br>`@Size(min = 2, max = 100)` | `"Product name is required"`<br>`"Product name must be between 2 and 100 characters"` |
| `category` | `String` | `@NotBlank`<br>`@Size(min = 2, max = 50)` | `"Category is required"`<br>`"Category must be between 2 and 50 characters"` |
| `price` | `Double` | `@NotNull`<br>`@DecimalMin(value = "0.01")`<br>`@DecimalMax(value = "99999.99")` | `"Price is required"`<br>`"Price must be at least $0.01"`<br>`"Price cannot exceed $99,999.99"` |
| `stockQuantity` | `Integer` | `@NotNull`<br>`@Min(value = 0)`<br>`@Max(value = 100000)` | `"Stock quantity is required"`<br>`"Stock quantity cannot be negative"`<br>`"Stock quantity cannot exceed 100,000 units"` |
| `description` | `String` | `@Size(max = 500)` | `"Description must not exceed 500 characters"` |

### Distinct Annotations Count:
1. `@NotBlank`
2. `@Size`
3. `@NotNull`
4. `@Min` / `@Max`
5. `@DecimalMin` / `@DecimalMax`
*(Exceeds the requirement of at least 3 distinct annotations on at least 4 fields).*

---

## ⚙️ Task 3 — Service & Repository Layers

### 1. Repository Interface & In-Memory Storage
`ProductRepository.java` defines clean data operations:
```java
public interface ProductRepository {
    List<Product> findAll();
    Optional<Product> findById(Long id);
    Product save(Product product);
    Product update(Long id, Product product);
    boolean deleteById(Long id);
    boolean existsById(Long id);
    void reset();
    void clear();
}
```

`InMemoryProductRepository.java` implements this contract with thread-safety:
- Uses `ConcurrentHashMap<Long, Product>` to prevent race conditions during concurrent web requests.
- Employs `AtomicLong` for unique identifier generation.
- Pre-seeds 5 initial catalog items on instantiation and reset.

### 2. Service Layer
`ProductService.java` is marked with Spring's `@Service` stereotype annotation:
- Encapsulates domain logic and validation checks (e.g., non-null checks, ensuring existing IDs prior to updates).
- Injects `ProductRepository` via constructor injection:
```java
@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    // Business methods delegating to repository...
}
```

---

## 🌐 Task 4 — Web Controller & Thymeleaf CRUD Views

### 1. Controller Routes Mapping (`ProductController.java`)
- **List (Read):** `GET /products` -> returns `"products"`
- **Detail (Read):** `GET /products/{id}` -> returns `"product-detail"` (or redirects if not found)
- **Create Form:** `GET /products/new` -> returns `"product-form"` with fresh `Product` model
- **Create Action:** `POST /products` -> validates with `@Valid`, redisplays `"product-form"` if `bindingResult.hasErrors()`, or redirects to `/products` with flash success message
- **Edit Form (Update):** `GET /products/edit/{id}` -> returns `"product-edit"` pre-populated with existing entity
- **Edit Action (Update):** `POST /products/edit/{id}` -> validates with `@Valid`, redisplays `"product-edit"` if `bindingResult.hasErrors()`, or updates and redirects
- **Delete Action:** `POST /products/delete/{id}` -> deletes entity and redirects to `/products` with status message
- **Reset / Clear Helpers:** `GET /products/reset` and `GET /products/clear`

### 2. Thymeleaf Views
- **`products.html`:** Formatted catalog table with product metrics, category badges, stock levels, quick action buttons (`View`, `Edit`, `Delete`), empty state message (`th:if`), and flash notification alerts.
- **`product-form.html`:** Clean creation form with inline field errors rendered using `th:errors` and inputs dynamically styled with `th:classappend="${#fields.hasErrors('fieldName')} ? 'input-error' : ''"`.
- **`product-edit.html`:** Pre-populated edit form bound to existing product details with inline validation display.
- **`product-detail.html`:** Full specification view with formatted price, stock status, breadcrumb navigation, and direct Edit/Delete action buttons.

---

## 🏗️ In-Depth Architectural Analysis

### Why Use the Controller-Service-Repository Pattern?
1. **Separation of Concerns:** Each layer has a singular responsibility:
   - *Controller:* HTTP protocol translation, request parameter binding, validation checking, and view selection.
   - *Service:* Business workflow rules and logic transactions.
   - *Repository:* Raw persistence mechanism and query execution.
2. **Testability:** Decoupled layers can be tested in complete isolation. The service layer can be tested without an HTTP server, and the repository can be swapped (e.g., from in-memory to JPA/Hibernate) without touching controller logic.
3. **Maintainability & Reusability:** Business rules in `ProductService` can be reused across multiple interfaces (REST APIs, Web controllers, CLI tools) without duplicating validation or business calculations.

---

## 🧪 Automated Verification

All 26 automated tests pass with 0 failures and 0 errors:

```text
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.example.webdev2.ProductControllerTest
[INFO] Tests run: 13, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running com.example.webdev2.ProductServiceTest
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running com.example.webdev2.ProductValidationTest
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 26, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] BUILD SUCCESS
```

---

## ✅ Acceptance Criteria Checklist

| Requirement | Status | Evidence |
|---|---|---|
| Domain selection and ≥4 CRUD requirements documented | ✅ Complete | `crud-app-spec.md` at repository root |
| Domain model with ≥4 fields and ≥3 distinct Bean Validation annotations | ✅ Complete | `Product.java` has 6 fields and 5 distinct constraints |
| Repository layer with in-memory storage methods | ✅ Complete | `ProductRepository.java` & `InMemoryProductRepository.java` |
| Service layer marked with `@Service` encapsulating business logic | ✅ Complete | `ProductService.java` |
| Web Controller handling all CRUD routes and `@Valid` checks | ✅ Complete | `ProductController.java` |
| Thymeleaf List view (Read) with links to add, edit, or delete | ✅ Complete | `products.html` |
| Thymeleaf Create form with inline Bean Validation errors | ✅ Complete | `product-form.html` |
| Thymeleaf Edit form (Update) pre-populated with validation | ✅ Complete | `product-edit.html` |
| Delete action removing item from repository | ✅ Complete | `POST /products/delete/{id}` in `ProductController` |
| Work committed directly to `week5` branch without subfolders | ✅ Complete | Clean git log on `week5` |

---

## 🚀 Build & Execution Guide

### Compile and Run Test Suite
```powershell
.\mvnw.cmd test
```

### Run the Application
```powershell
.\mvnw.cmd spring-boot:run
```

Access the application in your browser at `http://localhost:8080/products`.
