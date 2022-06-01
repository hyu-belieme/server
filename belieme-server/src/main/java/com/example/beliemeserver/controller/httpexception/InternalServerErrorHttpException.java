package com.example.beliemeserver.controller.httpexception;

import org.springframework.http.HttpStatus;

public class InternalServerErrorHttpException extends HttpException {
    public InternalServerErrorHttpException(Exception e) {
        super(e);
        setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    public InternalServerErrorHttpException(String message) {
        super();
        setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        setMessage(message);
    }
}