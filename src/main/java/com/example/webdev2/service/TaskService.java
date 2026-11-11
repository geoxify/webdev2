package com.example.webdev2.service;

import com.example.webdev2.model.Task;
import com.example.webdev2.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service layer coordinating business operations for Task entities.
 * Depends on TaskRepository via constructor injection.
 */
@Service
public class TaskService {

    private final TaskRepository taskRepository;

    /**
     * Constructor-based dependency injection.
     *
     * @param taskRepository The repository dependency injected by Spring
     */
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Retrieves all tasks.
     *
     * @return List of all tasks
     */
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    /**
     * Finds a task by ID.
     *
     * @param id The task identifier
     * @return Optional containing the task if found
     */
    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    /**
     * Saves or updates a task.
     *
     * @param task The task entity to save
     * @return The persisted task entity
     */
    public Task save(Task task) {
        return taskRepository.save(task);
    }
}
