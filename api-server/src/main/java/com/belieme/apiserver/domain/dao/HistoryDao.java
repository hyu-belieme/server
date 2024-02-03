package com.belieme.apiserver.domain.dao;

import com.belieme.apiserver.domain.dto.HistoryCursorDto;
import com.belieme.apiserver.domain.dto.HistoryDto;
import com.belieme.apiserver.domain.dto.enumeration.HistoryStatus;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;

public interface HistoryDao {

  @Transactional
  List<HistoryDto> getAllList();

  @Transactional
  List<HistoryDto> getListByDepartment(@NonNull UUID departmentId, HistoryStatus status);

  @Transactional
  List<HistoryDto> getListByDepartment(@NonNull UUID departmentId,
      HistoryStatus status, HistoryCursorDto cursor, int limit);

  @Transactional
  List<HistoryDto> getListByDepartmentAndRequester(@NonNull UUID departmentId,
      @NonNull UUID requesterId, HistoryStatus status);

  @Transactional
  List<HistoryDto> getListByDepartmentAndRequester(@NonNull UUID departmentId,
      @NonNull UUID requesterId, HistoryStatus status, HistoryCursorDto cursor, int limit);

  @Transactional
  HistoryDto getById(@NonNull UUID historyId);

  @Transactional
  HistoryDto create(@NonNull UUID historyId, @NonNull UUID itemId, int num, UUID requesterId,
      UUID approveManagerId, UUID returnManagerId, UUID lostManagerId, UUID cancelManagerId,
      long requestedAt, long approvedAt, long returnedAt, long lostAt, long canceledAt);

  @Transactional
  HistoryDto update(@NonNull UUID historyId, @NonNull UUID itemId, int num, UUID requesterId,
      UUID approveManagerId, UUID returnManagerId, UUID lostManagerId, UUID cancelManagerId,
      long requestedAt, long approvedAt, long returnedAt, long lostAt, long canceledAt);
}
