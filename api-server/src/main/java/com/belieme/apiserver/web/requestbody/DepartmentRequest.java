package com.belieme.apiserver.web.requestbody;

import com.belieme.apiserver.web.requestbody.validatemarker.DepartmentCreateValidationGroup;
import com.belieme.apiserver.web.requestbody.validatemarker.DepartmentUpdateValidationGroup;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentRequest {

  @NotNull(message = "{message.error.badRequest.notNull}", groups = DepartmentCreateValidationGroup.class)
  @JsonProperty("universityId")
  String universityId;

  @Size(min = 1, max = 30, message = "{message.error.badRequest.outOfSize}", groups = {
      DepartmentCreateValidationGroup.class,
      DepartmentUpdateValidationGroup.class}) @Pattern(regexp = "[\\p{L}\\d() ]+", message = "{message.error.badRequest.containsNonLetter}", groups = {
      DepartmentCreateValidationGroup.class,
      DepartmentUpdateValidationGroup.class}) @NotNull(message = "{message.error.badRequest.notNull}", groups = DepartmentCreateValidationGroup.class)
  @JsonProperty("name")
  String name;

  @NotNull(message = "{message.error.badRequest.notNull}", groups = DepartmentCreateValidationGroup.class)
  @JsonProperty("baseMajors")
  List<String> baseMajors;
}
