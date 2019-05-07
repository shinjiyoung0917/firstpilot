package com.example.firstpilot.exceptionAndHandler;

public class NotLoginMember extends RuntimeException {
    public NotLoginMember() { super("로그인하지 않은 회원입니다."); }
}
