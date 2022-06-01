package com.example.beliemeserver.controller.httpexception;

import org.springframework.http.HttpStatus;

public class ForbiddenHttpException extends HttpException {
    public ForbiddenHttpException(Exception e) {
        super(e);
        setHttpStatus(HttpStatus.FORBIDDEN);
    }
    
    public ForbiddenHttpException(String message) {
        super();
        setHttpStatus(HttpStatus.FORBIDDEN);
        setMessage(message);
    }
}