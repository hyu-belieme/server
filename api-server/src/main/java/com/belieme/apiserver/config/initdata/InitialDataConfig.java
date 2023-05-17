package com.belieme.apiserver.config.initdata;

import com.belieme.apiserver.config.initdata.container.DepartmentInfo;
import com.belieme.apiserver.config.initdata.container.UniversityInfo;
import com.belieme.apiserver.config.initdata.container.UserInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "init")
public class InitialDataConfig {

  private final Map<String, UniversityInfo> universityInfos;
  private final Map<String, DepartmentInfo> departmentInfos;
  private final List<UserInfo> userInfos;

  public InitialDataConfig(Map<String, UniversityInfo> universityInfos,
      Map<String, DepartmentInfo> departmentInfos, List<UserInfo> userInfos) {
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
