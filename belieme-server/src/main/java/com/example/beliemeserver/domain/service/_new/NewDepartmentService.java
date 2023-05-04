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
import java.util.UUID;

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

    public DepartmentDto getById(
            @NonNull String userToken, @NonNull UUID departmentId
    ) {
        UserDto requester = validateTokenAndGetUser(userToken);
        checkDeveloperPermission(requester);

        return departmentDao.getById(departmentId);
    }

    public DepartmentDto create(
            @NonNull String userToken, @NonNull UUID universityId,
            @NonNull String departmentName, @NonNull List<String> majorCodes
    ) {
        UserDto requester = validateTokenAndGetUser(userToken);
        checkDeveloperPermission(requester);

        UniversityDto university = getUniversityOrThrowInvalidIndexException(universityId);
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
            @NonNull String userToken, @NonNull UUID departmentId,
            String newDepartmentName, List<String> newMajorCodes
    ) {
        UserDto requester = validateTokenAndGetUser(userToken);
        checkDeveloperPermission(requester);

        DepartmentDto oldDepartment = departmentDao.getById(departmentId);

        if (newDepartmentName == null && newMajorCodes == null) return oldDepartment;

        if (newDepartmentName == null) newDepartmentName = oldDepartment.name();

        List<MajorDto> newBaseMajors = oldDepartment.baseMajors();
        if (newMajorCodes != null) {
            newBaseMajors = new ArrayList<>();
            for (String majorCode : newMajorCodes) {
                newBaseMajors.add(getMajorOrCreate(oldDepartment.university(), majorCode));
            }
        }

        DepartmentDto newDepartment = new DepartmentDto(departmentId, oldDepartment.university(), newDepartmentName, newBaseMajors);
        return departmentDao.update(departmentId, newDepartment);
    }

    private UniversityDto getUniversityOrThrowInvalidIndexException(UUID universityId) {
        try {
            return universityDao.getById(universityId);
        } catch (NotFoundException e) {
            throw new IndexInvalidException();
        }
    }

    private MajorDto getMajorOrCreate(UniversityDto university, String majorCode) {
        try {
            return majorDao.getByIndex(university.id(), majorCode);
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
