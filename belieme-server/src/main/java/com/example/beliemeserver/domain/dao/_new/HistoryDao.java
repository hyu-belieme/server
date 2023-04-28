package com.example.beliemeserver.domain.dao._new;

import com.example.beliemeserver.domain.dto._new.HistoryDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface HistoryDao {
    @Transactional
    List<HistoryDto> getAllList();

    @Transactional
    List<HistoryDto> getListByDepartment(String universityName, String departmentName);

    @Transactional
    List<HistoryDto> getListByStuff(String universityName,
                                    String departmentName, String stuffName);

    @Transactional
    List<HistoryDto> getListByItem(String universityName, String departmentName,
                                   String stuffName, int itemNum);

    @Transactional
    List<HistoryDto> getListByDepartmentAndRequester(
            String universityNameForDepartment, String departmentName,
            String universityNameForUser, String requesterStudentId);

    @Transactional
    HistoryDto getByIndex(String universityName, String departmentName,
                          String stuffName, int itemNum, int historyNum);

    @Transactional
    HistoryDto create(HistoryDto newHistory);

    @Transactional
    HistoryDto update(String universityName, String departmentName,
                      String stuffName, int itemNum,
                      int historyNum, HistoryDto newHistory);
}
