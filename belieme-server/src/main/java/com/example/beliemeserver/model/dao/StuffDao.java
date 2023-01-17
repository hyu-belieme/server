package com.example.beliemeserver.model.dao;

import com.example.beliemeserver.model.dto.StuffDto;
import com.example.beliemeserver.model.exception.ConflictException;
import com.example.beliemeserver.model.exception.DataException;
import com.example.beliemeserver.model.exception.NotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface StuffDao {
    @Transactional
    List<StuffDto> getAllList()  throws DataException;

    @Transactional
    List<StuffDto> getListByDepartment(String universityCode, String departmentCode)
            throws DataException, NotFoundException;

    @Transactional
    StuffDto getByIndex(
            String universityCode, String departmentCode, String stuffName)
            throws NotFoundException, DataException;

    @Transactional
    StuffDto create(StuffDto newStuff)
            throws ConflictException, NotFoundException, DataException;

    @Transactional
    StuffDto update(
            String universityCode, String departmentCode,
            String stuffName, StuffDto newStuff)
            throws NotFoundException, DataException, ConflictException;
}
