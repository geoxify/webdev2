package com.example.webdev2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom runtime exception thrown when a requested task cannot be found by its ID.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(String message) {
        super(message);
    }

    public TaskNotFoundException(Long id) {
        super("Task with ID " + id + " was not found.");
    }
}
