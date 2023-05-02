package com.example.beliemeserver.domain.service._new;

import com.example.beliemeserver.config.initdata._new.InitialData;
import com.example.beliemeserver.domain.dao._new.*;
import com.example.beliemeserver.domain.dto._new.*;
import com.example.beliemeserver.domain.dto.enumeration.Permission;
import com.example.beliemeserver.domain.exception.IndexInvalidException;
import com.example.beliemeserver.error.exception.NotFoundException;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NewDepartmentService extends NewBaseService {
    public NewDepartmentService(InitialData initialData, UniversityDao universityDao, DepartmentDao departmentDao, UserDao userDao, MajorDao majorDao, AuthorityDao authorityDao, StuffDao stuffDao, ItemDao itemDao, HistoryDao historyDao) {
        super(initialData, universityDao, departmentDao, userDao, majorDao, authorityDao, stuffDao, itemDao, historyDao);
    }

    public void initializeDepartments() {
        for (DepartmentDto department : initialData.departments().values()) {
            if (departmentDao.checkExistById(department.id())) {
                departmentDao.update(department.id(), department);
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
            @NonNull String userToken, @NonNull String universityName, @NonNull String departmentName
    ) {
        checkDeveloperPermission(userToken);
        return departmentDao.getByIndex(universityName, departmentName);
    }

    public DepartmentDto create(
            @NonNull String userToken, @NonNull String universityName,
            @NonNull String departmentName, @NonNull List<String> majorCodes
    ) {
        checkDeveloperPermission(userToken);

        UniversityDto university = getUniversityOrThrowInvalidIndexException(universityName);
        List<MajorDto> baseMajors = new ArrayList<>();
        for (String majorCode : majorCodes) {
            baseMajors.add(getMajorOrCreate(university, majorCode));
        }

        DepartmentDto newDepartment = DepartmentDto.init(university, departmentName, baseMajors);
        newDepartment = departmentDao.create(newDepartment);
        createOrUpdateAuthorities(newDepartment);
        return newDepartment;
    }

    public DepartmentDto update(
            @NonNull String userToken, @NonNull String universityName, @NonNull String departmentName,
            String newDepartmentName, List<String> newMajorCodes
    ) {
        checkDeveloperPermission(userToken);

        UniversityDto university = getUniversityOrThrowInvalidIndexException(universityName);
        DepartmentDto oldDepartment = departmentDao.getByIndex(universityName, departmentName);

        if (newDepartmentName == null && newMajorCodes == null) return oldDepartment;

        if (newDepartmentName == null) newDepartmentName = oldDepartment.name();
        List<MajorDto> newBaseMajors = oldDepartment.baseMajors();

        if (newMajorCodes != null) {
            newBaseMajors = new ArrayList<>();
            for (String majorCode : newMajorCodes) {
                newBaseMajors.add(getMajorOrCreate(university, majorCode));
            }
        }

        DepartmentDto newDepartment = new DepartmentDto(oldDepartment.id(), university, newDepartmentName, newBaseMajors);
        return departmentDao.update(universityName, departmentName, newDepartment);
    }

    private UniversityDto getUniversityOrThrowInvalidIndexException(String universityName) {
        try {
            return universityDao.getByIndex(universityName);
        } catch (NotFoundException e) {
            throw new IndexInvalidException();
        }
    }

    private MajorDto getMajorOrCreate(UniversityDto university, String majorCode) {
        try {
            return majorDao.getByIndex(university.name(), majorCode);
        } catch (NotFoundException e) {
            return majorDao.create(MajorDto.init(university, majorCode));
        }
    }

    private void createOrUpdateAuthorities(DepartmentDto department) {
        for (Permission permission : Permission.values()) {
            if (authorityDao.checkExistByIndex(department.id(), permission)) continue;
            authorityDao.create(new AuthorityDto(department, permission));
        }
    }
}
