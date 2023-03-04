package com.example.beliemeserver.exception;

import org.springframework.http.HttpStatus;

public interface ErrorInfo {
    String name();
    HttpStatus httpStatus();
    String responseMessage();
}
