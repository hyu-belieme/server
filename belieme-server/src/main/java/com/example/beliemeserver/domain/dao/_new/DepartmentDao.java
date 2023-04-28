package com.example.beliemeserver.domain.dao._new;

import com.example.beliemeserver.domain.dto._new.DepartmentDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DepartmentDao {
    @Transactional
    List<DepartmentDto> getAllList();

    @Transactional
    List<DepartmentDto> getListByUniversity(String universityName);

    @Transactional
    DepartmentDto getByIndex(String universityName, String departmentName);

    @Transactional
    boolean checkExistByIndex(String universityName, String departmentName);

    @Transactional
    DepartmentDto create(DepartmentDto newDepartment);

    @Transactional
    DepartmentDto update(String universityName,
                         String departmentName, DepartmentDto newDepartment);
}
