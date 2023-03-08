package com.example.beliemeserver.exception;

import com.example.beliemeserver.common.Message;
import org.springframework.http.HttpStatus;

public interface ErrorInfo {
    String name();
    HttpStatus httpStatus();
    Message responseMessage();
}
