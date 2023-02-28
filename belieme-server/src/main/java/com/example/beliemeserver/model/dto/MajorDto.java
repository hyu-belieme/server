package com.example.beliemeserver.model.dto;

import lombok.NonNull;

public record MajorDto(@NonNull UniversityDto university, @NonNull String code) {
    public static final MajorDto nestedEndpoint = new MajorDto(UniversityDto.nestedEndpoint, "-");

    public MajorDto withUniversity(@NonNull UniversityDto university) {
        return new MajorDto(university, code);
    }

    public MajorDto withCode(@NonNull String code) {
        return new MajorDto(university, code);
    }

    public boolean matchUniqueKey(String universityCode, String majorCode) {
        if (universityCode == null || majorCode == null) {
            return false;
        }
        return universityCode.equals(this.university().code())
                && majorCode.equals(this.code());
    }

    public boolean matchUniqueKey(MajorDto oth) {
        if (oth == null) {
            return false;
        }
        String universityCode = oth.university().code();
        String majorCode = oth.code();
        return universityCode.equals(this.university().code())
                && majorCode.equals(this.code());
    }

    @Override
    public String toString() {
        if (this.equals(nestedEndpoint)) {
            return "omitted";
        }

        return "MajorDto{" +
                "university=" + university +
                ", code='" + code + '\'' +
                '}';
    }
}
