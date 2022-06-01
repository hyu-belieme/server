package com.example.beliemeserver.controller.httpexception;

import com.example.beliemeserver.controller.responsebody.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class HttpException extends Exception {
    protected HttpStatus httpStatus;
    protected String message;

    protected HttpException() {

    }

    protected HttpException(Exception e) {
        this.message = e.getMessage();
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }

    protected void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    protected void setMessage(String message) {
        this.message = message;
    }

    public ResponseEntity<ExceptionResponse> toResponseEntity() {
        return ResponseEntity.status(getHttpStatus()).body(new ExceptionResponse(getHttpStatus().name(), getMessage()));
    }
}
