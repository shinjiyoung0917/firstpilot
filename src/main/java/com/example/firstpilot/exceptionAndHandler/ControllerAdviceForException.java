package com.example.firstpilot.exceptionAndHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RestControllerAdvice
public class ControllerAdviceForException extends ResponseEntityExceptionHandler {
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity handleNullPointerException(NullPointerException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        // TODO: 상태코드 고치기, NPE 발생하면 안됨
    }

    @ExceptionHandler(NotLoginMember.class)
    public ResponseEntity handleNotLoginMember(NotLoginMember e) {
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
