package com.example.beliemeserver.domain.service;

import com.example.beliemeserver.config.initdata._new.InitialDataConfig;
import com.example.beliemeserver.config.initdata._new.container.UniversityInfo;
import com.example.beliemeserver.domain.dao._new.*;
import com.example.beliemeserver.domain.dto._new.UniversityDto;
import com.example.beliemeserver.domain.dto._new.UserDto;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UniversityService extends BaseService {
    public UniversityService(InitialDataConfig initialData, UniversityDao universityDao, DepartmentDao departmentDao, UserDao userDao, MajorDao majorDao, AuthorityDao authorityDao, StuffDao stuffDao, ItemDao itemDao, HistoryDao historyDao) {
        super(initialData, universityDao, departmentDao, userDao, majorDao, authorityDao, stuffDao, itemDao, historyDao);
    }

    public void initializeUniversities() {
        for (UniversityInfo university : initialData.universityInfos().values()) {
            if (universityDao.checkExistById(university.id())) {
                universityDao.update(
                        university.id(),
                        university.name(),
                        university.externalApiInfo().getOrDefault("url", null)
                );
                continue;
            }
            universityDao.create(
                    university.id(),
                    university.name(),
                    university.externalApiInfo().getOrDefault("url", null)
            );
        }
    }

    public List<UniversityDto> getAllList(@NonNull String userToken) {
        UserDto requester = validateTokenAndGetUser(userToken);
        checkDeveloperPermission(requester);

        return universityDao.getAllList();
    }

    public UniversityDto getById(
            @NonNull String userToken, @NonNull UUID universityId
    ) {
        UserDto requester = validateTokenAndGetUser(userToken);
        checkDeveloperPermission(requester);
        return universityDao.getById(universityId);
    }
}
