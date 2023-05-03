package com.example.beliemeserver.web.requestbody._new;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ItemRequest {
    @NotNull(message = "{message.error.badRequest.notNull}")
    @JsonProperty("stuff-id")
    String stuffId;
}
