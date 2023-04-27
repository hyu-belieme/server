package com.example.beliemeserver.domain.exception;

import com.example.beliemeserver.util.message.Message;
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