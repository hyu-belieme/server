package com.example.beliemeserver.domain.dto._new;

import lombok.NonNull;

import java.util.UUID;

public record MajorDto(@NonNull UUID id, @NonNull UniversityDto university, @NonNull String code) {
    private static final UUID NIL_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    public static final MajorDto nestedEndpoint = new MajorDto(NIL_UUID, UniversityDto.nestedEndpoint, "-");

    public static MajorDto init(@NonNull UniversityDto university, @NonNull String code) {
        return new MajorDto(UUID.randomUUID(), university, code);
    }

    public MajorDto withUniversity(@NonNull UniversityDto university) {
        return new MajorDto(id, university, code);
    }

    public MajorDto withCode(@NonNull String code) {
        return new MajorDto(id, university, code);
    }

    public boolean matchUniqueKey(String universityName, String majorCode) {
        if (universityName == null || majorCode == null) {
            return false;
        }
        return university.matchUniqueKey(universityName)
                && majorCode.equals(this.code());
    }

    public boolean matchUniqueKey(MajorDto oth) {
        if (oth == null) {
            return false;
        }
        return matchUniqueKey(oth.university.name(), oth.code);
    }

    @Override
    public String toString() {
        if (this.equals(nestedEndpoint)) {
            return "omitted";
        }
        return "MajorDto{" +
                "id='" + id + '\'' +
                ", university=" + university +
                ", code='" + code + '\'' +
                '}';
    }
}