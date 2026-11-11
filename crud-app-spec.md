# Product Inventory Management System — Specification

**Course:** WEBDEV2  
**Term:** Prelim  
**Week:** 5 — Application Architecture & Validation  
**Repository Branch:** `week5`  

---

## 1. Project Description

The **Product Inventory Management System** is a Spring Boot web application designed for retail and warehouse operations to maintain an accurate, centralized catalog of merchandise and stock levels. The system allows inventory managers to register new items, track stock quantities and pricing, inspect individual product specifications, revise product information, and retire decommissioned stock.

The application expands upon the Week 4 foundation by restructuring the application into a robust, layered architecture following the **Controller-Service-Repository** design pattern and enforcing strict input validation using **Jakarta Bean Validation**.

---

## 2. Functional Requirements

The following requirements govern the functional capabilities of the system across the complete Create, Read, Update, and Delete (CRUD) lifecycle:

- **REQ-1 (Create):** The system shall allow users to create and persist a new product record by submitting valid product attributes (name, category, unit price, stock quantity, and optional description) through a web form. Submissions with invalid or missing data must be rejected with descriptive inline validation error messages.
- **REQ-2 (Read):** The system shall allow users to retrieve and display all registered products in a structured catalog table, as well as inspect the complete details of any individual product by its unique identifier (`id`).
- **REQ-3 (Update):** The system shall allow users to modify the attributes of an existing product record through a pre-populated edit form, re-validating all updated fields against domain constraints before saving changes.
- **REQ-4 (Delete):** The system shall allow users to remove an existing product record from the inventory catalog by its unique identifier, with immediate redirection and visual feedback on the updated product list.
- **REQ-5 (Validation):** The system shall apply Jakarta Bean Validation constraints (`@NotBlank`, `@Size`, `@NotNull`, `@Min`, `@DecimalMin`, `@DecimalMax`) on domain entity fields, capture binding errors via `BindingResult`, and render actionable error messages adjacent to invalid form controls.
- **REQ-6 (Layered Architecture):** The system shall enforce separation of concerns using the Controller-Service-Repository pattern such that:
  - The **Controller** handles HTTP requests, binds parameters, validates input, and selects Thymeleaf views.
  - The **Service** layer coordinates business rules and transaction-like workflow operations.
  - The **Repository** layer encapsulates data persistence operations and in-memory storage.

---

## 3. Domain Model Specification

The core domain entity is `Product`, configured with the following schema and validation constraints:

| Field | Type | Validation Constraints | Description |
|---|---|---|---|
| `id` | `Long` | Unique identifier (System-assigned) | Primary key assigned on persistence |
| `name` | `String` | `@NotBlank(message = "Product name is required")`<br>`@Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")` | Display name of the product |
| `category` | `String` | `@NotBlank(message = "Category is required")`<br>`@Size(min = 2, max = 50, message = "Category must be between 2 and 50 characters")` | Product classification / department |
| `price` | `Double` | `@NotNull(message = "Price is required")`<br>`@DecimalMin(value = "0.01", message = "Price must be at least $0.01")`<br>`@DecimalMax(value = "99999.99", message = "Price cannot exceed $99,999.99")` | Unit price in USD |
| `stockQuantity` | `Integer` | `@NotNull(message = "Stock quantity is required")`<br>`@Min(value = 0, message = "Stock quantity cannot be negative")`<br>`@Max(value = 100000, message = "Stock quantity cannot exceed 100,000 units")` | Units currently available in stock |
| `description` | `String` | `@Size(max = 500, message = "Description cannot exceed 500 characters")` | Extended product details / technical specifications |

---

## 4. Architectural Layers

```
+-------------------------------------------------------------+
|                      Client / Browser                       |
+-------------------------------------------------------------+
                               |  HTTP (GET, POST)
                               v
+-------------------------------------------------------------+
|     Web Layer: ProductController (@Controller)              |
|     - Maps routes: /products, /products/{id}, /products/new |
|                    /products/edit/{id}, /products/delete/{id}
|     - Binds forms to @Valid Product & evaluates BindingResult|
|     - Renders Thymeleaf views or issues Post/Redirect/Get   |
+-------------------------------------------------------------+
                               |  Calls Service APIs
                               v
+-------------------------------------------------------------+
|     Service Layer: ProductService / ProductServiceImpl      |
|     - Annotated with @Service                               |
|     - Enforces business logic and validation rules           |
|     - Delegates persistence to ProductRepository            |
+-------------------------------------------------------------+
                               |  Calls Repository APIs
                               v
+-------------------------------------------------------------+
|     Repository Layer: ProductRepository / InMemoryRepo     |
|     - Annotated with @Repository                            |
|     - Thread-safe in-memory store (ConcurrentHashMap)        |
|     - CRUD methods: findAll, findById, save, update, delete |
+-------------------------------------------------------------+
```

---

## 5. CRUD HTTP Route Mapping

| HTTP Method | Route | Purpose | View Template / Redirect |
|---|---|---|---|
| `GET` | `/products` | List all inventory products | `products.html` |
| `GET` | `/products/{id}` | View detailed product specifications | `product-detail.html` |
| `GET` | `/products/new` | Display empty form to create a new product | `product-form.html` |
| `POST` | `/products` | Process creation of new product | Redirect to `/products` (or redisplay form on validation error) |
| `GET` | `/products/edit/{id}` | Display pre-populated edit form for existing product | `product-form.html` (or `product-edit.html`) |
| `POST` | `/products/edit/{id}` | Process update of existing product | Redirect to `/products` (or redisplay form on validation error) |
| `POST` | `/products/delete/{id}` | Delete product by identifier | Redirect to `/products` |
| `GET` | `/products/reset` | Reset repository to default sample dataset | Redirect to `/products` |
