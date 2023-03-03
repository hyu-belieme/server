package com.example.beliemeserver.model.dao;

import com.example.beliemeserver.exception.ConflictException;
import com.example.beliemeserver.exception.NotFoundException;
import com.example.beliemeserver.model.dto.DepartmentDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DepartmentDao {
    @Transactional
    List<DepartmentDto> getAllList();

    @Transactional
    List<DepartmentDto> getListByUniversity(
            String universityCode
    ) throws NotFoundException;

    @Transactional
    DepartmentDto getByIndex(
            String universityCode,
            String departmentCode
    ) throws NotFoundException;

    @Transactional
    boolean checkExistByIndex(String universityCode, String departmentCode);

    @Transactional
    DepartmentDto create(
            DepartmentDto newDepartment
    ) throws NotFoundException, ConflictException;

    @Transactional
    DepartmentDto update(
            String universityCode,
            String departmentCode,
            DepartmentDto newDepartment
    ) throws NotFoundException, ConflictException;
}
