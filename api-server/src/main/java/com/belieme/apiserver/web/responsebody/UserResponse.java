package com.belieme.apiserver.web.responsebody;

import com.belieme.apiserver.domain.dto.AuthorityDto;
import com.belieme.apiserver.domain.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
public class UserResponse extends JsonResponse {

  private UUID id;
  private UniversityResponse university;
  private String studentId;
  private String name;
  @JsonInclude(JsonInclude.Include.NON_DEFAULT)
  private int entranceYear;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private List<AuthorityResponse> authorities;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String token;
  @JsonInclude(JsonInclude.Include.NON_DEFAULT)
  private long createdAt;
  @JsonInclude(JsonInclude.Include.NON_DEFAULT)
  private long approvedAt;

  private UserResponse(boolean doesJsonInclude) {
    super(doesJsonInclude);
  }

  private UserResponse(UUID id, UniversityResponse university, String studentId, String name,
      int entranceYear, List<AuthorityResponse> authorities, String token, long createdAt,
      long approvedAt) {
    super(true);
    this.id = id;
    this.university = university;
    this.studentId = studentId;
    this.name = name;
    this.entranceYear = entranceYear;
    this.authorities = authorities;
    this.token = token;
    this.createdAt = createdAt;
    this.approvedAt = approvedAt;
  }

  public static UserResponse responseWillBeIgnore() {
    return new UserResponse(false);
  }

  public static UserResponse from(UserDto userDto) {
    if (userDto == null) {
      return null;
    }
    if (userDto.equals(UserDto.nestedEndpoint)) {
      return responseWillBeIgnore();
    }

    List<AuthorityResponse> authorityResponseList = new ArrayList<>();
    for (AuthorityDto authorityDto : userDto.meaningfulAuthorities()) {
      authorityResponseList.add(AuthorityResponse.from(authorityDto));
    }

    return new UserResponse(userDto.id(), UniversityResponse.from(userDto.university()),
        userDto.studentId(), userDto.name(), userDto.entranceYear(), authorityResponseList,
        userDto.token(), userDto.createdAt(), userDto.approvedAt());
  }

  public UserResponse withoutSecureInfo() {
    return new UserResponse(id, university, studentId, name, entranceYear, null, null, 0, 0);
  }
}
