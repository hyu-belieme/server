package com.example.beliemeserver.domain.dao;

import com.example.beliemeserver.domain.dto.DepartmentDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DepartmentDao {
    @Transactional
    List<DepartmentDto> getAllList();

    @Transactional
    List<DepartmentDto> getListByUniversity(String universityCode);

    @Transactional
    DepartmentDto getByIndex(String universityCode, String departmentCode);

    @Transactional
    boolean checkExistByIndex(String universityCode, String departmentCode);

    @Transactional
    DepartmentDto create(DepartmentDto newDepartment);

    @Transactional
    DepartmentDto update(String universityCode,
                         String departmentCode, DepartmentDto newDepartment);
}
