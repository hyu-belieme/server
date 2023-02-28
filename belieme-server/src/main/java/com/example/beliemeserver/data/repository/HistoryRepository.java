package com.example.beliemeserver.data.repository;

import com.example.beliemeserver.data.entity.HistoryEntity;
import com.example.beliemeserver.data.repository.custom.RefreshRepository;

import java.util.List;
import java.util.Optional;

public interface HistoryRepository extends RefreshRepository<HistoryEntity, Integer> {
    List<HistoryEntity> findByItemId(int itemId);

    List<HistoryEntity> findByRequesterId(int requesterId);

    Optional<HistoryEntity> findByItemIdAndNum(int itemId, int num);

    boolean existsByItemIdAndNum(int itemId, int num);
}
