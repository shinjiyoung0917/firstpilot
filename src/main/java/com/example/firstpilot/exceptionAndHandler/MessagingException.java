package com.example.firstpilot.exceptionAndHandler;

public class MessagingException extends RuntimeException {
    public MessagingException() {
        super("메일 전송 에러입니다.");
    }
}
