package com.example.beliemeserver.controller.httpexception;

import org.springframework.http.HttpStatus;

public class MethodNotAllowedHttpException extends HttpException {
    public MethodNotAllowedHttpException(Exception e) {
        super(e);
        setHttpStatus(HttpStatus.METHOD_NOT_ALLOWED);
    }
    
    public MethodNotAllowedHttpException(String message) {
        super();
        setHttpStatus(HttpStatus.METHOD_NOT_ALLOWED);
        setMessage(message);
    }
}