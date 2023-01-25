package com.example.beliemeserver.model.service;

import com.example.beliemeserver.exception.InvalidIndexException;
import com.example.beliemeserver.exception.NotFoundException;
import com.example.beliemeserver.model.dao.*;
import com.example.beliemeserver.model.dto.*;
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
        checkDeveloperPermission(userToken);

        UniversityDto university = getUniversityOrThrowInvalidIndexException(universityCode);
        List<MajorDto> baseMajors = new ArrayList<>();
        for(String majorCode : majorCodes) {
            baseMajors.add(getMajorOrCreate(university, majorCode));
        }

        DepartmentDto newDepartment = new DepartmentDto(university, departmentCode, name, baseMajors);
        return departmentDao.addDepartmentData(newDepartment);
    }

    public DepartmentDto update(
            @NonNull String userToken, @NonNull String universityCode, @NonNull String departmentCode,
            String newDepartmentCode, String newName, List<String> newMajorCodes
    ) {
        checkDeveloperPermission(userToken);

        UniversityDto university = getUniversityOrThrowInvalidIndexException(universityCode);
        DepartmentDto oldDepartment = departmentDao.getDepartmentByUniversityCodeAndDepartmentCodeData(universityCode, departmentCode);

        if(newDepartmentCode == null && newName == null && newMajorCodes == null) return oldDepartment;

        if(newDepartmentCode == null) newDepartmentCode = oldDepartment.code();
        if(newName == null) newName = oldDepartment.name();
        List<MajorDto> newBaseMajors = oldDepartment.baseMajors();

        if(newMajorCodes != null) {
            newBaseMajors = new ArrayList<>();
            for(String majorCode : newMajorCodes) {
                newBaseMajors.add(getMajorOrCreate(university, majorCode));
            }
        }

        DepartmentDto newDepartment = new DepartmentDto(university, newDepartmentCode, newName, newBaseMajors);
        return departmentDao.updateDepartmentData(universityCode, departmentCode, newDepartment);
    }

    private UniversityDto getUniversityOrThrowInvalidIndexException(String universityCode) {
        try {
            return universityDao.getUniversityByCodeData(universityCode);
        } catch (NotFoundException e) {
            throw new InvalidIndexException();
        }
    }

    private MajorDto getMajorOrCreate(UniversityDto university, String majorCode) {
        try {
            return majorDao.getMajorByIndex(university.code(), majorCode);
        } catch (NotFoundException e) {
            return majorDao.addMajorData(new MajorDto(university, majorCode));
        }
    }
}
