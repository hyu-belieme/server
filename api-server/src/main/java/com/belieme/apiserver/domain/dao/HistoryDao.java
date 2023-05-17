package com.belieme.apiserver.domain.dao;

import com.belieme.apiserver.domain.dto.HistoryDto;
import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface HistoryDao {
    @Transactional
    List<HistoryDto> getAllList();

    @Transactional
    List<HistoryDto> getListByDepartment(@NonNull UUID departmentId);

    @Transactional
    List<HistoryDto> getListByStuff(@NonNull UUID stuffId);

    @Transactional
    List<HistoryDto> getListByItem(@NonNull UUID itemId);

    @Transactional
    List<HistoryDto> getListByDepartmentAndRequester(
            @NonNull UUID departmentId, @NonNull UUID requesterId);

    @Transactional
    HistoryDto getById(@NonNull UUID historyId);

    @Transactional
    HistoryDto create(
            @NonNull UUID historyId, @NonNull UUID itemId, int num, UUID requesterId,
            UUID approveManagerId, UUID returnManagerId, UUID lostManagerId,
            UUID cancelManagerId, long requestedAt, long approvedAt, long returnedAt,
            long lostAt, long canceledAt
    );

    @Transactional
    HistoryDto update(
            @NonNull UUID historyId, @NonNull UUID itemId, int num, UUID requesterId,
            UUID approveManagerId, UUID returnManagerId, UUID lostManagerId,
            UUID cancelManagerId, long requestedAt, long approvedAt, long returnedAt,
            long lostAt, long canceledAt
    );
}
