package com.belieme.apiserver.domain.exception;

import com.belieme.apiserver.error.exception.ForbiddenException;
import com.belieme.apiserver.util.message.Message;

public class ReturnRegistrationRequestedOnReturnedItemException extends ForbiddenException {

  @Override
  public String getName() {
    return "RETURN_REGISTRATION_REQUESTED_ON_RETURNED_ITEM";
  }

  @Override
  public Message getResponseMessage() {
    return new Message("message.error.returnRegistrationRequestedOnReturnedItem");
  }
}
