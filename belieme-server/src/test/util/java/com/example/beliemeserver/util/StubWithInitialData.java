package com.example.beliemeserver.util;

import com.example.beliemeserver.config.initdata.InitialDataConfig;
import com.example.beliemeserver.config.initdata.container.*;
import com.example.beliemeserver.domain.dto.AuthorityDto;
import com.example.beliemeserver.domain.dto.DepartmentDto;
import com.example.beliemeserver.domain.dto.UserDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StubWithInitialData extends StubData {
    public final InitialDataConfig INIT_DATA;

    public final UniversityInfo DEV_UNIV_INIT_INFO;
    public final UniversityInfo HYU_UNIV_INIT_INFO;

    public final DepartmentInfo DEV_DEV_DEPT_INIT_INFO;
    public final DepartmentInfo HYU_CSE_DEPT_INIT_INFO;
    public final DepartmentInfo HYU_ME_DEPT_INIT_INFO;

    public final UserInfo DEV_1_USER_INIT_INFO;
    public final UserInfo DEV_2_USER_INIT_INFO;
    public final UserInfo HYU_CSE_TESTER_1_USER_INIT_INFO;
    public final UserInfo HYU_ME_TESTER_1_USER_INIT_INFO;


    public StubWithInitialData() {
        // Initial Universities
        Map<String, String> apiInfos = new HashMap<>();
        DEV_UNIV_INIT_INFO = new UniversityInfo(DEV_UNIV.id(), DEV_UNIV.name(), apiInfos);

        apiInfos = new HashMap<>();
        apiInfos.put("url", HYU_UNIV.apiUrl());
        apiInfos.put("client-token", "5448db157878445bb4392355b15a03fa");
        HYU_UNIV_INIT_INFO = new UniversityInfo(HYU_UNIV.id(), HYU_UNIV.name(), apiInfos);

        Map<String, UniversityInfo> universities = new HashMap<>();
        universities.put("DEV", DEV_UNIV_INIT_INFO);
        universities.put("HYU", HYU_UNIV_INIT_INFO);

        // Initial Departments
        DEV_DEV_DEPT_INIT_INFO = makeDepartmentInfo(DEV_DEPT);
        HYU_CSE_DEPT_INIT_INFO = makeDepartmentInfo(HYU_CSE_DEPT);
        HYU_ME_DEPT_INIT_INFO = makeDepartmentInfo(HYU_ME_DEPT);

        Map<String, DepartmentInfo> departments = new HashMap<>();
        departments.put("DEV_DEV", DEV_DEV_DEPT_INIT_INFO);
        departments.put("HYU_CSE", HYU_CSE_DEPT_INIT_INFO);
        departments.put("HYU_ME", HYU_ME_DEPT_INIT_INFO);

        // Initial Users
        DEV_1_USER_INIT_INFO = makeUserInfo("eca3fb44-4f94-4076-97f9-c9798ccdac91", DEV_1_USER);
        DEV_2_USER_INIT_INFO = makeUserInfo("901e2d84-c2b1-40d6-a5ba-831e13f52941", DEV_2_USER);
        HYU_CSE_TESTER_1_USER_INIT_INFO = makeUserInfo("5eb63816-1930-40cd-94c0-8297fdeb0f4c", HYU_CSE_TESTER_1_USER);
        HYU_ME_TESTER_1_USER_INIT_INFO = makeUserInfo("c4a23e50-ab92-4b7a-a4c3-3a6bb27bdcad", HYU_ME_TESTER_1_USER);

        List<UserInfo> users = List.of(DEV_1_USER_INIT_INFO, DEV_2_USER_INIT_INFO,
                HYU_CSE_TESTER_1_USER_INIT_INFO, HYU_ME_TESTER_1_USER_INIT_INFO);

        INIT_DATA = new InitialDataConfig(universities, departments, users);
    }

    private DepartmentInfo makeDepartmentInfo(DepartmentDto deptDto) {
        return new DepartmentInfo(
                deptDto.id(),
                deptDto.university().id(),
                deptDto.name(),
                deptDto.baseMajors().stream()
                        .map(e -> new MajorInfo(deptDto.university().id(), e.code())).toList()
        );
    }

    private UserInfo makeUserInfo(String apiToken, UserDto userDto) {
        List<AuthorityInfo> authorityInfos = new ArrayList<>();
        for (AuthorityDto authority : userDto.authorities()) {
            authorityInfos.add(new AuthorityInfo(
                    authority.department().id(), authority.permission().toString()));
        }

        return new UserInfo(
                userDto.id(), apiToken, userDto.university().id(), userDto.studentId(),
                userDto.name(), userDto.entranceYear(), authorityInfos);
    }
}
