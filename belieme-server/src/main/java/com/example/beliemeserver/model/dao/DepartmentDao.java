package com.example.beliemeserver.model.dao;

import com.example.beliemeserver.model.dto.DepartmentDto;
import com.example.beliemeserver.model.exception.DataException;

import java.util.List;

public interface DepartmentDao {
    List<DepartmentDto> getAllDepartmentsData() throws DataException;

    List<DepartmentDto> getAllDepartmentsByUniversityCodeData(
            String universityCode
    ) throws DataException;

    DepartmentDto getDepartmentByUniversityCodeAndDepartmentCodeData(
            String universityCode,
            String departmentCode
    ) throws DataException;

    DepartmentDto addDepartmentData(
            DepartmentDto newDepartment
    ) throws DataException;

    DepartmentDto updateDepartmentData(
            String universityCode,
            String departmentCode,
            DepartmentDto newDepartment
    ) throws DataException;
}
