package com.example.beliemeserver.config.initdata;

import com.example.beliemeserver.config.initdata.container.DepartmentInfo;
import com.example.beliemeserver.config.initdata.container.UniversityInfo;
import com.example.beliemeserver.config.initdata.container.UserInfo;

import java.util.List;
import java.util.Map;

public interface InitialData {
    Map<String, UniversityInfo> universityInfos();

    Map<String, DepartmentInfo> departmentInfos();

    List<UserInfo> userInfos();
}
