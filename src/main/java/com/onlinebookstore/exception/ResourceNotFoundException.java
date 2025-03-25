package com.onlinebookstore.exception;


//A custom exception for missing resources
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
