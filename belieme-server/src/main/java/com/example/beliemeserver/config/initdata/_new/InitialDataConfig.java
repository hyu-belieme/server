package com.example.beliemeserver.config.initdata._new;

import com.example.beliemeserver.config.initdata._new.container.DepartmentInfo;
import com.example.beliemeserver.config.initdata._new.container.UniversityInfo;
import com.example.beliemeserver.config.initdata._new.container.UserInfo;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConstructorBinding
@ConfigurationProperties(prefix = "newinit")
public class InitialDataConfig implements InitialData {
    private final Map<String, UniversityInfo> universityInfos;
    private final Map<String, DepartmentInfo> departmentInfos;
    private final List<UserInfo> userInfos;

    public InitialDataConfig(Map<String, UniversityInfo> universityInfos, Map<String, DepartmentInfo> departmentInfos, List<UserInfo> userInfos) {
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
}
