package com.example.webdev2;

import com.example.webdev2.model.Task;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Task Model Bean Validation Tests")
public class TaskValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Valid task passes all bean validation constraints")
    void testValidTask() {
        Task task = new Task(null, "Study Spring MVC", "Read chapters on controllers and views", false);
        Set<ConstraintViolation<Task>> violations = validator.validate(task);
        assertTrue(violations.isEmpty(), "Valid task should have no validation violations");
    }

    @Test
    @DisplayName("Blank title fails validation with @NotBlank")
    void testBlankTitle() {
        Task task = new Task(null, "   ", "Valid description", false);
        Set<ConstraintViolation<Task>> violations = validator.validate(task);
        assertFalse(violations.isEmpty(), "Blank title should trigger validation error");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("title")));
    }

    @Test
    @DisplayName("Title shorter than 2 characters fails with @Size")
    void testTitleTooShort() {
        Task task = new Task(null, "A", "Valid description", false);
        Set<ConstraintViolation<Task>> violations = validator.validate(task);
        assertFalse(violations.isEmpty(), "1-character title should trigger validation error");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("between 2 and 100 characters")));
    }

    @Test
    @DisplayName("Title longer than 100 characters fails with @Size")
    void testTitleTooLong() {
        String longTitle = "T".repeat(101);
        Task task = new Task(null, longTitle, "Valid description", false);
        Set<ConstraintViolation<Task>> violations = validator.validate(task);
        assertFalse(violations.isEmpty(), "101-character title should trigger validation error");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("between 2 and 100 characters")));
    }

    @Test
    @DisplayName("Description longer than 500 characters fails with @Size")
    void testDescriptionTooLong() {
        String longDesc = "D".repeat(501);
        Task task = new Task(null, "Valid Title", longDesc, false);
        Set<ConstraintViolation<Task>> violations = validator.validate(task);
        assertFalse(violations.isEmpty(), "501-character description should trigger validation error");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("cannot exceed 500 characters")));
    }

    @Test
    @DisplayName("Null description is allowed (optional field)")
    void testNullDescriptionAllowed() {
        Task task = new Task(null, "Valid Title", null, false);
        Set<ConstraintViolation<Task>> violations = validator.validate(task);
        assertTrue(violations.isEmpty(), "Null description should be accepted as optional");
    }
}
