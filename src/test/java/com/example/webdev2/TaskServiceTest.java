package com.example.webdev2;

import com.example.webdev2.model.Task;
import com.example.webdev2.repository.TaskRepository;
import com.example.webdev2.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Task Service & Repository Integration Tests")
public class TaskServiceTest {

    private TaskRepository taskRepository;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskRepository = new TaskRepository();
        taskRepository.deleteAll(); // Start with empty repository for clean tests
        taskService = new TaskService(taskRepository);
    }

    @Test
    @DisplayName("findAll returns empty list when repository is empty")
    void testFindAllEmpty() {
        List<Task> tasks = taskService.findAll();
        assertNotNull(tasks);
        assertTrue(tasks.isEmpty());
    }

    @Test
    @DisplayName("save auto-assigns sequential ID to new tasks")
    void testSaveAutoAssignsId() {
        Task task1 = taskService.save(new Task(null, "Task 1", "Desc 1", false));
        assertNotNull(task1.getId());
        assertEquals(1L, task1.getId());

        Task task2 = taskService.save(new Task(null, "Task 2", "Desc 2", true));
        assertNotNull(task2.getId());
        assertEquals(2L, task2.getId());
    }

    @Test
    @DisplayName("findById retrieves existing task by ID")
    void testFindByIdSuccess() {
        Task saved = taskService.save(new Task(null, "Test Task", "Desc", false));
        Optional<Task> found = taskService.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("Test Task", found.get().getTitle());
        assertEquals(saved.getId(), found.get().getId());
    }

    @Test
    @DisplayName("findById returns empty Optional for non-existent ID")
    void testFindByIdNotFound() {
        Optional<Task> notFound = taskService.findById(999L);
        assertFalse(notFound.isPresent());
    }

    @Test
    @DisplayName("save updates existing task when ID is already set")
    void testSaveUpdatesExistingTask() {
        Task saved = taskService.save(new Task(null, "Initial Title", "Initial Desc", false));
        saved.setTitle("Updated Title");
        saved.setCompleted(true);

        Task updated = taskService.save(saved);
        assertEquals(saved.getId(), updated.getId());
        assertEquals("Updated Title", updated.getTitle());
        assertTrue(updated.isCompleted());

        Optional<Task> reloaded = taskService.findById(saved.getId());
        assertTrue(reloaded.isPresent());
        assertEquals("Updated Title", reloaded.get().getTitle());
        assertTrue(reloaded.get().isCompleted());
    }
}
