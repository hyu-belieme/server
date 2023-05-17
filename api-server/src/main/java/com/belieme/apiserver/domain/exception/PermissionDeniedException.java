package com.belieme.apiserver.domain.exception;

import com.belieme.apiserver.error.exception.ForbiddenException;
import com.belieme.apiserver.util.message.Message;

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
