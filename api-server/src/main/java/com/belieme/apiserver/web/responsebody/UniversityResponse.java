package com.belieme.apiserver.web.responsebody;

import com.belieme.apiserver.domain.dto.UniversityDto;
import lombok.Getter;

import java.util.UUID;

@Getter
public class UniversityResponse extends JsonResponse {
    private UUID id;
    private String name;

    private UniversityResponse(boolean doesJsonInclude) {
        super(doesJsonInclude);
    }

    private UniversityResponse(UUID id, String name) {
        super(true);
        this.id = id;
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
        return new UniversityResponse(universityDto.id(), universityDto.name());
    }
}
