package com.example.beliemeserver.model.exception;

import com.example.beliemeserver.config.message.Message;
import com.example.beliemeserver.error.exception.UnauthorizedException;

public class TokenExpiredException extends UnauthorizedException {
    @Override
    public String getName() {
        return "TOKE_EXPIRED";
    }

    @Override
    public Message getResponseMessage() {
        return new Message("message.error.tokenExpired");
    }
}