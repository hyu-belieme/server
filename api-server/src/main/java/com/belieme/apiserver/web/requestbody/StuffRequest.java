package com.belieme.apiserver.web.requestbody;

import com.belieme.apiserver.web.requestbody.validatemarker.StuffCreateValidationGroup;
import com.belieme.apiserver.web.requestbody.validatemarker.StuffUpdateValidationGroup;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StuffRequest {

  @NotNull(message = "{message.error.badRequest.notNull}", groups = StuffCreateValidationGroup.class)
  String departmentId;

  @Size(min = 1, max = 30, message = "{message.error.badRequest.outOfSize}", groups = {
      StuffCreateValidationGroup.class,
      StuffUpdateValidationGroup.class})
  @Pattern(regexp = "[\\p{L}\\d() ]+", message = "{message.error.badRequest.containsNonLetter}", groups = {
      StuffCreateValidationGroup.class,
      StuffUpdateValidationGroup.class})
  @NotNull(message = "{message.error.badRequest.notNull}", groups = StuffCreateValidationGroup.class)
  String name;

  @Size(min = 1, max = 5, message = "{message.error.badRequest.outOfSize}", groups = {
      StuffCreateValidationGroup.class,
      StuffUpdateValidationGroup.class})
  @NotNull(message = "{message.error.badRequest.notNull}", groups = StuffCreateValidationGroup.class)
  String thumbnail;

  @Size(min = 1, max = 1023, message = "{message.error.badRequest.outOfSize}", groups = {
      StuffCreateValidationGroup.class,
      StuffUpdateValidationGroup.class})
  @NotNull(message = "{message.error.badRequest.notNull}", groups = StuffCreateValidationGroup.class)
  String desc;

  @PositiveOrZero(message = "{message.error.badRequest.containsNegative}", groups = StuffCreateValidationGroup.class) @NotNull(message = "{message.error.badRequest.notNull}", groups = StuffCreateValidationGroup.class)
  Integer amount;
}
