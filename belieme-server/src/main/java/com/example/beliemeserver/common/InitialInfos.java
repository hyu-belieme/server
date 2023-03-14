package com.example.beliemeserver.common;

import com.example.beliemeserver.model.dto.DepartmentDto;
import com.example.beliemeserver.model.dto.MajorDto;
import com.example.beliemeserver.model.dto.UniversityDto;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.*;

@ConstructorBinding
@ConfigurationProperties(prefix = "init")
public class InitialInfos {
    private final Map<String, UniversityInfo> universityInfos;
    private final Map<String, DepartmentInfo> departmentInfos;
    private final List<UserInfo> userInfos;

    public InitialInfos(Map<String, UniversityInfo> universityInfos, Map<String, DepartmentInfo> departmentInfos, List<UserInfo> userInfos) {
        this.universityInfos = universityInfos;
        this.departmentInfos = departmentInfos;
        this.userInfos = userInfos;
    }

    public Map<String, UniversityInfo> universityInfos() {
        return new HashMap<>(universityInfos);
    }

    public Map<String, DepartmentInfo> departmentInfos() {
        return new HashMap<>(departmentInfos);
    }

    public List<UserInfo> userInfos() {
        return new ArrayList<>(userInfos);
    }

    public Map<String, UniversityDto> universities() {
        Map<String, UniversityDto> output = new HashMap<>();
        universityInfos.forEach((s, universityInfo) ->
                output.put(s, toUniversityDto(universityInfo))
        );
        return output;
    }

    public Map<String, DepartmentDto> departments() {
        Map<String, DepartmentDto> output = new HashMap<>();
        departmentInfos.forEach((s, departmentInfo) ->
                output.put(s, toDepartmentDto(departmentInfo))
        );
        return output;
    }

    private UniversityDto toUniversityDto(UniversityInfo universityInfo) {
        if(universityInfo == null) return null;
        return new UniversityDto(
                universityInfo.code,
                universityInfo.name,
                universityInfo.externalApiInfo.getOrDefault("url", null));
    }

    private DepartmentDto toDepartmentDto(DepartmentInfo departmentInfo) {
        if(departmentInfo == null) return null;

        UniversityDto university = universities().get(departmentInfo.universityCode);
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
