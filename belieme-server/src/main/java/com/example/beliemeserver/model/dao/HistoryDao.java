package com.example.beliemeserver.model.dao;

import com.example.beliemeserver.model.dto.HistoryDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface HistoryDao {
    @Transactional
    List<HistoryDto> getAllList();

    @Transactional
    List<HistoryDto> getListByDepartment(String universityCode, String departmentCode);

    @Transactional
    List<HistoryDto> getListByStuff(String universityCode,
                                    String departmentCode, String stuffName);

    @Transactional
    List<HistoryDto> getListByItem(String universityCode, String departmentCode,
                                   String stuffName, int itemNum);

    @Transactional
    List<HistoryDto> getListByDepartmentAndRequester(
            String universityCodeForDepartment, String departmentCode,
            String universityCodeForUser, String requesterStudentId);

    @Transactional
    HistoryDto getByIndex(String universityCode, String departmentCode,
                          String stuffName, int itemNum, int historyNum);

    @Transactional
    HistoryDto create(HistoryDto newHistory);

    @Transactional
    HistoryDto update(String universityCode, String departmentCode,
                      String stuffName, int itemNum,
                      int historyNum, HistoryDto newHistory);
}
