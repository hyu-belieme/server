package com.example.beliemeserver.controller.httpexception;

import org.springframework.http.HttpStatus;

public class BadRequestHttpException extends HttpException {
    public BadRequestHttpException(Exception e) {
        super(e);
        setHttpStatus(HttpStatus.BAD_REQUEST);
    }
    
    public BadRequestHttpException(String message) {
        super();
        setHttpStatus(HttpStatus.BAD_REQUEST);
        setMessage(message);
    }
}