package com.example.beliemeserver.util;

import com.example.beliemeserver.common.InitialInfos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StubInitialInfos {
    public final InitialInfos initialInfos;

    public StubInitialInfos() {
        Map<String, InitialInfos.UniversityInfo> universities = makeUniversityStubs();
        Map<String, InitialInfos.DepartmentInfo> departments = makeDepartmentStubs();
        List<InitialInfos.UserInfo> users = makeUserStubs();

        initialInfos = new InitialInfos(universities, departments, users);
    }

    private Map<String, InitialInfos.UniversityInfo> makeUniversityStubs() {
        Map<String, InitialInfos.UniversityInfo> universities = new HashMap<>();
        Map<String, String> apiInfos = new HashMap<>();
        universities.put("DEV", new InitialInfos.UniversityInfo("DEV", "DEV", apiInfos));

        apiInfos = new HashMap<>();
        apiInfos.put("url", "https://api.hanyang.ac.kr/login");
        apiInfos.put("client-token", "5448db157878445bb4392355b15a03fa");
        universities.put("HYU", new InitialInfos.UniversityInfo("HYU", "한양대학교", apiInfos));
        return universities;
    }

    private Map<String, InitialInfos.DepartmentInfo> makeDepartmentStubs() {
        Map<String, InitialInfos.DepartmentInfo> departments = new HashMap<>();
        departments.put("DEV_DEV", new InitialInfos.DepartmentInfo("DEV", "DEV", "DEV", new ArrayList<>()));
        departments.put("DEV_TEST", new InitialInfos.DepartmentInfo("DEV", "TEST", "TEST", new ArrayList<>()));
        departments.put("HYU_CSE", new InitialInfos.DepartmentInfo("HYU", "CSE", "컴퓨터소프트웨어학부", List.of("FH04067", "FH04068")));
        departments.put("HYU_ME", new InitialInfos.DepartmentInfo("HYU", "ME", "기계공학부", List.of("FH04069")));
        return departments;
    }

    private List<InitialInfos.UserInfo> makeUserStubs() {
        List<InitialInfos.UserInfo> users = new ArrayList<>();

        List<InitialInfos.AuthorityInfo> authorityInfos = new ArrayList<>();
        authorityInfos.add(new InitialInfos.AuthorityInfo("DEV", "DEV", "DEVELOPER"));
        users.add(new InitialInfos.UserInfo("eca3fb44-4f94-4076-97f9-c9798ccdac91", "DEV", "DEV1", "개발자 1", authorityInfos));

        authorityInfos = new ArrayList<>();
        authorityInfos.add(new InitialInfos.AuthorityInfo("DEV", "DEV", "DEVELOPER"));
        users.add(new InitialInfos.UserInfo("901e2d84-c2b1-40d6-a5ba-831e13f52941", "DEV", "DEV2", "개발자 2", authorityInfos));

        authorityInfos = new ArrayList<>();
        authorityInfos.add(new InitialInfos.AuthorityInfo("HYU", "CSE", "STAFF"));
        authorityInfos.add(new InitialInfos.AuthorityInfo("HYU", "ME", "USER"));
        users.add(new InitialInfos.UserInfo("5eb63816-1930-40cd-94c0-8297fdeb0f4c", "HYU", "TESTER1", "테스터 1", authorityInfos));
        return users;
    }
}
