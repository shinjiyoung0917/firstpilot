package com.example.firstpilot.exceptionAndHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerAdviceForException extends ResponseEntityExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ExceptionHandler.class);

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity handleNullPointerException(NullPointerException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(AlreadyExistedEmailException.class)
    public ResponseEntity handleAlreadyExistedEmailException(AlreadyExistedEmailException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(AlreadyExistedNicknameException.class)
    public ResponseEntity handleAlreadyExistedNicknameException(AlreadyExistedNicknameException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity handleMessagingException(MessagingException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(NotFoundMemberException.class)
    public ResponseEntity handleNotFoundMemberException(NotFoundMemberException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(NotTimeForNicknameChange.class)
    public ResponseEntity handleNotTimeForNicknameChange(NotTimeForNicknameChange e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(NotFoundBoardException.class)
    public ResponseEntity handleNotFoundBoardException(NotFoundBoardException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(NotFoundCommentException.class)
    public ResponseEntity handleNotFoundCommentException(NotFoundCommentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(UnableToDeleteLikeBoard.class)
    public ResponseEntity handleUnableToDeleteLikeBoard(UnableToDeleteLikeBoard e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
