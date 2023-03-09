package com.example.beliemeserver.model.exception;

import com.example.beliemeserver.common.Message;
import com.example.beliemeserver.error.exception.ForbiddenException;

public class PermissionDeniedException extends ForbiddenException {
    @Override
    public String getName() {
        return "PERMISSION_DENIED";
    }

    @Override
    public Message getResponseMessage() {
        return new Message("error.permissionDenied.message");
    }
}
