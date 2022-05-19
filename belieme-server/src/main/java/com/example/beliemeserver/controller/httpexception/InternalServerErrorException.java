package com.example.beliemeserver.controller.httpexception;

import org.springframework.http.HttpStatus;

public class InternalServerErrorException extends HttpException {
    public InternalServerErrorException(Exception e) {
        super(e);
        setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    public InternalServerErrorException(String message) {
        super();
        setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        setMessage(message);
    }
}