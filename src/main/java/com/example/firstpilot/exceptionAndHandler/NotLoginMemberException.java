package com.example.firstpilot.exceptionAndHandler;

public class NotLoginMemberException extends RuntimeException {
    public NotLoginMemberException() { super("로그인하지 않은 회원입니다."); }
}
