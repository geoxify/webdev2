# WebDev 2 — Prelim Lab Exam: Task Tracker

**Course:** WEBDEV2 (Java/Spring Boot Full-Stack Web Development)  
**Assessment:** Prelim Lab Exam  
**Student:** geoxify (`geoffbenedict.carino@lorma.edu`)  
**Repository:** `https://github.com/geoxify/webdev2`  
**Branch:** `prelim`  
**Walkthrough Video (Task 6):** [https://youtu.be/FrRgu0Lc7YQ](https://youtu.be/FrRgu0Lc7YQ) (YouTube Unlisted)

---

## 📌 Table of Contents
1. [Overview & Scope](#-overview--scope)
2. [Architecture & Package Structure](#-architecture--package-structure)
3. [Task 1 — Project Bootstrap & Layered Skeleton](#-task-1--project-bootstrap--layered-skeleton)
4. [Task 2 — List & Detail Routing](#-task-2--list--detail-routing)
5. [Task 3 — Thymeleaf Rendering](#-task-3--thymeleaf-rendering)
6. [Task 4 — Validated Create Form](#-task-4--validated-create-form)
7. [Task 5 — Custom Exception Handling](#-task-5--custom-exception-handling)
8. [Task 6 — Video Walkthrough Guide & Talking Points](#-task-6--video-walkthrough-guide--talking-points)
9. [Automated Verification Results](#-automated-verification-results)
10. [Acceptance Criteria Checklist](#-acceptance-criteria-checklist)

---

## 🎯 Overview & Scope
The Prelim Lab Exam assesses mastery of Weeks 1–5:
- Object-Oriented Programming (encapsulation, constructors, streams, collections).
- Spring Boot Project Setup & Dependency Injection (IoC, constructor injection).
- Spring MVC Routing (Controller, Request Mapping, Path Variables, Model).
- Server-side rendering using Thymeleaf (`th:each`, `th:if`, `th:object`, `th:field`, `th:errors`).
- Jakarta Bean Validation (`@NotBlank`, `@Size`, `BindingResult`).
- Layered Controller-Service-Repository architecture and custom Exception Handling (`@ExceptionHandler`).

---

## 📁 Architecture & Package Structure

```text
com.example.webdev2/
├── Webdev2Application.java
├── controller/
│   └── TaskController.java
├── exception/
│   └── TaskNotFoundException.java
├── model/
│   └── Task.java
├── repository/
│   └── TaskRepository.java
└── service/
    └── TaskService.java
```

---

## 💻 Code Explanations by Layer

### 1. Model Layer (`Task.java`)
Represents the core domain entity. It holds the attributes:
- `Long id`: Unique identifier auto-assigned by the repository counter.
- `String title`: Mandatory task title constrained by `@NotBlank` and `@Size(min = 2, max = 100)`.
- `String description`: Optional detail constrained by `@Size(max = 500)`.
- `boolean completed`: Flag defaulted to `false`.

Full encapsulation is maintained via private fields, public getters, setters, and explicit constructors.

### 2. Repository Layer (`TaskRepository.java`)
Annotated with `@Repository`. It manages an in-memory thread-safe `CopyOnWriteArrayList<Task>` and uses `AtomicLong` to auto-increment task IDs. Provides `findAll()`, `findById(Long)`, and `save(Task)`.

### 3. Service Layer (`TaskService.java`)
Annotated with `@Service`. Employs constructor injection to receive `TaskRepository`. Acts as the intermediary business coordination layer.

### 4. Controller Layer (`TaskController.java`)
Annotated with `@Controller`. Routes:
- `GET /tasks`: Returns `"task-list"` view.
- `GET /tasks/{id}`: Returns `"task-detail"` view or throws `TaskNotFoundException`.
- `GET /tasks/new`: Returns `"task-form"` view.
- `POST /tasks`: Validates form submission with `@Valid` and `BindingResult`. If invalid, redisplays `"task-form"`. If valid, saves and redirects (`redirect:/tasks`).
- `@ExceptionHandler(TaskNotFoundException.class)`: Returns `"error/not-found"` view with status 404.

---

## 📹 Task 6 — Video Walkthrough Guide & Talking Points

Use this timed walkthrough script during recording:

### Segment 1: Running Application Demo (~4 minutes)
1. **List View (`/tasks`):**
   - Show the browser at `http://localhost:8080/tasks`.
   - Point out the list table with the initial pre-seeded tasks, IDs, titles, and completion badges.
2. **Detail View (`/tasks/1`):**
   - Click "View Details" on Task 1.
   - Show that all fields (`id`, `title`, `description`, `completed`) are rendered.
   - Click "Back to Task List".
3. **Form Validation (`/tasks/new`):**
   - Click "Create New Task".
   - Leave the title blank and click "Save Task".
   - Point out the inline error message: *"Title is required"*.
   - Type a single character `"A"` and click "Save Task".
   - Point out the inline error message: *"Title must be between 2 and 100 characters"*.
4. **Valid Task Creation & PRG Redirect:**
   - Type a valid title: `"Record Walkthrough Video"`.
   - Enter a description: *"Demonstrate all acceptance criteria in under 15 minutes."*
   - Check the "Mark as already completed" box.
   - Click "Save Task".
   - Show the immediate redirect back to `/tasks` (Post/Redirect/Get) with the new task displayed.
5. **Exception Handling (`/tasks/999`):**
   - Manually type `http://localhost:8080/tasks/999` in the browser URL bar.
   - Show the friendly 404 page ("Task with ID 999 was not found") instead of a stack trace.
   - Click "Return to Task List".

### Segment 2: Code Walkthrough (~6 minutes)
1. **Architecture & Dependency Injection:**
   - Open `TaskRepository.java`, `TaskService.java`, and `TaskController.java`.
   - Explain how `@Repository`, `@Service`, and `@Controller` work together.
   - Highlight the constructor injection:
     ```java
     public TaskService(TaskRepository taskRepository) { ... }
     public TaskController(TaskService taskService) { ... }
     ```
2. **Domain Model & Bean Validation:**
   - Open `Task.java`.
   - Point out the annotations `@NotBlank` and `@Size(min = 2, max = 100)` on `title`, and `@Size(max = 500)` on `description`.
3. **Thymeleaf Form Binding:**
   - Open `task-form.html`.
   - Explain `th:object="${task}"`, `th:field="*{title}"`, and `th:errors="*{title}"`.
   - Show how `BindingResult` in `TaskController.java` catches errors before returning `"task-form"`.
4. **Exception Handling:**
   - Open `TaskNotFoundException.java` and show `extends RuntimeException`.
   - Open `TaskController.java` and show `@ExceptionHandler(TaskNotFoundException.class)`.
   - Show `error/not-found.html` rendering `${errorMessage}`.
5. **Automated Tests:**
   - Show the terminal with `./mvnw test` passing 20/20 tests.

---

## 🧪 Automated Verification Results

All 20 unit and integration tests execute successfully:

```text
[INFO] Running Task Controller Web MVC Tests
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running Task Service & Repository Integration Tests
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running Task Model Bean Validation Tests
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] Results:
[INFO] Tests run: 20, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

---

## ✅ Acceptance Criteria Checklist

- [x] **Task 1:** Project compiles and starts. `Task` model is encapsulated. Repository and Service are beans with constructor injection.
- [x] **Task 2:** `GET /tasks` returns the task list view. `GET /tasks/{id}` returns detail or throws `TaskNotFoundException`.
- [x] **Task 3:** Both templates render correctly with dynamic data. Empty list shows a fallback message.
- [x] **Task 4:** Invalid form submissions redisplay with field-level errors. Valid submissions redirect to `/tasks`.
- [x] **Task 5:** `/tasks/999` shows a friendly not-found page, not a stack trace.
- [x] **Task 6:** Video walkthrough recorded and uploaded to YouTube: [https://youtu.be/FrRgu0Lc7YQ](https://youtu.be/FrRgu0Lc7YQ). All acceptance criteria and code walkthrough covered.
