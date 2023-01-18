package com.example.beliemeserver.model.service;

import com.example.beliemeserver.model.dao.*;
import com.example.beliemeserver.model.dto.DepartmentDto;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentService extends BaseService {
    public DepartmentService(UniversityDao universityDao, DepartmentDao departmentDao, UserDao userDao, MajorDao majorDao, AuthorityDao authorityDao, StuffDao stuffDao, ItemDao itemDao, HistoryDao historyDao) {
        super(universityDao, departmentDao, userDao, majorDao, authorityDao, stuffDao, itemDao, historyDao);
    }

    public List<DepartmentDto> getAccessibleList(@NonNull String userToken) {
        // TODO Need to implements.
        return new ArrayList<>();
    }

    public DepartmentDto getByIndex(
            @NonNull String userToken, @NonNull String universityCode,
            @NonNull String departmentCode
    ) {
        // TODO Need to implements.
        return null;
    }

    public DepartmentDto create(
            @NonNull String userToken, @NonNull String universityCode,
            @NonNull String departmentCode, @NonNull String name,
            @NonNull List<String> majorCodes
    ) {
        // TODO Need to implements.
        return null;
    }

    public DepartmentDto update(
            @NonNull String userToken, @NonNull String universityCode, @NonNull String departmentCode,
            String newDepartmentCode, String newName, List<String> majorCodes
    ) {
        // TODO Need to implements.
        return null;
    }
}
