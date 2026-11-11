package com.example.webdev2.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Domain entity representing a personal task in the Task Tracker application.
 */
public class Task {

    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 2, max = 100, message = "Title must be between 2 and 100 characters")
    private String title;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    private boolean completed = false;

    /**
     * Default no-argument constructor required for form binding and serialization.
     */
    public Task() {
    }

    /**
     * Parameterized constructor for convenience and testing.
     *
     * @param id          Unique identifier (auto-assigned by repository)
     * @param title       Task title (required, 2-100 characters)
     * @param description Task description (optional, max 500 characters)
     * @param completed   Task completion status (defaults to false)
     */
    public Task(Long id, String title, String description, boolean completed) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = completed;
    }

    // Getters and Setters ensuring full encapsulation

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", completed=" + completed +
                '}';
    }
}
