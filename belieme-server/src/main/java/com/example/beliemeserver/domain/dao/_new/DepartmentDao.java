package com.example.beliemeserver.domain.dao._new;

import com.example.beliemeserver.domain.dto._new.DepartmentDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface DepartmentDao {
    @Transactional
    List<DepartmentDto> getAllList();

    @Transactional
    List<DepartmentDto> getListByUniversity(String universityName);

    @Transactional
    List<DepartmentDto> getListByUniversity(UUID universityId);

    @Transactional
    DepartmentDto getById(UUID departmentId);

    @Transactional
    DepartmentDto getByIndex(String universityName, String departmentName);

    @Transactional
    boolean checkExistByIndex(String universityName, String departmentName);

    @Transactional
    DepartmentDto create(DepartmentDto newDepartment);

    @Transactional
    DepartmentDto update(String universityName,
                         String departmentName, DepartmentDto newDepartment);


    @Transactional
    DepartmentDto update(UUID departmentId, DepartmentDto newDepartment);
}
