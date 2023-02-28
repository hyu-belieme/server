package com.example.beliemeserver.controller.responsebody;

import com.example.beliemeserver.model.dto.UniversityDto;
import lombok.Getter;

@Getter
public class UniversityResponse extends JsonResponse {
    private String code;
    private String name;

    private UniversityResponse(boolean doesJsonInclude) {
        super(doesJsonInclude);
    }

    private UniversityResponse(String code, String name) {
        super(true);
        this.code = code;
        this.name = name;
    }

    public static UniversityResponse responseWillBeIgnore() {
        return new UniversityResponse(false);
    }

    public static UniversityResponse from(UniversityDto universityDto) {
        if (universityDto == null) return null;
        if (universityDto.equals(UniversityDto.nestedEndpoint)) {
            return responseWillBeIgnore();
        }
        return new UniversityResponse(universityDto.code(), universityDto.name());
    }
}
