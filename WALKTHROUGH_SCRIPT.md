# Prelim Lab Exam — Video Walkthrough Script (Task 6)

**Student Name:** Geoff Benedict Cariño (`geoxify`)  
**Course:** WEBDEV2 (Java/Spring Boot Full-Stack Web Development)  
**Project:** Task Tracker Application  
**Branch:** `prelim`  
**Target Duration:** ~8 to 10 minutes (Limit: under 15 minutes)

---

## 🎬 Pre-Recording Checklist
- [x] Application is running on **`http://localhost:8080/tasks`**.
- [ ] Screen recording software (OBS, Windows Game Bar `Win + G`, or Loom) set to record full screen or browser + IDE.
- [ ] Browser window open with tabs:
  - Tab 1: `http://localhost:8080/tasks`
- [ ] IDE open showing `webdev2` project tree on branch `prelim`.

---

## 🎙️ Spoken Script & Action Cues

### [00:00 - 00:45] Introduction
> **[ACTION: Start on your IDE or browser showing the Task Tracker page at `http://localhost:8080/tasks`.]**  
>
> "Hello everyone, my name is Geoff Benedict Cariño, and this is my video walkthrough for the WEBDEV2 Prelim Lab Exam.
>
> Today, I'll be demonstrating the **Task Tracker** web application, which is built entirely from scratch using Spring Boot, Spring MVC, Thymeleaf, and Jakarta Bean Validation, structured in a layered Controller-Service-Repository architecture with constructor-based dependency injection.
>
> In the first half of this video, I will demonstrate all running application features in the browser: listing tasks, viewing details, form validation, successful task creation with Post/Redirect/Get, and custom 404 exception handling. In the second half, I will walk through the source code and explain each architectural layer."

---

### [00:45 - 02:00] Part 1: Task Listing & Detail View
> **[ACTION: Switch to browser at `http://localhost:8080/tasks`.]**  
>
> "Here on the screen is our main task list page at `GET /tasks`.
>
> As you can see, our in-memory repository is seeded with two initial sample tasks. We have a table showing the auto-assigned task ID, the task title, a short description excerpt, dynamic status badges indicating whether a task is 'Completed' or 'Pending', and a 'View Details' action button.
>
> Our Thymeleaf template also includes an empty-state fallback using `th:if`. If no tasks exist in the repository, a clean empty-state card is displayed inviting the user to create their first task.
>
> **[ACTION: Click the 'View Details' button on Task 1.]**
>
> Now, let's view a single task's details by clicking 'View Details'. This navigates to `GET /tasks/1`.
>
> Here in `task-detail.html`, all four fields of our domain entity are rendered using Thymeleaf variable expressions:
> - The auto-assigned ID: 1
> - The Title: 'Complete Prelim Lab Exam'
> - The full Description
> - And the Completion status: 'False (Incomplete)', highlighted with an amber Pending badge.
>
> **[ACTION: Click 'Back to Task List' or use the breadcrumb link.]**
>
> Clicking 'Back to Task List' safely navigates us back to `/tasks`."

---

### [02:00 - 04:00] Part 2: Form Validation & Creation (PRG)
> **[ACTION: Click the '+ New Task' or 'Create New Task' button to navigate to `http://localhost:8080/tasks/new`.]**  
>
> "Next, let's test Task 4: creating a task with Bean Validation. I will click 'Create New Task', which executes `GET /tasks/new`. This displays our `task-form.html` template bound to an empty `Task` model object.
>
> First, let's test invalid input.
>
> **[ACTION: Leave the Title input completely empty, and click 'Save Task'.]**
>
> If I leave the Title blank and click 'Save Task', the form submits via `POST /tasks`. Our controller detects binding errors via `BindingResult` and redisplays the form with inline error messages. You can see the red border and the message: *'Title is required'*.
>
> **[ACTION: Type a single letter 'A' into the Title input, and click 'Save Task'.]**
>
> Now let's test the size constraint. The specification requires the title to be between 2 and 100 characters. If I enter just one letter, 'A', and click 'Save Task', validation fails again and displays: *'Title must be between 2 and 100 characters'*.
>
> The description field is also constrained to a maximum of 500 characters, while remaining completely optional.
>
> **[ACTION: Now enter valid task data.]**
> - **Title:** `Record Video Walkthrough`
> - **Description:** `Demonstrate all acceptance criteria and explain code architecture in under 15 minutes.`
> - **Check the box:** 'Mark as already completed'
>
> "Now let's submit a valid task. I'll enter 'Record Video Walkthrough' as the title, add a description, and toggle the switch to mark it as already completed.
>
> **[ACTION: Click 'Save Task'.]**
>
> When I click 'Save Task', the controller persists the task via the service layer and issues an HTTP redirect to `/tasks`. This implements the **Post/Redirect/Get** pattern, which prevents accidental duplicate form submissions if the user refreshes the page.
>
> Back on the task list, we immediately see our new task with ID 3, our title and description, and a green 'Completed' badge."

---

