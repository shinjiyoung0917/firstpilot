package com.example.firstpilot.exceptionAndHandler;

public class AlreadyExistedNicknameException extends RuntimeException {
    public AlreadyExistedNicknameException() {
        super("이미 존재하는 닉네임입니다.");
    }
}
