package com.example.beliemeserver.model.service;

import com.example.beliemeserver.model.dao.*;
import com.example.beliemeserver.model.dto.AuthorityDto;
import com.example.beliemeserver.model.dto.DepartmentDto;
import com.example.beliemeserver.model.dto.UserDto;
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
        List<DepartmentDto> output = new ArrayList<>();

        UserDto requester = checkTokenAndGetUser(userToken);
        List<DepartmentDto> allDepartment = departmentDao.getAllDepartmentsData();
        for(DepartmentDto department : allDepartment) {
            if(requester.getMaxPermission(department).hasUserPermission()) {
                output.add(department);
            }
        }

        return output;
    }

    public DepartmentDto getByIndex(
            @NonNull String userToken, @NonNull String universityCode,
            @NonNull String departmentCode
    ) {
        checkDeveloperPermission(userToken);
        return departmentDao.getDepartmentByUniversityCodeAndDepartmentCodeData(universityCode, departmentCode);
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
