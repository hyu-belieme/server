package com.example.beliemeserver.common;

import com.example.beliemeserver.model.dto.DepartmentDto;
import com.example.beliemeserver.model.dto.MajorDto;
import com.example.beliemeserver.model.dto.UniversityDto;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConstructorBinding
@ConfigurationProperties(prefix = "init")
public class InitialInfos {
    private final Map<String, UniversityInfo> universities;
    private final Map<String, DepartmentInfo> departments;
    private final List<UserInfo> users;

    public InitialInfos(Map<String, UniversityInfo> universities, Map<String, DepartmentInfo> departments, List<UserInfo> users) {
        this.universities = universities;
        this.departments = departments;
        this.users = users;
    }

    public Map<String, UniversityDto> universities() {
        Map<String, UniversityDto> output = new HashMap<>();
        universities.forEach((s, universityInfo) ->
                output.put(s, new UniversityDto(
                        universityInfo.code,
                        universityInfo.name,
                        universityInfo.externalApiInfo.getOrDefault("url", null)))
        );

        return output;
    }

    public Map<String, DepartmentDto> departments() {
        Map<String, UniversityDto> universities = universities();
        Map<String, DepartmentDto> output = new HashMap<>();
        departments.forEach((s, departmentInfo) -> {
            UniversityDto university = universities.values().stream()
                    .filter((targetUniversity -> targetUniversity.matchUniqueKey(departmentInfo.universityCode)))
                    .findFirst()
                    .orElse(null);

            if (university == null) throw new RuntimeException();
            List<MajorDto> majors = departmentInfo.baseMajors.stream()
                    .map((majorCode) -> new MajorDto(university, majorCode))
                    .toList();

            output.put(s, new DepartmentDto(
                    university,
                    departmentInfo.code,
                    departmentInfo.name,
                    majors));
        });
        return output;
    }

    public List<UserInfo> users() {
        return users;
    }

    public record UniversityInfo(
            String code,
            String name,
            Map<String, String> externalApiInfo
    ) {

    }

    public record DepartmentInfo(
            String universityCode,
            String code,
            String name,
            List<String> baseMajors
    ) {
    }

    public record UserInfo(
            String apiToken,
            String universityCode,
            String studentId,
            String name,
            List<AuthorityInfo> authorities
    ) {

    }

    public record AuthorityInfo(
            String universityCode,
            String departmentCode,
            String permission
    ) {

    }
}
