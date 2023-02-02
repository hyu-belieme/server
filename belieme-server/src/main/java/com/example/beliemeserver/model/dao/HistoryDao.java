package com.example.beliemeserver.model.dao;

import com.example.beliemeserver.model.dto.HistoryDto;
import com.example.beliemeserver.exception.ConflictException;
import com.example.beliemeserver.exception.FormatDoesNotMatchException;
import com.example.beliemeserver.exception.NotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface HistoryDao {
    @Transactional
    List<HistoryDto> getAllList() throws FormatDoesNotMatchException;

    @Transactional
    List<HistoryDto> getListByDepartment(
            String universityCode, String departmentCode)
            throws NotFoundException, FormatDoesNotMatchException;

    @Transactional
    List<HistoryDto> getListByStuff(
            String universityCode, String departmentCode, String stuffName)
            throws NotFoundException, FormatDoesNotMatchException;

    @Transactional
    List<HistoryDto> getListByItem(
            String universityCode, String departmentCode,
            String stuffName, int itemNum)
            throws NotFoundException, FormatDoesNotMatchException;

    @Transactional
    List<HistoryDto> getListByDepartmentAndRequester(
            String universityCodeForDepartment, String departmentCode,
            String universityCodeForUser, String requesterStudentId)
            throws NotFoundException, FormatDoesNotMatchException;

    @Transactional
    HistoryDto getByIndex(
            String universityCode, String departmentCode,
            String stuffName, int itemNum, int historyNum)
            throws NotFoundException, FormatDoesNotMatchException;

    @Transactional
    HistoryDto create(HistoryDto newHistory)
            throws ConflictException, NotFoundException, FormatDoesNotMatchException;

    @Transactional
    HistoryDto update(
            String universityCode, String departmentCode, String stuffName,
            int itemNum, int historyNum, HistoryDto newHistory)
            throws NotFoundException, ConflictException, FormatDoesNotMatchException;
}
