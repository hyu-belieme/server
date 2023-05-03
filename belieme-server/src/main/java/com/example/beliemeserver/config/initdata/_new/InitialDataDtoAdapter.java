package com.example.beliemeserver.config.initdata._new;

import com.example.beliemeserver.config.initdata._new.container.DepartmentInfo;
import com.example.beliemeserver.config.initdata._new.container.UniversityInfo;
import com.example.beliemeserver.config.initdata._new.container.UserInfo;
import com.example.beliemeserver.domain.dto._new.DepartmentDto;
import com.example.beliemeserver.domain.dto._new.MajorDto;
import com.example.beliemeserver.domain.dto._new.UniversityDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class InitialDataDtoAdapter implements InitialData {
    private final InitialData initialData;

    public InitialDataDtoAdapter(InitialData initialData) {
        this.initialData = initialData;
    }

    public Map<String, UniversityInfo> universityInfos() {
        return initialData.universityInfos();
    }

    public Map<String, DepartmentInfo> departmentInfos() {
        return initialData.departmentInfos();
    }

    public List<UserInfo> userInfos() {
        return initialData.userInfos();
    }

    public Map<UUID, UniversityDto> universities() {
        Map<UUID, UniversityDto> output = new HashMap<>();
        initialData.universityInfos().forEach((s, universityInfo) ->
                output.put(universityInfo.id(), toUniversityDto(universityInfo))
        );
        return output;
    }

    public Map<UUID, DepartmentDto> departments() {
        Map<UUID, DepartmentDto> output = new HashMap<>();
        initialData.departmentInfos().forEach((s, departmentInfo) ->
                output.put(departmentInfo.id(), toDepartmentDto(departmentInfo))
        );
        return output;
    }

    private UniversityDto toUniversityDto(UniversityInfo universityInfo) {
        if (universityInfo == null) return null;
        return new UniversityDto(
                universityInfo.id(),
                universityInfo.name(),
                universityInfo.externalApiInfo().getOrDefault("url", null));
    }

    private DepartmentDto toDepartmentDto(DepartmentInfo departmentInfo) {
        if (departmentInfo == null) return null;

        UniversityDto university = universities().get(departmentInfo.universityId());
        if (university == null) throw new RuntimeException();

        List<MajorDto> majors = departmentInfo.baseMajors().stream()
                .map((majorInfo) -> new MajorDto(majorInfo.id(), university, majorInfo.code()))
                .toList();
        return new DepartmentDto(
                departmentInfo.id(),
                university,
                departmentInfo.name(),
                majors);
    }
}