### [04:00 - 05:00] Part 3: Custom Exception Handling (404)
> **[ACTION: Click the browser URL bar, type `http://localhost:8080/tasks/999`, and press Enter.]**  
>
> "Now let's demonstrate Task 5: Custom Exception Handling.
>
> If a user requests a task ID that does not exist—for instance, `/tasks/999`—the application should not crash or expose an ugly Spring Boot stack trace.
>
> As you can see, requesting `/tasks/999` displays our friendly, custom 404 page rendered from `error/not-found.html`. It shows:
> - A 404 badge
> - The friendly message: *'Task with ID 999 was not found'*
> - And helpful navigation buttons to return to the task list or create a new task.
>
> **[ACTION: Click 'Return to Task List'.]**
>
> Clicking 'Return to Task List' brings us back to safety."

---

### [05:00 - 08:30] Part 4: Code Walkthrough & Architecture
> **[ACTION: Switch to your IDE (VS Code or IntelliJ) showing the project file tree.]**  
>
> "Now let's examine the source code to see how these features are implemented across our architectural layers.
>
> In the project tree under `src/main/java/com/example/webdev2`, we strictly follow the **Controller-Service-Repository** pattern across distinct packages: `model`, `repository`, `service`, `controller`, and `exception`.
>
> #### 1. Domain Model: `Task.java`
> **[ACTION: Open `Task.java`.]**  
>
> "Here in `Task.java`, we define our domain model:
> - We have private fields: `Long id`, `String title`, `String description`, and `boolean completed` defaulting to `false`.
> - Encapsulation is enforced through standard getters and setters, a no-arg constructor required for Spring MVC form binding, and a parameterized constructor.
> - We apply Jakarta Bean Validation: `@NotBlank(message = 'Title is required')`, `@Size(min = 2, max = 100)` on `title`, and `@Size(max = 500)` on `description`."
>
> #### 2. Repository Layer: `TaskRepository.java`
> **[ACTION: Open `TaskRepository.java`.]**  
>
> "Next, `TaskRepository.java` is annotated with `@Repository`.
> - It holds an in-memory `List<Task>` using `CopyOnWriteArrayList` to ensure thread-safety.
> - An `AtomicLong idCounter` automatically assigns incrementing sequential IDs starting at 1.
> - It implements standard persistence methods: `findAll()`, `findById(Long)` returning an `Optional<Task>`, and `save(Task)` which auto-assigns the ID if null, or updates an existing record."
>
> #### 3. Service Layer & Dependency Injection: `TaskService.java`
> **[ACTION: Open `TaskService.java`.]**  
>
> "In `TaskService.java`, we annotate the class with `@Service`.
> - Notice the **constructor-based dependency injection**: `TaskRepository` is declared as a `private final` field and injected through the public constructor. Spring's IoC container automatically resolves and provides the repository bean.
> - The service layer coordinates business operations and delegates persistence to `taskRepository`."
>
> #### 4. Web Controller: `TaskController.java`
> **[ACTION: Open `TaskController.java`.]**  
>
> "In `TaskController.java`, we annotate the class with `@Controller` and use constructor injection to inject `TaskService`.
>
> Let's look at the key endpoints:
> - `GET /tasks`: Calls `taskService.findAll()`, adds the list to the Spring `Model`, and returns the `'task-list'` view.
> - `GET /tasks/{id}`: Binds the ID using `@PathVariable`. It retrieves the task from `taskService.findById(id)`. If the optional is empty, it throws `new TaskNotFoundException(id)`. Otherwise, it adds the task to the model and returns `'task-detail'`.
> - `GET /tasks/new`: Adds an empty `new Task()` to the model and returns `'task-form'`.
> - `POST /tasks`: Accepts `@Valid @ModelAttribute("task") Task task` along with `BindingResult`. If `bindingResult.hasErrors()` is true, it immediately redisplays `'task-form'` with field-level validation errors. If valid, it saves the task and returns `'redirect:/tasks'`, implementing the Post/Redirect/Get pattern."
>
> #### 5. Exception Handling: `TaskNotFoundException.java` & `@ExceptionHandler`
> **[ACTION: Highlight `@ExceptionHandler` in `TaskController.java`, then show `TaskNotFoundException.java`.]**  
>
> "For exception handling, `TaskNotFoundException` extends `RuntimeException` and has `@ResponseStatus(HttpStatus.NOT_FOUND)`.
>
> Inside `TaskController`, our `@ExceptionHandler(TaskNotFoundException.class)` method intercepts this exception, sets the HTTP response status to 404, adds the error message to the model, and renders our custom `'error/not-found'` template."
>
> #### 6. Thymeleaf Templates
> **[ACTION: Open `task-form.html`.]**  
>
> "Taking a quick look at `task-form.html`, notice how form binding is configured:
> - `th:object="${task}"` binds the HTML form to the task model attribute.
> - `th:field="*{title}"` and `th:field="*{description}"` bind individual inputs.
> - And `th:errors="*{title}"` renders the exact validation message inline under the input when validation fails."

---

### [08:30 - 09:30] Automated Tests & Conclusion
> **[ACTION: Open your terminal and run `./mvnw test`.]**  
>
> "To verify that all requirements and edge cases are satisfied, I've written 20 automated unit and integration tests across `TaskValidationTest`, `TaskServiceTest`, and `TaskControllerTest`.
>
> Running `./mvnw test`... as you can see, all 20 tests pass with zero failures and zero errors.
>
> All code is committed to the `prelim` branch of my GitHub repository `https://github.com/geoxify/webdev2`.
>
> Thank you very much for watching!"
