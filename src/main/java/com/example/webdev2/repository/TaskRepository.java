package com.example.webdev2.repository;

import com.example.webdev2.model.Task;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory repository for storing and managing Task domain entities.
 */
@Repository
public class TaskRepository {

    private final List<Task> tasks = new CopyOnWriteArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1L);

    public TaskRepository() {
        // Pre-populate with sample tasks for demonstration
        save(new Task(null, "Complete Prelim Lab Exam", "Implement Tasks 1 through 6 for the Spring Boot Task Tracker.", false));
        save(new Task(null, "Review Spring Boot Architecture", "Study Controller-Service-Repository pattern and Thymeleaf templates.", true));
    }

    /**
     * Retrieve all tasks.
     *
     * @return List of all tasks
     */
    public List<Task> findAll() {
        return new ArrayList<>(tasks);
    }

    /**
     * Retrieve a task by its unique ID.
     *
     * @param id The task identifier
     * @return Optional containing the task if found, or empty Optional if not found
     */
    public Optional<Task> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return tasks.stream()
                .filter(t -> id.equals(t.getId()))
                .findFirst();
    }

    /**
     * Save or update a task in memory.
     * If the task's ID is null, a new ID is automatically assigned.
     *
     * @param task The task to persist
     * @return The persisted task
     */
    public Task save(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }

        if (task.getId() == null) {
            task.setId(idCounter.getAndIncrement());
            tasks.add(task);
        } else {
            // Update existing or add if not present
            Optional<Task> existing = findById(task.getId());
            if (existing.isPresent()) {
                int index = tasks.indexOf(existing.get());
                tasks.set(index, task);
            } else {
                tasks.add(task);
            }
        }
        return task;
    }

    /**
     * Clear all tasks and reset counter (useful for test isolation).
     */
    public void deleteAll() {
        tasks.clear();
        idCounter.set(1L);
    }
}
