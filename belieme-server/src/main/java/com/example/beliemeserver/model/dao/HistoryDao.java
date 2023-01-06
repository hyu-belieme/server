package com.example.beliemeserver.model.dao;

import com.example.beliemeserver.model.dto.HistoryDto;
import com.example.beliemeserver.model.exception.ConflictException;
import com.example.beliemeserver.model.exception.DataException;
import com.example.beliemeserver.model.exception.NotFoundException;

import java.util.List;

public interface HistoryDao {
    List<HistoryDto> getListByDepartment(
            String universityCode, String departmentCode)
            throws DataException;

    List<HistoryDto> getListByDepartmentAndRequester(
            String universityCodeForDepartment, String departmentCode,
            String universityCodeForUser, String requesterStudentId)
            throws DataException;

    HistoryDto getByIndex(
            String universityCode, String departmentCode,
            String stuffName, int itemNum, int historyNum)
            throws NotFoundException, DataException;

    HistoryDto create(HistoryDto newHistory)
            throws ConflictException, NotFoundException, DataException;

    HistoryDto update(
            String universityCode, String departmentCode, String stuffName,
            int itemNum, int historyNum, HistoryDto newHistory)
            throws NotFoundException, DataException;
}
