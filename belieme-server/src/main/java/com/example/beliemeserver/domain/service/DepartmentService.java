package com.example.beliemeserver.domain.service;

import com.example.beliemeserver.config.initdata.InitialData;
import com.example.beliemeserver.domain.dao.*;
import com.example.beliemeserver.domain.dto.*;
import com.example.beliemeserver.domain.dto.enumeration.Permission;
import com.example.beliemeserver.error.exception.InvalidIndexException;
import com.example.beliemeserver.error.exception.NotFoundException;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentService extends BaseService {
    public DepartmentService(InitialData initialData, UniversityDao universityDao, DepartmentDao departmentDao, UserDao userDao, MajorDao majorDao, AuthorityDao authorityDao, StuffDao stuffDao, ItemDao itemDao, HistoryDao historyDao) {
        super(initialData, universityDao, departmentDao, userDao, majorDao, authorityDao, stuffDao, itemDao, historyDao);
    }

    public void initializeDepartments() {
        for (DepartmentDto department : initialData.departments().values()) {
            if (departmentDao.checkExistByIndex(department.university().code(), department.code())) {
                departmentDao.update(department.university().code(), department.code(), department);
                createOrUpdateAuthorities(department);
                continue;
            }
            departmentDao.create(department);
            createOrUpdateAuthorities(department);
        }
    }

    public List<DepartmentDto> getAccessibleList(@NonNull String userToken) {
        List<DepartmentDto> output = new ArrayList<>();

        UserDto requester = validateTokenAndGetUser(userToken);
        List<DepartmentDto> allDepartment = departmentDao.getAllList();
        for (DepartmentDto department : allDepartment) {
            if (requester.getMaxPermission(department).hasUserPermission()) {
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
        return departmentDao.getByIndex(universityCode, departmentCode);
    }

    public DepartmentDto create(
            @NonNull String userToken, @NonNull String universityCode,
            @NonNull String departmentCode, @NonNull String name,
            @NonNull List<String> majorCodes
    ) {
        checkDeveloperPermission(userToken);

        UniversityDto university = getUniversityOrThrowInvalidIndexException(universityCode);
        List<MajorDto> baseMajors = new ArrayList<>();
        for (String majorCode : majorCodes) {
            baseMajors.add(getMajorOrCreate(university, majorCode));
        }

        DepartmentDto newDepartment = new DepartmentDto(university, departmentCode, name, baseMajors);
        newDepartment = departmentDao.create(newDepartment);
        createOrUpdateAuthorities(newDepartment);
        return newDepartment;
    }

    public DepartmentDto update(
            @NonNull String userToken, @NonNull String universityCode, @NonNull String departmentCode,
            String newDepartmentCode, String newName, List<String> newMajorCodes
    ) {
        checkDeveloperPermission(userToken);

        UniversityDto university = getUniversityOrThrowInvalidIndexException(universityCode);
        DepartmentDto oldDepartment = departmentDao.getByIndex(universityCode, departmentCode);

        if (newDepartmentCode == null && newName == null && newMajorCodes == null) return oldDepartment;

        if (newDepartmentCode == null) newDepartmentCode = oldDepartment.code();
        if (newName == null) newName = oldDepartment.name();
        List<MajorDto> newBaseMajors = oldDepartment.baseMajors();

        if (newMajorCodes != null) {
            newBaseMajors = new ArrayList<>();
            for (String majorCode : newMajorCodes) {
                newBaseMajors.add(getMajorOrCreate(university, majorCode));
            }
        }

        DepartmentDto newDepartment = new DepartmentDto(university, newDepartmentCode, newName, newBaseMajors);
        return departmentDao.update(universityCode, departmentCode, newDepartment);
    }

    private UniversityDto getUniversityOrThrowInvalidIndexException(String universityCode) {
        try {
            return universityDao.getByIndex(universityCode);
        } catch (NotFoundException e) {
            throw new InvalidIndexException();
        }
    }

    private MajorDto getMajorOrCreate(UniversityDto university, String majorCode) {
        try {
            return majorDao.getByIndex(university.code(), majorCode);
        } catch (NotFoundException e) {
            return majorDao.create(new MajorDto(university, majorCode));
        }
    }

    private void createOrUpdateAuthorities(DepartmentDto department) {
        for (Permission permission : Permission.values()) {
            if (!authorityDao.checkExistByIndex(department.university().code(), department.code(), permission)) {
                authorityDao.create(new AuthorityDto(department, permission));
                continue;
            }
            authorityDao.update(department.university().code(), department.code(), permission, new AuthorityDto(department, permission));
        }
    }
}
