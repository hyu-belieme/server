package com.example.beliemeserver.config.initdata;

import com.example.beliemeserver.config.initdata.container.DepartmentInfo;
import com.example.beliemeserver.config.initdata.container.UniversityInfo;
import com.example.beliemeserver.config.initdata.container.UserInfo;
import com.example.beliemeserver.domain.dto.DepartmentDto;
import com.example.beliemeserver.domain.dto.MajorDto;
import com.example.beliemeserver.domain.dto.UniversityDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<String, UniversityDto> universities() {
        Map<String, UniversityDto> output = new HashMap<>();
        initialData.universityInfos().forEach((s, universityInfo) ->
                output.put(s, toUniversityDto(universityInfo))
        );
        return output;
    }

    public Map<String, DepartmentDto> departments() {
        Map<String, DepartmentDto> output = new HashMap<>();
        initialData.departmentInfos().forEach((s, departmentInfo) ->
                output.put(s, toDepartmentDto(departmentInfo))
        );
        return output;
    }

    private UniversityDto toUniversityDto(UniversityInfo universityInfo) {
        if (universityInfo == null) return null;
        return new UniversityDto(
                universityInfo.code(),
                universityInfo.name(),
                universityInfo.externalApiInfo().getOrDefault("url", null));
    }

    private DepartmentDto toDepartmentDto(DepartmentInfo departmentInfo) {
        if (departmentInfo == null) return null;

        UniversityDto university = universities().get(departmentInfo.universityCode());
        if (university == null) throw new RuntimeException();

        List<MajorDto> majors = departmentInfo.baseMajors().stream()
                .map((majorCode) -> new MajorDto(university, majorCode))
                .toList();
        return new DepartmentDto(
                university,
                departmentInfo.code(),
                departmentInfo.name(),
                majors);
    }
}
