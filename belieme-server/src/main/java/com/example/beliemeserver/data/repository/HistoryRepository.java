package com.example.beliemeserver.data.repository;

import com.example.beliemeserver.data.entity.HistoryEntity;
import com.example.beliemeserver.data.repository.custom.RefreshRepository;

import java.util.Optional;

public interface HistoryRepository extends RefreshRepository<HistoryEntity, Integer> {
    Optional<HistoryEntity> findByItemIdAndNum(int itemId, int num);
}
