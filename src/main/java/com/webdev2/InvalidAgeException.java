package com.webdev2;

/**
 * Custom unchecked exception thrown when an invalid (negative) age is supplied.
 */
public class InvalidAgeException extends RuntimeException {
    public InvalidAgeException(String message) {
        super(message);
    }

    public InvalidAgeException(String message, Throwable cause) {
        super(message, cause);
    }
}
