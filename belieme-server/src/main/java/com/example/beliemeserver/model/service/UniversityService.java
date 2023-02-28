package com.example.beliemeserver.model.service;

import com.example.beliemeserver.common.Globals;
import com.example.beliemeserver.model.dao.*;
import com.example.beliemeserver.model.dto.UniversityDto;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UniversityService extends BaseService {
    public UniversityService(UniversityDao universityDao, DepartmentDao departmentDao, UserDao userDao, MajorDao majorDao, AuthorityDao authorityDao, StuffDao stuffDao, ItemDao itemDao, HistoryDao historyDao) {
        super(universityDao, departmentDao, userDao, majorDao, authorityDao, stuffDao, itemDao, historyDao);
    }

    public void initUniversity() {
        for (UniversityDto university : Globals.universities) {
            if (universityDao.checkExistByIndex(university.code())) {
                universityDao.update(university.code(), university);
                continue;
            }
            universityDao.create(university);
        }
    }

    public List<UniversityDto> getAllList(@NonNull String userToken) {
        checkDeveloperPermission(userToken);

        return universityDao.getAllList();
    }

    public UniversityDto getByIndex(
            @NonNull String userToken, @NonNull String universityCode
    ) {
        checkDeveloperPermission(userToken);
        return universityDao.getByIndex(universityCode);
    }
}
