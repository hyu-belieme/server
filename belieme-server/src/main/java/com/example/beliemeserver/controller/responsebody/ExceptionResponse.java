package com.example.beliemeserver.controller.responsebody;

public class ExceptionResponse {
    private String name;
    private String message;
    public ExceptionResponse(String name, String message) {
        this.name = name;
        this.message = message;
    }

    public String getName() { return name; }
    public String getMessage() { return message; }
}
