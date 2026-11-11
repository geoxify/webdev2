package com.example.webdev2;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ProductValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Task 2: Valid product passes all Bean Validation constraints")
    void testValidProduct() {
        Product product = new Product(1L, "Mechanical Keyboard", "Peripherals", 129.99, 25, "Hot-swappable tactile keyboard");
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertTrue(violations.isEmpty(), "Valid product should not produce any constraint violations");
    }

    @Test
    @DisplayName("Task 2: Blank name triggers @NotBlank and @Size violations")
    void testBlankNameViolation() {
        Product product = new Product(1L, "   ", "Peripherals", 129.99, 10, "Description");
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    @DisplayName("Task 2: Negative stock triggers @Min violation")
    void testNegativeStockViolation() {
        Product product = new Product(1L, "Gaming Mouse", "Peripherals", 59.99, -5, "Description");
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("cannot be negative")));
    }

    @Test
    @DisplayName("Task 2: Null price triggers @NotNull violation")
    void testNullPriceViolation() {
        Product product = new Product(1L, "Gaming Mouse", "Peripherals", null, 10, "Description");
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Price is required")));
    }

    @Test
    @DisplayName("Task 2: Zero or negative price triggers @DecimalMin violation")
    void testPriceBelowMinimumViolation() {
        Product product = new Product(1L, "Gaming Mouse", "Peripherals", 0.00, 10, "Description");
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("at least $0.01")));
    }
}
