package com.example.beliemeserver.error.info;

import com.example.beliemeserver.util.message.Message;
import org.springframework.http.HttpStatusCode;

public interface ErrorInfo {
    String name();

    HttpStatusCode httpStatus();

    Message responseMessage();
}
