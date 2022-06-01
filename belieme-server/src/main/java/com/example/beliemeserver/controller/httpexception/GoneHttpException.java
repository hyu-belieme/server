package com.example.beliemeserver.controller.httpexception;

import org.springframework.http.HttpStatus;

public class GoneHttpException extends HttpException {
    public GoneHttpException(Exception e) {
        super(e);
        setHttpStatus(HttpStatus.GONE);
    }
    
    public GoneHttpException(String message) {
        super();
        setHttpStatus(HttpStatus.GONE);
        setMessage(message);
    }
}