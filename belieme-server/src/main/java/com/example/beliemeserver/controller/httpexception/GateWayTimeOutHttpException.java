package com.example.beliemeserver.controller.httpexception;

import org.springframework.http.HttpStatus;

public class GateWayTimeOutHttpException extends HttpException {
    public GateWayTimeOutHttpException(Exception e) {
        super(e);
        setHttpStatus(HttpStatus.GATEWAY_TIMEOUT);
    }
    
    public GateWayTimeOutHttpException(String message) {
        super();
        setHttpStatus(HttpStatus.GATEWAY_TIMEOUT);
        setMessage(message);
    }
}