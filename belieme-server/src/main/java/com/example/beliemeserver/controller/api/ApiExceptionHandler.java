package com.example.beliemeserver.controller.api;

import com.example.beliemeserver.controller.responsebody.ExceptionResponse;
import com.example.beliemeserver.exception.InternalServerException;
import com.example.beliemeserver.exception.ServerException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAllException(Exception ex) {
        return handleExceptionInternal(new InternalServerException());
    }

    @ExceptionHandler({ServerException.class})
    public ResponseEntity<Object> handleExceptionInternal(ServerException ex) {
        ExceptionResponse errorBody = new ExceptionResponse(ex.getName(), ex.getMessage());
        return ResponseEntity.status(ex.getHttpStatus()).body(errorBody);
    }
}
