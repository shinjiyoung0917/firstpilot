package com.example.firstpilot.exceptionAndHandler;

public class UnableToDeleteLikeBoard extends RuntimeException {
    public UnableToDeleteLikeBoard() { super("좋아요 되어있던 게시물이 아닙니다."); }
}