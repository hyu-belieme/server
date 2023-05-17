package com.belieme.apiserver.web.requestbody;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class ItemRequest {
    @NotNull(message = "{message.error.badRequest.notNull}")
    @JsonProperty("stuff-id")
    String stuffId;
}
