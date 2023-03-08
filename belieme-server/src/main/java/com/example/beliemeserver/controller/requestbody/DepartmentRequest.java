package com.example.beliemeserver.controller.requestbody;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class DepartmentRequest {
    @Size(min = 1, max = 10, message = "{error.badRequest.outOfSize}")
    @Pattern(regexp = "[A-Z]+", message = "{error.badRequest.containsNonLargeCapital}")
    @JsonProperty("code")
    String code;

    @Size(min = 1, max = 30)
    @Pattern(regexp = "[\\p{L}\\d() ]+", message = "{error.badRequest.containsNonLetter}")
    @JsonProperty("name")
    String name;

    @JsonProperty("base-majors")
    List<String> baseMajors;
}
