package com.example.beliemeserver.web.requestbody;

import com.example.beliemeserver.web.requestbody.validatemarker.StuffCreateValidationGroup;
import com.example.beliemeserver.web.requestbody.validatemarker.StuffUpdateValidationGroup;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Getter
@Setter
public class StuffRequest {
    @Size(min= 1, max= 30, message = "{error.badRequest.outOfSize.message}",
            groups = {StuffCreateValidationGroup.class, StuffUpdateValidationGroup.class})
    @Pattern(regexp = "[\\p{L}\\d() ]+", message = "{error.badRequest.containsNonLetter.message}",
            groups = {StuffCreateValidationGroup.class, StuffUpdateValidationGroup.class})
    @NotNull(message = "{error.badRequest.notNull.message}",
            groups = StuffCreateValidationGroup.class)
    @JsonProperty("name")
    String name;

    @Size(min= 1, max= 5, message = "{error.badRequest.outOfSize.message}",
            groups = {StuffCreateValidationGroup.class, StuffUpdateValidationGroup.class})
    @NotNull(message = "{error.badRequest.notNull.message}",
            groups = StuffCreateValidationGroup.class)
    @JsonProperty("emoji")
    String emoji;

    @PositiveOrZero(message = "{error.badRequest.containsNegative.message}",
            groups = StuffCreateValidationGroup.class)
    @NotNull(message = "{error.badRequest.notNull.message}",
            groups = StuffCreateValidationGroup.class)
    @JsonProperty("amount")
    Integer amount;
}
