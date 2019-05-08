package com.example.firstpilot.exceptionAndHandler;

public class BadRequestFromMemberException extends RuntimeException {
    public BadRequestFromMemberException(String message) { super(message); }
}
