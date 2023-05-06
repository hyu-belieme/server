package com.example.beliemeserver.web.requestbody;

import com.example.beliemeserver.web.requestbody.validatemarker.StuffCreateValidationGroup;
import com.example.beliemeserver.web.requestbody.validatemarker.StuffUpdateValidationGroup;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Getter
@Setter
public class StuffRequest {
    @NotNull(message = "{message.error.badRequest.notNull}",
            groups = StuffCreateValidationGroup.class)
    @JsonProperty("department-id")
    String departmentId;

    @Size(min = 1, max = 30, message = "{message.error.badRequest.outOfSize}",
            groups = {StuffCreateValidationGroup.class, StuffUpdateValidationGroup.class})
    @Pattern(regexp = "[\\p{L}\\d() ]+", message = "{message.error.badRequest.containsNonLetter}",
            groups = {StuffCreateValidationGroup.class, StuffUpdateValidationGroup.class})
    @NotNull(message = "{message.error.badRequest.notNull}",
            groups = StuffCreateValidationGroup.class)
    @JsonProperty("name")
    String name;

    @Size(min = 1, max = 5, message = "{message.error.badRequest.outOfSize}",
            groups = {StuffCreateValidationGroup.class, StuffUpdateValidationGroup.class})
    @NotNull(message = "{message.error.badRequest.notNull}",
            groups = StuffCreateValidationGroup.class)
    @JsonProperty("thumbnail")
    String thumbnail;

    @PositiveOrZero(message = "{message.error.badRequest.containsNegative}",
            groups = StuffCreateValidationGroup.class)
    @NotNull(message = "{message.error.badRequest.notNull}",
            groups = StuffCreateValidationGroup.class)
    @JsonProperty("amount")
    Integer amount;
}
