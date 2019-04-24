package com.example.firstpilot.exceptionAndHandler;

public class AlreadyExistedEmailException extends RuntimeException {
    public AlreadyExistedEmailException() {
        super("이미 존재하는 이메일입니다.");
    }
}
