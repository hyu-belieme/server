package com.example.beliemeserver.controller.api;

import com.example.beliemeserver.controller.responsebody.ExceptionResponse;
import com.example.beliemeserver.exception.BadRequestException;
import com.example.beliemeserver.exception.InternalServerException;
import com.example.beliemeserver.exception.ServerException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

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

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        BadRequestException badRequestException = new BadRequestException();
        List<ExceptionResponse.ValidationError> validationErrorList = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(ExceptionResponse.ValidationError::of)
                .toList();

        ExceptionResponse errorBody = new ExceptionResponse(
                badRequestException.getName(),
                badRequestException.getMessage(),
                validationErrorList
        );
        return ResponseEntity.status(badRequestException.getHttpStatus()).body(errorBody);
    }
}
