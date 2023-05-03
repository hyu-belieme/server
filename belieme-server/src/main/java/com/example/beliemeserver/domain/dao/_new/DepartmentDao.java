package com.example.beliemeserver.domain.dao._new;

import com.example.beliemeserver.domain.dto._new.DepartmentDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface DepartmentDao {
    @Transactional
    List<DepartmentDto> getAllList();

    @Transactional
    List<DepartmentDto> getListByUniversity(UUID universityId);

    @Transactional
    DepartmentDto getById(UUID departmentId);

    @Transactional
    boolean checkExistById(UUID id);

    @Transactional
    DepartmentDto create(DepartmentDto newDepartment);

    @Transactional
    DepartmentDto update(UUID departmentId, DepartmentDto newDepartment);
}
