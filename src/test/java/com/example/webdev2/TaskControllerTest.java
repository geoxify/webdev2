package com.example.webdev2;

import com.example.webdev2.controller.TaskController;
import com.example.webdev2.model.Task;
import com.example.webdev2.repository.TaskRepository;
import com.example.webdev2.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Task Controller Web MVC Tests")
public class TaskControllerTest {

    private MockMvc mockMvc;
    private TaskRepository taskRepository;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskRepository = new TaskRepository();
        taskRepository.deleteAll(); // Clean state
        taskService = new TaskService(taskRepository);
        TaskController controller = new TaskController(taskService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("GET / redirects to /tasks")
    void testRootRedirect() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks"));
    }

    @Test
    @DisplayName("GET /tasks returns 200 and task-list view with tasks in model")
    void testListTasks() throws Exception {
        taskService.save(new Task(null, "Task Alpha", "Description Alpha", false));
        taskService.save(new Task(null, "Task Beta", "Description Beta", true));

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(view().name("task-list"))
                .andExpect(model().attributeExists("tasks"))
                .andExpect(model().attribute("tasks", hasSize(2)));
    }

    @Test
    @DisplayName("GET /tasks/{id} returns 200 and task-detail view for existing task")
    void testViewTaskSuccess() throws Exception {
        Task saved = taskService.save(new Task(null, "Detail Test Task", "Some Details", false));

        mockMvc.perform(get("/tasks/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("task-detail"))
                .andExpect(model().attributeExists("task"))
                .andExpect(model().attribute("task", hasProperty("title", is("Detail Test Task"))));
    }

    @Test
    @DisplayName("GET /tasks/999 triggers @ExceptionHandler and returns 404 with error/not-found view")
    void testViewTaskNotFound() throws Exception {
        mockMvc.perform(get("/tasks/999"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error/not-found"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", containsString("999")));
    }

    @Test
    @DisplayName("GET /tasks/new returns 200 and task-form view with empty task in model")
    void testShowCreateForm() throws Exception {
        mockMvc.perform(get("/tasks/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("task-form"))
                .andExpect(model().attributeExists("task"))
                .andExpect(model().attribute("task", hasProperty("title", nullValue())));
    }

    @Test
    @DisplayName("POST /tasks with valid data saves task and redirects to /tasks (Post/Redirect/Get)")
    void testCreateTaskValid() throws Exception {
        mockMvc.perform(post("/tasks")
                        .param("title", "New Valid Task")
                        .param("description", "Valid task description")
                        .param("completed", "false"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks"));

        // Verify task was persisted
        org.junit.jupiter.api.Assertions.assertEquals(1, taskService.findAll().size());
        org.junit.jupiter.api.Assertions.assertEquals("New Valid Task", taskService.findAll().get(0).getTitle());
    }

    @Test
    @DisplayName("POST /tasks with blank title fails validation and redisplays task-form with errors")
    void testCreateTaskBlankTitle() throws Exception {
        mockMvc.perform(post("/tasks")
                        .param("title", "")
                        .param("description", "Valid description")
                        .param("completed", "false"))
                .andExpect(status().isOk())
                .andExpect(view().name("task-form"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("task", "title"));
    }

    @Test
    @DisplayName("POST /tasks with 1-character title fails @Size and redisplays task-form")
    void testCreateTaskShortTitle() throws Exception {
        mockMvc.perform(post("/tasks")
                        .param("title", "A")
                        .param("description", "Valid description")
                        .param("completed", "false"))
                .andExpect(status().isOk())
                .andExpect(view().name("task-form"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("task", "title"));
    }

    @Test
    @DisplayName("POST /tasks with description > 500 characters fails @Size and redisplays task-form")
    void testCreateTaskDescriptionTooLong() throws Exception {
        String longDescription = "x".repeat(501);
        mockMvc.perform(post("/tasks")
                        .param("title", "Valid Title")
                        .param("description", longDescription)
                        .param("completed", "false"))
                .andExpect(status().isOk())
                .andExpect(view().name("task-form"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("task", "description"));
    }
}
