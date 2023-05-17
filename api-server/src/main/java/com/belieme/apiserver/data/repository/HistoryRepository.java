package com.belieme.apiserver.data.repository;

import com.belieme.apiserver.data.entity.HistoryEntity;
import com.belieme.apiserver.data.repository.custom.RefreshRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HistoryRepository extends RefreshRepository<HistoryEntity, UUID> {
    List<HistoryEntity> findByItemId(UUID itemId);

    List<HistoryEntity> findByRequesterId(UUID requesterId);

    Optional<HistoryEntity> findByItemIdAndNum(UUID itemId, int num);

    boolean existsByItemIdAndNum(UUID itemId, int num);
}
