package com.example.firstpilot.exceptionAndHandler;

public class NotFoundResourcesException extends RuntimeException {
    public NotFoundResourcesException(String message) {
        super(message);
    }
}
