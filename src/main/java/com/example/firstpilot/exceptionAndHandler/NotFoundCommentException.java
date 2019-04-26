package com.example.firstpilot.exceptionAndHandler;

public class NotFoundCommentException extends RuntimeException {
    public NotFoundCommentException() {
        super("해당 댓글이 존재하지 않습니다.");
    }
}
