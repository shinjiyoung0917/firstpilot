package com.example.firstpilot.exceptionAndHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RestControllerAdvice
public class ControllerAdviceForException extends ResponseEntityExceptionHandler {
    // TODO: 원래 NPE는 발생하면 안되지만, 꼭 필요하다면 처리
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity handleNullPointerException(NullPointerException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(NotLoginMemberException.class)
    public ResponseEntity handleNotLoginMemberException(NotLoginMemberException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler(NotFoundResourcesException.class)
    public ResponseEntity handleNotFoundResourcesException(NotFoundResourcesException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(BadRequestFromMemberException.class)
    public ResponseEntity handleBadRequestFromMemberException(BadRequestFromMemberException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    // TODO: 이 메서드를 그대로 냅둘지, 변경할지 고민
    @ExceptionHandler(UnableToDeleteLikeBoardException.class)
    public ResponseEntity handleUnableToDeleteLikeBoardException(UnableToDeleteLikeBoardException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(MailingException.class)
    public ResponseEntity handleMailingException(MailingException e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
    }

    @ExceptionHandler(ResourceIOStreamException.class)
    public ResponseEntity handleResourceIOStreamException(ResourceIOStreamException e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
    }
}
