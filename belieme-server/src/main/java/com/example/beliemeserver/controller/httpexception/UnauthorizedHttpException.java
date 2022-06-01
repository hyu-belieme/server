package com.example.beliemeserver.controller.httpexception;

import org.springframework.http.HttpStatus;

public class UnauthorizedHttpException extends HttpException {
    public UnauthorizedHttpException(Exception e) {
        super(e);
        setHttpStatus(HttpStatus.UNAUTHORIZED);
    }
    
    public UnauthorizedHttpException(String message) {
        super();
        setHttpStatus(HttpStatus.UNAUTHORIZED);
        setMessage(message);
    }
}