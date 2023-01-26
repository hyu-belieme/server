package com.example.beliemeserver.model.service;

import com.example.beliemeserver.exception.InvalidIndexException;
import com.example.beliemeserver.exception.NotFoundException;
import com.example.beliemeserver.model.dao.*;
import com.example.beliemeserver.model.dto.DepartmentDto;
import com.example.beliemeserver.model.dto.StuffDto;
import lombok.NonNull;
import org.springframework.stereotype.Service;

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
        DepartmentDto department = getDepartmentOrThrowInvalidIndexException(universityCode, departmentCode);
        checkUserPermission(userToken, department);

        return stuffDao.getListByDepartment(universityCode, departmentCode);
    }

    public StuffDto getByIndex(
            @NonNull String userToken,
            @NonNull String universityCode, @NonNull String departmentCode, @NonNull String name
    ) {
        DepartmentDto department = getDepartmentOrThrowInvalidIndexException(universityCode, departmentCode);
        checkUserPermission(userToken, department);

        return stuffDao.getByIndex(universityCode, departmentCode, name);
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

    private DepartmentDto getDepartmentOrThrowInvalidIndexException(String universityCode, String departmentCode) {
        try {
            return departmentDao.getDepartmentByUniversityCodeAndDepartmentCodeData(universityCode, departmentCode);
        } catch (NotFoundException e) {
            throw new InvalidIndexException();
        }
    }
}
