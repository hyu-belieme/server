package com.example.beliemeserver.controller.httpexception;

import org.springframework.http.HttpStatus;

public class GateWayTimeOutException extends HttpException {
    public GateWayTimeOutException(Exception e) {
        super(e);
        setHttpStatus(HttpStatus.GATEWAY_TIMEOUT);
    }
    
    public GateWayTimeOutException(String message) {
        super();
        setHttpStatus(HttpStatus.GATEWAY_TIMEOUT);
        setMessage(message);
    }
}