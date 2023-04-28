package com.example.beliemeserver.config.initdata._new;

import com.example.beliemeserver.config.initdata._new.container.DepartmentInfo;
import com.example.beliemeserver.config.initdata._new.container.UniversityInfo;
import com.example.beliemeserver.config.initdata._new.container.UserInfo;

import java.util.List;
import java.util.Map;

public interface InitialData {
    Map<String, UniversityInfo> universityInfos();

    Map<String, DepartmentInfo> departmentInfos();

    List<UserInfo> userInfos();
}
