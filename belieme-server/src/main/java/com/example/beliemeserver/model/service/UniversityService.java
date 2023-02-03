package com.example.beliemeserver.model.service;

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

    public UniversityDto create(
            @NonNull String userToken, @NonNull String universityCode,
            @NonNull String name, String apiUrl
    ) {
        checkDeveloperPermission(userToken);

        UniversityDto newUniversity = new UniversityDto(universityCode, name, apiUrl);
        return universityDao.create(newUniversity);
    }

    public UniversityDto update(
            @NonNull String userToken,
            @NonNull String universityCode,
            String newUniversityCode, String newName, String newApiUrl
    ) {
        checkDeveloperPermission(userToken);

        UniversityDto oldUniversity = universityDao.getByIndex(universityCode);
        if(newUniversityCode == null && newName == null && newApiUrl == null) return oldUniversity;

        if(newUniversityCode == null) newUniversityCode = oldUniversity.code();
        if(newName == null) newName = oldUniversity.name();
        if(newApiUrl == null) newApiUrl = oldUniversity.apiUrl();
        UniversityDto newUniversity = new UniversityDto(newUniversityCode, newName, newApiUrl);

        return universityDao.update(universityCode, newUniversity);
    }
}
