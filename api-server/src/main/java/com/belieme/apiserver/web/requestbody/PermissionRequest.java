package com.belieme.apiserver.web.requestbody;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissionRequest {

  @NotNull(message = "{message.error.badRequest.notNull}")
  String userId;

  @NotNull(message = "{message.error.badRequest.notNull}")
  String departmentId;

  @NotNull(message = "{message.error.badRequest.notNull}")
  String permission;
}
