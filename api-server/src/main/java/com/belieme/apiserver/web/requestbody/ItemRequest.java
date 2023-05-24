package com.belieme.apiserver.web.requestbody;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemRequest {

  @NotNull(message = "{message.error.badRequest.notNull}")
  String stuffId;
}
