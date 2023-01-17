package com.example.beliemeserver.model.dao;

import com.example.beliemeserver.model.dto.StuffDto;
import com.example.beliemeserver.model.exception.ConflictException;
import com.example.beliemeserver.model.exception.DataException;
import com.example.beliemeserver.model.exception.NotFoundException;

import java.util.List;

public interface StuffDao {
    List<StuffDto> getAllList()  throws DataException;

    List<StuffDto> getListByDepartment(String universityCode, String departmentCode)
            throws DataException, NotFoundException;

    StuffDto getByIndex(
            String universityCode, String departmentCode, String stuffName)
            throws NotFoundException, DataException;

    StuffDto create(StuffDto newStuff)
            throws ConflictException, NotFoundException, DataException;

    StuffDto update(
            String universityCode, String departmentCode,
            String stuffName, StuffDto newStuff)
            throws NotFoundException, DataException, ConflictException;
}
