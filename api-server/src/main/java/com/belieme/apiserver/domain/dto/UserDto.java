package com.belieme.apiserver.domain.dto;

import com.belieme.apiserver.domain.dto.enumeration.Permission;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;

public record UserDto(@NonNull UUID id, @NonNull UniversityDto university,
                      @NonNull String studentId, @NonNull String name, int entranceYear,
                      @NonNull String token, long createdAt, long approvedAt,
                      @NonNull List<AuthorityDto> authorities) {

  private static final UUID NIL_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
  public static final UserDto nestedEndpoint = new UserDto(NIL_UUID, UniversityDto.nestedEndpoint,
      "-", "-", 0, "", 0, 0, new ArrayList<>());

  public UserDto(@NonNull UUID id, @NonNull UniversityDto university, @NonNull String studentId,
      @NonNull String name, int entranceYear, @NonNull String token, long createdAt,
      long approvedAt, @NonNull List<AuthorityDto> authorities) {
    this.id = id;
    this.university = university;
    this.studentId = studentId;
    this.name = name;
    this.entranceYear = entranceYear;
    this.token = token;
    this.createdAt = createdAt;
    this.approvedAt = approvedAt;
    this.authorities = new ArrayList<>(authorities);
  }

  public static UserDto init(@NonNull UniversityDto university, @NonNull String studentId,
      @NonNull String name) {
    return new UserDto(UUID.randomUUID(), university, studentId, name, 0, newToken(), currentTime(),
        currentTime(), new ArrayList<>());
  }

  public static UserDto init(@NonNull UniversityDto university, @NonNull String studentId,
      @NonNull String name, int entranceYear) {
    return new UserDto(UUID.randomUUID(), university, studentId, name, entranceYear, newToken(),
        currentTime(), currentTime(), new ArrayList<>());
  }

  @Override
  public List<AuthorityDto> authorities() {
    return new ArrayList<>(authorities);
  }

  public List<AuthorityDto> meaningfulAuthorities() {
    List<AuthorityDto> output = new ArrayList<>();
    for (AuthorityDto authority : authorities) {
      overwriteAuthority(output, authority);
    }
    return output;
  }

  public UserDto withUniversity(@NonNull UniversityDto university) {
    return new UserDto(id, university, studentId, name, entranceYear, token, createdAt, approvedAt,
        authorities);
  }

  public UserDto withStudentId(@NonNull String studentId) {
    return new UserDto(id, university, studentId, name, entranceYear, token, createdAt, approvedAt,
        authorities);
  }

  public UserDto withName(@NonNull String name) {
    return new UserDto(id, university, studentId, name, entranceYear, token, createdAt, approvedAt,
        authorities);
  }

  public UserDto withEntranceYear(int entranceYear) {
    return new UserDto(id, university, studentId, name, entranceYear, token, createdAt, approvedAt,
        authorities);
  }

  public UserDto withToken(@NonNull String token) {
    return new UserDto(id, university, studentId, name, entranceYear, token, createdAt, approvedAt,
        authorities);
  }

  public UserDto withCreatedAt(long createdAt) {
    return new UserDto(id, university, studentId, name, entranceYear, token, createdAt, approvedAt,
        authorities);
  }

  public UserDto withApprovedAt(long approvedAt) {
    return new UserDto(id, university, studentId, name, entranceYear, token, createdAt, approvedAt,
        authorities);
  }

  public UserDto withAuthorities(@NonNull List<AuthorityDto> authorities) {
    return new UserDto(id, university, studentId, name, entranceYear, token, createdAt, approvedAt,
        authorities);
  }

  public UserDto withAuthorityAdd(AuthorityDto authorityDto) {
    UserDto output = new UserDto(id, university, studentId, name, entranceYear, token, createdAt,
        approvedAt, authorities);
    output.authorities.add(authorityDto);
    return output;
  }

  public UserDto withAuthorityRemove(DepartmentDto department) {
    UserDto output = new UserDto(id, university, studentId, name, entranceYear, token, createdAt,
        approvedAt, authorities);
    output.authorities.removeIf((authority) -> department.matchId(authority.department()));
    return output;
  }

  @Override
  public String toString() {
    if (this.equals(nestedEndpoint)) {
      return "omitted";
    }
    return "UserDto{" + "id=" + id + ", university=" + university + ", studentId='" + studentId
        + '\'' + ", name='" + name + '\'' + ", entranceYear=" + entranceYear + ", token='" + token
        + '\'' + ", createdAt=" + createdAt + ", approvedAt=" + approvedAt + ", authorities="
        + authorities + '}';
  }

  public boolean matchId(UserDto oth) {
    if (oth == null) {
      return false;
    }
    return this.id.equals(oth.id);
  }

  public boolean isDeveloper() {
    for (AuthorityDto authority : authorities) {
      if (authority.permission().hasDeveloperPermission()) {
        return true;
      }
    }
    return false;
  }

  public Permission getMaxPermission(DepartmentDto department) {
    if (isDeveloper()) {
      return Permission.DEVELOPER;
    }

    Permission maxPermission = Permission.BANNED;
    for (AuthorityDto authority : authorities) {
      if (department.equals(authority.department())
          && authority.permission() == Permission.DEFAULT) {
        maxPermission = Permission.USER;
        break;
      }
    }

    for (AuthorityDto authority : authorities) {
      if (department.equals(authority.department())
          && authority.permission() != Permission.DEFAULT) {
        maxPermission = authority.permission();
        break;
      }
    }
    return maxPermission;
  }

  public Permission getMaxPermission() {
    if (isDeveloper()) {
      return Permission.DEVELOPER;
    }

    Permission maxPermission = Permission.BANNED;
    for (AuthorityDto authority : authorities) {
      if (authority.permission() == Permission.DEFAULT) {
        maxPermission = Permission.USER;
        break;
      }
    }

    for (AuthorityDto authority : authorities) {
      if (authority.permission() != Permission.DEFAULT) {
        maxPermission = authority.permission();
        break;
      }
    }
    return maxPermission;
  }

  private void overwriteAuthority(List<AuthorityDto> list, AuthorityDto newAuthority) {
    DepartmentDto targetDepartment = newAuthority.department();
    Permission newPermission = newAuthority.permission();
    if (newPermission == Permission.DEFAULT) {
      newPermission = Permission.USER;
    }

    boolean notExist = true;
    for (int i = 0; i < list.size(); i++) {
      DepartmentDto department = list.get(i).department();
      if (!department.matchId(targetDepartment)) {
        continue;
      }
      notExist = false;

      if (newAuthority.permission() != Permission.DEFAULT) {
        list.set(i, new AuthorityDto(department, newPermission));
      }
      break;
    }
    if (notExist) {
      list.add(new AuthorityDto(targetDepartment, newPermission));
    }
  }

  private static long currentTime() {
    return System.currentTimeMillis() / 1000;
  }

  private static String newToken() {
    return UUID.randomUUID().toString();
  }
}
