package com.example.beliemeserver.controller.api;

import com.example.beliemeserver.common.Message;
import com.example.beliemeserver.controller.responsebody.ExceptionResponse;
import com.example.beliemeserver.exception.CommonErrorInfo;
import com.example.beliemeserver.exception.ErrorInfo;
import com.example.beliemeserver.exception.InternalServerException;
import com.example.beliemeserver.exception.ServerException;
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
        ExceptionResponse errorBody = new ExceptionResponse(
                e.getName(),
                getMessageFromSource(e.getResponseMessage()));
        return ResponseEntity.status(e.getHttpStatus()).body(errorBody);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        ErrorInfo errorInfo = CommonErrorInfo.BAD_REQUEST;
        ExceptionResponse errorBody = new ExceptionResponse(
                errorInfo.name(),
                getMessageFromSource(errorInfo.responseMessage()),
                makeValidationErrorResponse(e)
        );
        return ResponseEntity.status(errorInfo.httpStatus()).body(errorBody);
    }

    private String getMessageFromSource(Message message) {
        return messageSource.getMessage(message.getCode(), message.getArgs(), LocaleContextHolder.getLocale());
    }

    private List<ExceptionResponse.ValidationError> makeValidationErrorResponse(MethodArgumentNotValidException e) {
        return e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new ExceptionResponse.ValidationError(
                        fieldError.getField(),
                        getMessageFromSource(new Message(fieldError.getDefaultMessage()))))
                .toList();
    }
}
