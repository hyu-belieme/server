package com.example.beliemeserver.error.info;

import com.example.beliemeserver.config.message.Message;
import org.springframework.http.HttpStatus;

public interface ErrorInfo {
    String name();
    HttpStatus httpStatus();
    Message responseMessage();
}
