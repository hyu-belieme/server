package com.example.beliemeserver.model.service;

import com.example.beliemeserver.model.dao.*;
import com.example.beliemeserver.model.dto.StuffDto;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StuffService extends BaseService {
    public StuffService(UniversityDao universityDao, DepartmentDao departmentDao, UserDao userDao, MajorDao majorDao, AuthorityDao authorityDao, StuffDao stuffDao, ItemDao itemDao, HistoryDao historyDao) {
        super(universityDao, departmentDao, userDao, majorDao, authorityDao, stuffDao, itemDao, historyDao);
    }

    public List<StuffDto> getListByDepartment(
            @NonNull String userToken,
            @NonNull String universityCode, @NonNull String departmentCode
    ) {
       // TODO Need to implements.
       return new ArrayList<>();
    }

    public StuffDto getByIndex(
            @NonNull String userToken,
            @NonNull String universityCode, @NonNull String departmentCode, @NonNull String name
    ) {
        // TODO Need to implements.
        return null;
    }

    public StuffDto create(
            @NonNull String userToken,
            @NonNull String universityCode, @NonNull String departmentCode,
            @NonNull String name, @NonNull String emoji, Integer amount
    ) {
        // TODO Need to implements.
        return null;
    }

    public StuffDto update(
            @NonNull String userToken,
            @NonNull String universityCode, @NonNull String departmentCode, @NonNull String name,
            String newName, String newEmoji
    ) {
        // TODO Need to implements.
        return null;
    }
}
