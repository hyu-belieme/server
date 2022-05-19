package com.example.beliemeserver.controller.httpexception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends HttpException {
    public ForbiddenException(Exception e) {
        super(e);
        setHttpStatus(HttpStatus.FORBIDDEN);
    }
    
    public ForbiddenException(String message) {
        super();
        setHttpStatus(HttpStatus.FORBIDDEN);
        setMessage(message);
    }
}