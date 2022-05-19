package com.example.beliemeserver.controller.httpexception;

import org.springframework.http.HttpStatus;

public class MethodNotAllowedException extends HttpException {
    public MethodNotAllowedException(Exception e) {
        super(e);
        setHttpStatus(HttpStatus.METHOD_NOT_ALLOWED);
    }
    
    public MethodNotAllowedException(String message) {
        super();
        setHttpStatus(HttpStatus.METHOD_NOT_ALLOWED);
        setMessage(message);
    }
}