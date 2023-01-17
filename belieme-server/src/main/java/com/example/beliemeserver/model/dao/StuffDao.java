package com.example.beliemeserver.model.dao;

import com.example.beliemeserver.model.dto.StuffDto;
import com.example.beliemeserver.exception.ConflictException;
import com.example.beliemeserver.exception.FormatDoesNotMatchException;
import com.example.beliemeserver.exception.NotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface StuffDao {
    @Transactional
    List<StuffDto> getAllList() throws FormatDoesNotMatchException;

    @Transactional
    List<StuffDto> getListByDepartment(String universityCode, String departmentCode)
            throws NotFoundException, FormatDoesNotMatchException;

    @Transactional
    StuffDto getByIndex(
            String universityCode, String departmentCode, String stuffName)
            throws NotFoundException, FormatDoesNotMatchException;

    @Transactional
    StuffDto create(StuffDto newStuff)
            throws ConflictException, NotFoundException, FormatDoesNotMatchException;

    @Transactional
    StuffDto update(
            String universityCode, String departmentCode,
            String stuffName, StuffDto newStuff)
            throws NotFoundException, ConflictException, FormatDoesNotMatchException;
}
