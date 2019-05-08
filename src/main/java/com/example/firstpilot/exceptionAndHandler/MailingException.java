package com.example.firstpilot.exceptionAndHandler;

public class MailingException extends RuntimeException {
    public MailingException() {
        super("메일 전송 에러입니다.");
    }
}
