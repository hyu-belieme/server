package com.example.beliemeserver.controller.responsebody.old;

public class OldExceptionResponse {
    private String name;
    private String message;
    public OldExceptionResponse(String name, String message) {
        this.name = name;
        this.message = message;
    }

    public String getName() { return name; }
    public String getMessage() { return message; }
}
