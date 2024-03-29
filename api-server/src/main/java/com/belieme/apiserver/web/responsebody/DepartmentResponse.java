package com.belieme.apiserver.web.responsebody;

import com.belieme.apiserver.domain.dto.DepartmentDto;
import com.belieme.apiserver.domain.dto.MajorDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
public class DepartmentResponse extends JsonResponse {

  private UUID id;
  @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = ResponseFilter.class)
  private UniversityResponse university;
  private String name;
  private List<String> baseMajors;

  private DepartmentResponse(boolean doesJsonInclude) {
    super(doesJsonInclude);
  }

  private DepartmentResponse(UUID id, UniversityResponse university, String name,
      List<String> baseMajors) {
    super(true);
    this.id = id;
    this.university = university;
    this.name = name;
    this.baseMajors = baseMajors;
  }

  public static DepartmentResponse responseWillBeIgnore() {
    return new DepartmentResponse(false);
  }

  public static DepartmentResponse from(DepartmentDto departmentDto) {
    if (departmentDto == null) {
      return null;
    }
    if (departmentDto.equals(DepartmentDto.nestedEndpoint)) {
      return responseWillBeIgnore();
    }

    List<String> baseMajorCodes = new ArrayList<>();
    for (MajorDto major : departmentDto.baseMajors()) {
      baseMajorCodes.add(major.code());
    }

    return new DepartmentResponse(departmentDto.id(),
        UniversityResponse.from(departmentDto.university()), departmentDto.name(), baseMajorCodes);
  }

  public DepartmentResponse withoutUniversity() {
    return new DepartmentResponse(id, UniversityResponse.responseWillBeIgnore(), name, baseMajors);
  }
}
