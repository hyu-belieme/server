package com.example.beliemeserver.web.requestbody;

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
    @Size(min = 1, max = 10, message = "{error.badRequest.outOfSize.message}",
            groups = {DepartmentCreateValidationGroup.class, DepartmentUpdateValidationGroup.class})
    @Pattern(regexp = "[A-Z]+", message = "{error.badRequest.containsNonLargeCapital.message}",
            groups = {DepartmentCreateValidationGroup.class, DepartmentUpdateValidationGroup.class})
    @NotNull(message = "{error.badRequest.notNull.message}",
            groups = DepartmentCreateValidationGroup.class)
    @JsonProperty("code")
    String code;

    @Size(min = 1, max = 30, message = "{error.badRequest.outOfSize.message}",
            groups = {DepartmentCreateValidationGroup.class, DepartmentUpdateValidationGroup.class})
    @Pattern(regexp = "[\\p{L}\\d() ]+", message = "{error.badRequest.containsNonLetter.message}",
            groups = {DepartmentCreateValidationGroup.class, DepartmentUpdateValidationGroup.class})
    @NotNull(message = "{error.badRequest.notNull.message}",
            groups = DepartmentCreateValidationGroup.class)
    @JsonProperty("name")
    String name;

    @NotNull(message = "{error.badRequest.notNull.message}",
            groups = DepartmentCreateValidationGroup.class)
    @JsonProperty("base-majors")
    List<String> baseMajors;
}
