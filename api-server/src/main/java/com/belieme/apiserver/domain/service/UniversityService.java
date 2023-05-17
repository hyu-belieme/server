package com.belieme.apiserver.domain.service;

import com.belieme.apiserver.config.initdata.InitialDataConfig;
import com.belieme.apiserver.config.initdata.container.UniversityInfo;
import com.belieme.apiserver.domain.dao.AuthorityDao;
import com.belieme.apiserver.domain.dao.DepartmentDao;
import com.belieme.apiserver.domain.dao.HistoryDao;
import com.belieme.apiserver.domain.dao.ItemDao;
import com.belieme.apiserver.domain.dao.MajorDao;
import com.belieme.apiserver.domain.dao.StuffDao;
import com.belieme.apiserver.domain.dao.UniversityDao;
import com.belieme.apiserver.domain.dao.UserDao;
import com.belieme.apiserver.domain.dto.UniversityDto;
import com.belieme.apiserver.domain.dto.UserDto;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
public class UniversityService extends BaseService {

  public UniversityService(InitialDataConfig initialData, UniversityDao universityDao,
      DepartmentDao departmentDao, UserDao userDao, MajorDao majorDao, AuthorityDao authorityDao,
      StuffDao stuffDao, ItemDao itemDao, HistoryDao historyDao) {
    super(initialData, universityDao, departmentDao, userDao, majorDao, authorityDao, stuffDao,
        itemDao, historyDao);
  }

  public void initializeUniversities() {
    for (UniversityInfo university : initialData.universityInfos().values()) {
      if (universityDao.checkExistById(university.id())) {
        universityDao.update(university.id(), university.name(),
            university.externalApiInfo().getOrDefault("url", null));
        continue;
      }
      universityDao.create(university.id(), university.name(),
          university.externalApiInfo().getOrDefault("url", null));
    }
  }

  public List<UniversityDto> getAllList(@NonNull String userToken) {
    UserDto requester = validateTokenAndGetUser(userToken);
    checkDeveloperPermission(requester);

    return universityDao.getAllList();
  }

  public UniversityDto getById(@NonNull String userToken, @NonNull UUID universityId) {
    UserDto requester = validateTokenAndGetUser(userToken);
    checkDeveloperPermission(requester);
    return universityDao.getById(universityId);
  }
}
