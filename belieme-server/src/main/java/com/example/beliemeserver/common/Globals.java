package com.example.beliemeserver.common;

import com.example.beliemeserver.model.dto.AuthorityDto;
import com.example.beliemeserver.model.dto.DepartmentDto;
import com.example.beliemeserver.model.dto.UniversityDto;

import java.util.ArrayList;
import java.util.List;

public class Globals {
    public static final UniversityDto DEV_UNIVERSITY = new UniversityDto("DEV", "DEV", "DEV");
    public static final DepartmentDto DEV_DEPARTMENT = new DepartmentDto(DEV_UNIVERSITY, "DEV", "DEV", new ArrayList<>());
    public static final AuthorityDto DEV_AUTHORITY = new AuthorityDto(DEV_DEPARTMENT, AuthorityDto.Permission.DEVELOPER);

    public static final UniversityDto HANYANG_UNIVERSITY = new UniversityDto("HYU", "한양대학교", "https://api.hanyang.ac.kr/rs/user/loginInfo.json");
    public static final String HANYANG_API_CLIENT_TOKEN = "a4b1abe746f384c3d43fa82a17f222";
    public static final String HANYANG_API_URL = "https://api.hanyang.ac.kr/rs/user/loginInfo.json";

    public static final List<DeveloperInfo> developers = List.of(
            new DeveloperInfo("c305ee87-a4c7-4b5a-8d71-7e23b6064613", "DEV1", "개발자1")
    );
}