package com.example.beliemeserver.model.service;

import com.example.beliemeserver.model.dao.*;
import com.example.beliemeserver.model.dto.UniversityDto;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UniversityService extends BaseService {
    public UniversityService(UniversityDao universityDao, DepartmentDao departmentDao, UserDao userDao, MajorDao majorDao, AuthorityDao authorityDao, StuffDao stuffDao, ItemDao itemDao, HistoryDao historyDao) {
        super(universityDao, departmentDao, userDao, majorDao, authorityDao, stuffDao, itemDao, historyDao);
    }

    public List<UniversityDto> getAllList(@NonNull String userToken) {
        // TODO Need to implements.
        return new ArrayList<>();
    }

    public UniversityDto getByIndex(
            @NonNull String userToken, @NonNull String universityCode
    ) {
        // TODO Need to implements.
        return null;
    }

    public UniversityDto create(
            @NonNull String userToken, @NonNull String universityCode,
            @NonNull String name, String apiUrl
    ) {
        // TODO Need to implements.
        return null;
    }

    public UniversityDto update(
            @NonNull String userToken,
            @NonNull String universityCode,
            String newUniversityCode, String newName, String newApiUrl
    ) {
        // TODO Need to implements.
        return null;
    }
}
