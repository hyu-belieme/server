package com.example.beliemeserver.domain.service._new;

import com.example.beliemeserver.config.initdata._new.InitialData;
import com.example.beliemeserver.domain.dao._new.*;
import com.example.beliemeserver.domain.dto._new.UniversityDto;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class NewUniversityService extends NewBaseService {
    public NewUniversityService(InitialData initialData, UniversityDao universityDao, DepartmentDao departmentDao, UserDao userDao, MajorDao majorDao, AuthorityDao authorityDao, StuffDao stuffDao, ItemDao itemDao, HistoryDao historyDao) {
        super(initialData, universityDao, departmentDao, userDao, majorDao, authorityDao, stuffDao, itemDao, historyDao);
    }

    public void initializeUniversities() {
        for (UniversityDto university : initialData.universities().values()) {
            if (universityDao.checkExistById(university.id())) {
                universityDao.update(university.id(), university);
                continue;
            }
            universityDao.create(university);
        }
    }

    public List<UniversityDto> getAllList(@NonNull String userToken) {
        checkDeveloperPermission(userToken);

        return universityDao.getAllList();
    }

    public UniversityDto getById(
            @NonNull String userToken, @NonNull UUID universityId
    ) {
        checkDeveloperPermission(userToken);
        return universityDao.getById(universityId);
    }
}
