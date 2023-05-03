package com.example.beliemeserver.domain.dao._new;

import com.example.beliemeserver.domain.dto._new.HistoryDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface HistoryDao {
    @Transactional
    List<HistoryDto> getAllList();

    @Transactional
    List<HistoryDto> getListByDepartment(UUID departmentId);

    @Transactional
    List<HistoryDto> getListByStuff(UUID stuffId);

    @Transactional
    List<HistoryDto> getListByItem(UUID itemId);

    @Transactional
    List<HistoryDto> getListByDepartmentAndRequester(
            UUID departmentId, UUID requesterId);

    @Transactional
    HistoryDto getById(UUID historyId);

    @Transactional
    HistoryDto create(HistoryDto newHistory);

    @Transactional
    HistoryDto update(UUID historyId, HistoryDto newHistory);
}
