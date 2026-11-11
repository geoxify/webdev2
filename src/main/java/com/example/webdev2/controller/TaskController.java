package com.example.webdev2.controller;

import com.example.webdev2.exception.TaskNotFoundException;
import com.example.webdev2.model.Task;
import com.example.webdev2.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Web Controller handling HTTP requests for the Task Tracker application.
 * Follows Spring MVC patterns with constructor-based dependency injection.
 */
@Controller
public class TaskController {

    private final TaskService taskService;

    /**
     * Constructor injection for TaskService.
     *
     * @param taskService Service layer bean injected by Spring
     */
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Redirects root URL "/" to the main task listing.
     */
    @GetMapping("/")
    public String index() {
        return "redirect:/tasks";
    }

    /**
     * Task 2: List Routing
     * Retrieves all tasks from the service, adds them to the Model, and returns "task-list".
     */
    @GetMapping("/tasks")
    public String listTasks(Model model) {
        model.addAttribute("tasks", taskService.findAll());
        return "task-list";
    }

    /**
     * Task 2: Detail Routing
     * Retrieves a single task by ID using @PathVariable, adds it to the Model, and returns "task-detail".
     * If not found, throws TaskNotFoundException.
     */
    @GetMapping("/tasks/{id}")
    public String getTaskById(@PathVariable("id") Long id, Model model) {
        Task task = taskService.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        model.addAttribute("task", task);
        return "task-detail";
    }

    /**
     * Task 4: Create Form Display
     * Places a new empty Task object in the Model and returns "task-form".
     */
    @GetMapping("/tasks/new")
    public String showCreateForm(Model model) {
        model.addAttribute("task", new Task());
        return "task-form";
    }

    /**
     * Task 4: Validated Create Action
     * Processes form submission using @Valid @ModelAttribute and BindingResult.
     * If validation fails, redisplays "task-form" with errors.
     * If valid, persists task and redirects to "/tasks" (Post/Redirect/Get pattern).
     */
    @PostMapping("/tasks")
    public String createTask(@Valid @ModelAttribute("task") Task task,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            return "task-form";
        }
        taskService.save(task);
        return "redirect:/tasks";
    }

    /**
     * Task 5: Custom Exception Handling
     * Catches TaskNotFoundException, populates error model, and returns "error/not-found".
     */
    @ExceptionHandler(TaskNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleTaskNotFound(TaskNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/not-found";
    }
}
