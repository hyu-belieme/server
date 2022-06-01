package com.example.beliemeserver.controller.httpexception;

import org.springframework.http.HttpStatus;

public class NotFoundHttpException extends HttpException {
    public NotFoundHttpException(Exception e) {
        super(e);
        setHttpStatus(HttpStatus.NOT_FOUND);
    }
    
    public NotFoundHttpException(String message) {
        super();
        setHttpStatus(HttpStatus.NOT_FOUND);
        setMessage(message);
    }
}