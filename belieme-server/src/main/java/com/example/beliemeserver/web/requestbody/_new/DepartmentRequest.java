package com.example.beliemeserver.web.requestbody._new;

import com.example.beliemeserver.web.requestbody.validatemarker.DepartmentCreateValidationGroup;
import com.example.beliemeserver.web.requestbody.validatemarker.DepartmentUpdateValidationGroup;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class DepartmentRequest {
    @Size(min = 1, max = 30, message = "{message.error.badRequest.outOfSize}",
            groups = {DepartmentCreateValidationGroup.class, DepartmentUpdateValidationGroup.class})
    @Pattern(regexp = "[\\p{L}\\d() ]+", message = "{message.error.badRequest.containsNonLetter}",
            groups = {DepartmentCreateValidationGroup.class, DepartmentUpdateValidationGroup.class})
    @NotNull(message = "{message.error.badRequest.notNull}",
            groups = DepartmentCreateValidationGroup.class)
    @JsonProperty("name")
    String name;

    @NotNull(message = "{message.error.badRequest.notNull}",
            groups = DepartmentCreateValidationGroup.class)
    @JsonProperty("base-majors")
    List<String> baseMajors;
}
