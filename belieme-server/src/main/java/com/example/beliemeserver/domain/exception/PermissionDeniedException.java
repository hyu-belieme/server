package com.example.beliemeserver.domain.exception;

import com.example.beliemeserver.config.message.Message;
import com.example.beliemeserver.error.exception.ForbiddenException;

public class PermissionDeniedException extends ForbiddenException {
    @Override
    public String getName() {
        return "PERMISSION_DENIED";
    }

    @Override
    public Message getResponseMessage() {
        return new Message("message.error.permissionDenied");
    }
}
