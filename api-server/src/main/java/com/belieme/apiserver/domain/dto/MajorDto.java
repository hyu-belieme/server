package com.belieme.apiserver.domain.dto;

import java.util.UUID;
import lombok.NonNull;

public record MajorDto(@NonNull UUID id, @NonNull UniversityDto university, @NonNull String code) {

  private static final UUID NIL_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
  public static final MajorDto nestedEndpoint = new MajorDto(NIL_UUID, UniversityDto.nestedEndpoint,
      "-");

  public static MajorDto init(@NonNull UniversityDto university, @NonNull String code) {
    return new MajorDto(UUID.randomUUID(), university, code);
  }

  public MajorDto withUniversity(@NonNull UniversityDto university) {
    return new MajorDto(id, university, code);
  }

  public MajorDto withCode(@NonNull String code) {
    return new MajorDto(id, university, code);
  }

  public boolean matchId(MajorDto oth) {
    if (oth == null) {
      return false;
    }
    return this.id.equals(oth.id);
  }

  @Override
  public String toString() {
    if (this.equals(nestedEndpoint)) {
      return "omitted";
    }
    return "MajorDto{" + "id='" + id + '\'' + ", university=" + university + ", code='" + code
        + '\'' + '}';
  }
}
