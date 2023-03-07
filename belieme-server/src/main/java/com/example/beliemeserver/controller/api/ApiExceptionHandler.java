package com.example.beliemeserver.controller.api;

import com.example.beliemeserver.controller.responsebody.ExceptionResponse;
import com.example.beliemeserver.exception.*;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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
    private final MessageSource messageSource;

    public ApiExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAllException() {
        return handleExceptionInternal(new InternalServerException());
    }

    @ExceptionHandler({ServerException.class})
    public ResponseEntity<Object> handleExceptionInternal(ServerException e) {
        ExceptionResponse errorBody = new ExceptionResponse(e.getName(), e.getMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(errorBody);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        ErrorInfo errorInfo = CommonErrorInfo.BAD_REQUEST;
        List<ExceptionResponse.ValidationError> validationErrorList = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(ExceptionResponse.ValidationError::of)
                .toList();

        ExceptionResponse errorBody = new ExceptionResponse(
                errorInfo.name(),
                getMessage(errorInfo.responseMessage(), null),
                validationErrorList
        );
        return ResponseEntity.status(errorInfo.httpStatus()).body(errorBody);
    }

    private String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
