package com.belieme.apiserver.util.message;

import lombok.Getter;

@Getter
public class Message {
    private final String code;
    private final Object[] args;

    public Message(String code, Object[] args) {
        this.code = code;
        this.args = args;
    }

    public Message(String code) {
        this.code = code;
        this.args = new Object[0];
    }
}
