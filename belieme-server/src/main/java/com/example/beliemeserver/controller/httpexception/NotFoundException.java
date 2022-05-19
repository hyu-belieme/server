package com.example.beliemeserver.controller.httpexception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends HttpException {
    public NotFoundException(Exception e) {
        super(e);
        setHttpStatus(HttpStatus.NOT_FOUND);
    }
    
    public NotFoundException(String message) {
        super();
        setHttpStatus(HttpStatus.NOT_FOUND);
        setMessage(message);
    }
}