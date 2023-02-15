package com.example.beliemeserver.controller.requestbody;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StuffRequest {
    @JsonProperty("name") String name;
    @JsonProperty("emoji") String emoji;
    @JsonProperty("amount") Integer amount;
}
