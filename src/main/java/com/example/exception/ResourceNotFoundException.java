package com.example.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
