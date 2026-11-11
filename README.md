# WebDev 2 — Week 3: Spring MVC Controller Application

**Term:** Prelim · **Week:** 3  
**Course:** WebDev 2  
**Student:** geoxify (`geoffbenedict.carino@lorma.edu`)  
**Repository:** `webdev2`  
**Branch:** `week3`  

---

## 📌 Table of Contents
1. [Overview & Learning Outcomes](#-overview--learning-outcomes)
2. [Project Structure](#-project-structure)
3. [Implementation Details (Tasks 1–5)](#%EF%B8%8F-implementation-details)
   - [Task 1: List and Detail Endpoints](#task-1--list-and-detail-endpoints-easy)
   - [Task 2: Filtering with Request Parameters](#task-2--filtering-with-request-parameters-easymedium)
   - [Task 3: Model-Driven Detail View](#task-3--model-driven-detail-view-medium)
   - [Task 4: Create Endpoint with Post/Redirect/Get](#task-4--create-endpoint-with-postredirectget-medium)
   - [Task 5: Meaningful Status Codes](#task-5--meaningful-status-codes-hard)
4. [Spring MVC Controller & Request Routing Architecture](#-spring-mvc-controller--request-routing-architecture)
5. [API Endpoint Reference](#-api-endpoint-reference)
6. [Build & Execution Guide](#-build--execution-guide)
7. [Port Configuration](#-port-configuration)
8. [Evidence & Screenshots](#-evidence--screenshots)
9. [Acceptance Criteria Checklist](#-acceptance-criteria-checklist)

---

## 🎯 Overview & Learning Outcomes
This project implements a Spring MVC web controller application handling HTTP requests, data extraction from path variables and query parameters, Model attribute binding, the Post/Redirect/Get (PRG) design pattern, and explicit HTTP status codes using `ResponseEntity` and `@ResponseStatus`.

Key concepts demonstrated:
- **DispatcherServlet & Request Mapping:** Directing incoming HTTP requests to controller handler methods via `@Controller`, `@RequestMapping("/books")`, `@GetMapping`, and `@PostMapping`.
- **Path Variables:** Extracting URI path segments using `@PathVariable Long id` to identify specific book resources.
- **Query Parameters & Optional Filtering:** Reading query strings with `@RequestParam(required = false) String author` to support selective filtering.
- **Model Attribute Binding:** Populating Spring's `Model` interface via `model.addAttribute("book", book)` and routing to logical view identifiers (`book-detail`).
- **Post/Redirect/Get (PRG) Pattern:** Eliminating duplicate form resubmission vulnerabilities by responding to successful `POST` requests with a `302 Found` redirect (`"redirect:/books"`).
- **Explicit HTTP Status Codes:** Returning `ResponseEntity<Book>` with `200 OK` or `404 Not Found`, alongside `@ResponseStatus(HttpStatus.CREATED)` (HTTP 201) for resource creation.

---

## 📁 Project Structure

```text
webdev2/
├── .gitattributes
├── .gitignore
├── HELP.md
├── mvnw
├── mvnw.cmd
├── pom.xml
├── README.md
├── screenshot/
│   ├── task1_list_and_detail_endpoints.png
│   ├── task2_all_books_omitted_param.png
│   ├── task2_filtered_by_author.png
│   ├── task3_model_driven_detail_view.png
│   ├── task4_post_redirect_get.png
│   ├── task5_post_status_201_created.png
│   ├── task5_status_200_valid_id.png
│   └── task5_status_404_invalid_id.png
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── webdev2/
│   │   │               ├── Book.java
│   │   │               ├── BookController.java
│   │   │               └── Webdev2Application.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── webdev2/
│                       └── Webdev2ApplicationTests.java
```

---

## 🛠️ Implementation Details

### Task 1 — List and Detail Endpoints _(Easy)_
- Created `Book.java` domain model containing `id` (Long), `title` (String), `author` (String), and `price` (double).
- Created `BookController.java` annotated with `@Controller` and class-level `@RequestMapping("/books")`.
- Implemented an in-memory `List<Book>` pre-populated with initial book records.
- Added `GET /books` mapped to `getAllBooks()` returning the list of books.
- Added `GET /books/{id}` mapped to `getBookById(@PathVariable Long id)` to retrieve a specific book by its URL path variable.

### Task 2 — Filtering with Request Parameters _(Easy–Medium)_
- Enhanced `GET /books` with `@RequestParam(required = false) String author`.
- When `author` is specified, filters the list using Java streams and case-insensitive matching (`toLowerCase().contains(...)`).
- When `author` is omitted (null or blank), the endpoint returns the complete book collection.

### Task 3 — Model-Driven Detail View _(Medium)_
- Updated the detail endpoint to accept Spring's `Model` parameter:
  ```java
  model.addAttribute("book", book);
  return "book-detail";
  ```
- Provided a dedicated view method `getBookDetailView(@PathVariable Long id, Model model)` returning the logical view name `"book-detail"` with the populated model attribute.

### Task 4 — Create Endpoint with Post/Redirect/Get _(Medium)_
- Added `POST /books` endpoint that accepts new book data via form submission or JSON payload.
- Automatically computes the next sequential ID and appends the book to the in-memory list.
- Implements the **Post/Redirect/Get (PRG)** pattern by returning `"redirect:/books"`, directing the client to re-issue a `GET` request and avoiding duplicate submissions on browser refresh.

### Task 5 — Meaningful Status Codes _(Hard)_
- Refactored `GET /books/{id}` to return `ResponseEntity<Book>`:
  - If the requested ID matches a book: returns `ResponseEntity.ok(book)` (`200 OK`).
  - If the requested ID does not exist: returns `ResponseEntity.notFound().build()` (`404 Not Found`).
- Annotated the `POST /books` create endpoint with `@ResponseStatus(HttpStatus.CREATED)` so successful creation emits HTTP status `201 Created` with a `Location: /books` header.

---

## 🏛️ Spring MVC Controller & Request Routing Architecture

```text
HTTP Request
     │
     ▼
DispatcherServlet (Spring MVC Front Controller)
     │
     ├─► Match HandlerMapping (/books/**)
     │
     ▼
BookController (@Controller, base: /books)
     │
     ├── GET  /books                 ──► getAllBooks(@RequestParam(required=false) author)
     │                                    └─► 200 OK (List<Book>)
     │
     ├── GET  /books/{id}            ──► getBookById(@PathVariable id, Model model)
     │                                    ├─► ID Found:     200 OK (Book)
     │                                    └─► ID Not Found: 404 Not Found
     │
     ├── GET  /books/{id}/view       ──► getBookDetailView(@PathVariable id, Model model)
     │                                    └─► model.addAttribute("book", book) ──► "book-detail"
     │
     └── POST /books                 ──► createBook(Book book) [@ResponseStatus(HttpStatus.CREATED)]
                                          └─► Saves Book ──► redirect:/books (PRG Pattern)
```

---

## 🌐 API Endpoint Reference

| Method | Endpoint | Parameters / Body | Response Status | Description |
|---|---|---|---|---|
| `GET` | `/books` | `author` (optional query string) | `200 OK` | Retrieves all books or filters by author. |
| `GET` | `/books/{id}` | `id` (path variable, Long) | `200 OK` / `404 Not Found` | Retrieves a book by ID wrapped in `ResponseEntity`. |
| `GET` | `/books/{id}/view` | `id` (path variable, Long) | `200 OK` | Populates `Model` with `"book"` attribute and returns view `"book-detail"`. |
| `POST` | `/books` | Form or JSON (`title`, `author`, `price`) | `201 Created` (302 redirect) | Creates a new book and redirects to `/books`. |

---

## 🚀 Build & Execution Guide

### Prerequisites
- Java Development Kit (JDK) 17 or higher
- Maven Wrapper (`mvnw.cmd` included in repository)

### 1. Build the Application
```powershell
.\mvnw.cmd clean package
```

### 2. Run the Application
```powershell
.\mvnw.cmd spring-boot:run
```
Or run the packaged JAR directly:
```powershell
java -jar target/webdev2-0.0.1-SNAPSHOT.jar
```

---

## 🌐 Port Configuration
The application is configured to run on port **`8082`**:
- Configured in: `src/main/resources/application.properties`
- Setting: `server.port=8082`
- Context URL: `http://localhost:8082/books`

---

## 📷 Evidence & Screenshots

| Screenshot | Description | File Path |
|---|---|---|
| **Task 1: List & Detail Endpoints** | Verifying `GET /books` (all books) and `GET /books/1` (detail by path variable). | `screenshot/task1_list_and_detail_endpoints.png` |
| **Task 2: Author Filtering** | Request with `?author=Tolkien` returning only matching author books. | `screenshot/task2_filtered_by_author.png` |
| **Task 2: Full Catalog** | Request without author parameter returning the complete list of books. | `screenshot/task2_all_books_omitted_param.png` |
| **Task 3: Model Detail View** | Source implementation demonstrating `model.addAttribute("book", book)` and returning `"book-detail"`. | `screenshot/task3_model_driven_detail_view.png` |
| **Task 4: Post/Redirect/Get** | Successful `POST /books` resulting in a `302 Found` redirect followed by `GET /books`. | `screenshot/task4_post_redirect_get.png` |
| **Task 5: Status 200 OK** | Detail endpoint returning HTTP `200 OK` with `ResponseEntity<Book>` for a valid ID. | `screenshot/task5_status_200_valid_id.png` |
| **Task 5: Status 404 Not Found** | Detail endpoint returning explicit HTTP `404 Not Found` for an invalid ID. | `screenshot/task5_status_404_invalid_id.png` |
| **Task 5: Status 201 Created** | Create endpoint returning HTTP `201 Created` with `Location: /books` header. | `screenshot/task5_post_status_201_created.png` |

---

## ✅ Acceptance Criteria Checklist

- [x] **Task 1:** `BookController` created with `@Controller` and base path `/books`; `GET /books` lists all books; `GET /books/{id}` extracts the path variable and retrieves the book.
- [x] **Task 2:** `@RequestParam(required = false)` parameter `author` properly filters results when present and returns all books when omitted.
- [x] **Task 3:** `Model` carries the found book under the key `"book"`, and the logical view name `"book-detail"` is returned.
- [x] **Task 4:** `POST /books` accepts new book data, appends it to the in-memory store, and redirects to `GET /books` via the Post/Redirect/Get pattern.
- [x] **Task 5:** Refactored `GET /books/{id}` returns `ResponseEntity<Book>` yielding `200 OK` for valid IDs and `404 Not Found` for missing IDs; create endpoint returns `201 Created`.
