package com.example.beliemeserver.controller.requestbody;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Getter
@Setter
public class StuffRequest {
    @Size(min= 1, max= 30, message = "{error.badRequest.outOfSize.message}")
    @Pattern(regexp = "[\\p{L}\\d() ]+", message = "{error.badRequest.containsNonLetter.message}")
    @JsonProperty("name")
    String name;

    @Size(min= 1, max= 5, message = "{error.badRequest.outOfSize.message}")
    @JsonProperty("emoji")
    String emoji;

    @PositiveOrZero(message = "{error.badRequest.containsNegative.message}")
    @JsonProperty("amount")
    Integer amount;
}
