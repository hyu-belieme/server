package com.example.beliemeserver.controller.httpexception;

import org.springframework.http.HttpStatus;

public class ConflictException extends HttpException {
    public ConflictException(Exception e) {
        super(e);
        setHttpStatus(HttpStatus.CONFLICT);
    }
    
    public ConflictException(String message) {
        super();
        setHttpStatus(HttpStatus.CONFLICT);
        setMessage(message);
    }
}