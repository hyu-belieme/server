package com.example.beliemeserver.model.dao;

import com.example.beliemeserver.model.dto.DepartmentDto;
import com.example.beliemeserver.model.dto.MajorDto;
import com.example.beliemeserver.model.exception.ConflictException;
import com.example.beliemeserver.model.exception.DataException;
import com.example.beliemeserver.model.exception.NotFoundException;

import java.util.List;

public interface DepartmentDao {
    List<DepartmentDto> getAllDepartmentsData() throws DataException;

    List<DepartmentDto> getAllDepartmentsByUniversityCodeData(
            String universityCode
    ) throws DataException, NotFoundException;

    DepartmentDto getDepartmentByUniversityCodeAndDepartmentCodeData(
            String universityCode,
            String departmentCode
    ) throws DataException, NotFoundException;

    DepartmentDto addDepartmentData(
            DepartmentDto newDepartment
    ) throws DataException, NotFoundException, ConflictException;

    DepartmentDto updateDepartmentData(
            String universityCode,
            String departmentCode,
            DepartmentDto newDepartment
    ) throws DataException, NotFoundException;

    DepartmentDto putBaseMajorOnDepartmentData(
            String universityCode,
            String departmentCode,
            MajorDto newBaseMajor
    ) throws NotFoundException;

    DepartmentDto removeBaseMajorOnDepartmentData(
            String universityCode,
            String departmentCode,
            MajorDto targetBaseMajor
    ) throws NotFoundException;
}
