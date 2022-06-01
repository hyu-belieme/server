package com.example.beliemeserver.controller.httpexception;

import org.springframework.http.HttpStatus;

public class ConflictHttpException extends HttpException {
    public ConflictHttpException(Exception e) {
        super(e);
        setHttpStatus(HttpStatus.CONFLICT);
    }
    
    public ConflictHttpException(String message) {
        super();
        setHttpStatus(HttpStatus.CONFLICT);
        setMessage(message);
    }
}