# Task Tracker — WEBDEV2 Prelim Lab Exam

**Course:** WEBDEV2 (Java/Spring Boot Full-Stack Web Development)  
**Assessment:** Prelim Lab Exam  
**Student:** geoxify (`geoffbenedict.carino@lorma.edu`)  
**Repository:** `https://github.com/geoxify/webdev2`  
**Branch:** `prelim`  
**Walkthrough Video (Task 6):** [https://youtu.be/FrRgu0Lc7YQ](https://youtu.be/FrRgu0Lc7YQ) (YouTube Unlisted)

---

## 📋 Overview & Objectives

The **Task Tracker** is a Spring Boot web application for managing personal tasks. The project was built from scratch following enterprise design standards across Weeks 1–5 of the course:

- **Layered Architecture:** Strict separation of concerns following the **Controller–Service–Repository** pattern.
- **Dependency Injection:** Constructor-based dependency injection with Spring IoC container.
- **RESTful MVC Routing:** Clean URL routing for task listing, detail viewing, creation, and friendly error states.
- **Server-Side Rendering:** Dynamic HTML templates powered by **Thymeleaf** with standalone HTML validity.
- **Bean Validation:** Jakarta Bean Validation (`@NotBlank`, `@Size`) with field-level inline error binding (`BindingResult`, `th:errors`).
- **Custom Exception Handling:** Centralized controller advice / `@ExceptionHandler` mapping `TaskNotFoundException` to a user-friendly 404 page.
- **Automated Verification:** 20 comprehensive unit and integration tests covering domain validation, service/repository logic, and web controller endpoints.

---

## 🏗️ Project Architecture & Directory Structure

```text
webdev2/
├── .mvn/
│   └── wrapper/
├── mvnw
├── mvnw.cmd
├── pom.xml
├── README.md
├── answers.md
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── webdev2/
│   │   │               ├── Webdev2Application.java
│   │   │               ├── controller/
│   │   │               │   └── TaskController.java            <-- Task 2, 4, 5: MVC Routing & Exception Handler
│   │   │               ├── exception/
│   │   │               │   └── TaskNotFoundException.java     <-- Task 2, 5: Custom 404 Runtime Exception
│   │   │               ├── model/
│   │   │               │   └── Task.java                      <-- Task 1, 4: Encapsulated Domain Model & Validation
│   │   │               ├── repository/
│   │   │               │   └── TaskRepository.java            <-- Task 1: In-Memory Storage & ID Generation
│   │   │               └── service/
│   │   │                   └── TaskService.java               <-- Task 1: Business Logic & Constructor DI
│   │   └── resources/
│   │       ├── application.properties
│   │       └── templates/
│   │           ├── task-list.html                             <-- Task 3: Thymeleaf Task Listing & Empty State
│   │           ├── task-detail.html                           <-- Task 3: Thymeleaf Task Detail View
│   │           ├── task-form.html                             <-- Task 4: Form Binding & Inline Validation
│   │           └── error/
│   │               └── not-found.html                         <-- Task 5: Friendly 404 Not Found View
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── webdev2/
│                       ├── TaskControllerTest.java            <-- Web MVC MockMvc Integration Tests
│                       ├── TaskServiceTest.java               <-- Service & Repository Integration Tests
│                       ├── TaskValidationTest.java            <-- Bean Validation Constraint Tests
│                       └── Webdev2ApplicationTests.java
```

---

## 🔍 Tasks & Implementation Details

### Task 1: Project Bootstrap & Layered Skeleton
- **Domain Model (`Task.java`):**
  - Fields: `id` (`Long`), `title` (`String`), `description` (`String`), `completed` (`boolean`).
  - Strict encapsulation with private fields, standard getters/setters, default no-args constructor, and parameterized constructor.
- **Repository Layer (`TaskRepository.java`):**
  - Annotated with `@Repository`.
  - In-memory thread-safe `List<Task>` using `CopyOnWriteArrayList<Task>`.
  - Auto-assigns sequential IDs using `AtomicLong idCounter`.
  - Provides `findAll()`, `findById(Long)`, and `save(Task)`.
  - Seeded with two sample tasks for immediate interactive demonstration.
- **Service Layer (`TaskService.java`):**
  - Annotated with `@Service`.
  - Uses **constructor injection** to receive `TaskRepository`.
  - Delegates persistence and retrieval cleanly.

### Task 2: List & Detail Routing
- **Web Controller (`TaskController.java`):**
  - Annotated with `@Controller`.
  - Injected with `TaskService` via constructor injection.
  - `GET /tasks`: Retrieves all tasks, populates `Model` with `"tasks"`, and renders `"task-list"`.
  - `GET /tasks/{id}`: Binds `@PathVariable Long id`. Retrieves task by ID or throws `TaskNotFoundException` if missing. Populates `Model` with `"task"` and renders `"task-detail"`.
  - `GET /`: Convenience redirect to `/tasks`.

### Task 3: Thymeleaf Rendering
- **`task-list.html`:**
  - Renders all tasks in a styled table using `th:each="task : ${tasks}"`.
  - Shows dynamic completion badges (`Completed` / `Pending`).
  - Detail navigation link: `th:href="@{/tasks/{id}(id=${task.id})}"`.
  - Fallback empty state message rendered with `th:if="${tasks == null or tasks.empty}"`.
- **`task-detail.html`:**
  - Displays all entity attributes (`id`, `title`, `description`, `completed`) using variable expressions `${task.*}`.
  - Provides a back navigation link to `/tasks`.

### Task 4: Validated Create Form
- **Form Display (`GET /tasks/new`):**
  - Populates Model with a new empty `Task` entity and returns `"task-form"`.
- **Form Submission (`POST /tasks`):**
  - Signature: `createTask(@Valid @ModelAttribute("task") Task task, BindingResult bindingResult, Model model)`.
  - If `bindingResult.hasErrors()` is true, redisplays `"task-form"` with field-level inline error messages.
  - If valid, saves via `taskService.save(task)` and returns `"redirect:/tasks"` adhering to the **Post/Redirect/Get (PRG)** pattern to prevent duplicate submissions.
- **Form Template (`task-form.html`):**
  - Bound to entity using `th:object="${task}"`, `th:field="*{title}"`, `th:field="*{description}"`, `th:field="*{completed}"`.
  - Renders inline errors adjacent to form inputs using `th:if="${#fields.hasErrors('...')}"` and `th:errors="*{...}"`.
- **Validation Constraints:**
  - `title`: `@NotBlank(message = "Title is required")`, `@Size(min = 2, max = 100, message = "Title must be between 2 and 100 characters")`.
  - `description`: `@Size(max = 500, message = "Description cannot exceed 500 characters")`.

### Task 5: Custom Exception Handling
- **Exception Class (`TaskNotFoundException.java`):**
  - Extends `RuntimeException`.
  - Annotated with `@ResponseStatus(HttpStatus.NOT_FOUND)`.
- **Controller Exception Handler (`TaskController.java`):**
  - Method annotated with `@ExceptionHandler(TaskNotFoundException.class)`.
  - Captures the exception, populates `Model` with `errorMessage`, and returns view `"error/not-found"`.
- **Error Template (`error/not-found.html`):**
  - Displays a clean, user-friendly 404 message (`th:text="${errorMessage}"`) without exposing raw stack traces.
  - Provides a button to safely return to `/tasks`.
  - Verified by navigating to `/tasks/999`.

---

## 🎯 Acceptance Criteria Verification Matrix

| Task | Criterion | Implementation Evidence | Status |
|---|---|---|:---:|
| **1** | Project compiles and starts. `Task` model is encapsulated. Repository and Service are beans with constructor injection. | `Task.java`, `TaskRepository.java`, `TaskService.java`, constructor injection verified via tests | ✅ **PASSED** |
| **2** | `GET /tasks` returns the task list view. `GET /tasks/{id}` returns detail or throws `TaskNotFoundException`. | `TaskController.java#listTasks`, `TaskController.java#getTaskById`, verified via `TaskControllerTest` | ✅ **PASSED** |
| **3** | Both templates render correctly with dynamic data. Empty list shows a fallback message. | `task-list.html` (`th:each`, `th:if`), `task-detail.html` (${task.*}) | ✅ **PASSED** |
| **4** | Invalid form submissions redisplay with field-level errors. Valid submissions redirect to `/tasks`. | `POST /tasks` with `@Valid` and `BindingResult`, PRG redirect, inline `th:errors` | ✅ **PASSED** |
| **5** | `/tasks/999` shows a friendly not-found page, not a stack trace. | `@ExceptionHandler(TaskNotFoundException.class)` returning `error/not-found.html` | ✅ **PASSED** |
| **6** | Video walkthrough demonstrates operations, validation, redirect, 404, and code explanation. | [https://youtu.be/FrRgu0Lc7YQ](https://youtu.be/FrRgu0Lc7YQ) | ✅ **PASSED** |

---

## 🧪 Automated Test Results

Execute the automated test suite using Maven:

```bash
./mvnw clean test
```

### Test Summary:
```text
[INFO] Running Task Controller Web MVC Tests
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0 -- in Task Controller Web MVC Tests
[INFO] Running Task Service & Repository Integration Tests
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0 -- in Task Service & Repository Integration Tests
[INFO] Running Task Model Bean Validation Tests
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0 -- in Task Model Bean Validation Tests
[INFO] 
[INFO] Results:
[INFO] Tests run: 20, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

---

## 🚀 How to Run the Application

1. **Start the application:**
   ```bash
   ./mvnw spring-boot:run
   ```
2. **Open in browser:**
   - Task List: `http://localhost:8080/tasks`
   - Create Task: `http://localhost:8080/tasks/new`
   - Single Task Detail: `http://localhost:8080/tasks/1`
   - Not Found Error Test: `http://localhost:8080/tasks/999`
