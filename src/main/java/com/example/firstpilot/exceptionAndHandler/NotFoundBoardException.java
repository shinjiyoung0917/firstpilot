package com.example.firstpilot.exceptionAndHandler;

public class NotFoundBoardException extends RuntimeException {
    public NotFoundBoardException() {
        super("게시물이 존재하지 않습니다.");
    }
}