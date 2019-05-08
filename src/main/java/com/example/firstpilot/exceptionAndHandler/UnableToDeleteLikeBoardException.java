package com.example.firstpilot.exceptionAndHandler;

public class UnableToDeleteLikeBoardException extends RuntimeException {
    public UnableToDeleteLikeBoardException() { super("좋아요 되어있던 게시물이 아닙니다."); }
}