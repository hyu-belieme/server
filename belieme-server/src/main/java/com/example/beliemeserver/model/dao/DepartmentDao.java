package com.example.beliemeserver.model.dao;

import com.example.beliemeserver.model.dto.DepartmentDto;
import com.example.beliemeserver.model.dto.MajorDto;
import com.example.beliemeserver.model.exception.ConflictException;
import com.example.beliemeserver.model.exception.DataException;
import com.example.beliemeserver.model.exception.NotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DepartmentDao {
    @Transactional
    List<DepartmentDto> getAllDepartmentsData() throws DataException;

    @Transactional
    List<DepartmentDto> getAllDepartmentsByUniversityCodeData(
            String universityCode
    ) throws DataException, NotFoundException;

    @Transactional
    DepartmentDto getDepartmentByUniversityCodeAndDepartmentCodeData(
            String universityCode,
            String departmentCode
    ) throws DataException, NotFoundException;

    @Transactional
    DepartmentDto addDepartmentData(
            DepartmentDto newDepartment
    ) throws DataException, NotFoundException, ConflictException;

    @Transactional
    DepartmentDto updateDepartmentData(
            String universityCode,
            String departmentCode,
            DepartmentDto newDepartment
    ) throws DataException, NotFoundException, ConflictException;

    @Transactional
    DepartmentDto putBaseMajorOnDepartmentData(
            String universityCode,
            String departmentCode,
            MajorDto newBaseMajor
    ) throws NotFoundException, ConflictException;

    @Transactional
    DepartmentDto removeBaseMajorOnDepartmentData(
            String universityCode,
            String departmentCode,
            MajorDto targetBaseMajor
    ) throws NotFoundException;
}
