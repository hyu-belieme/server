package com.example.beliemeserver.data.repository.old;

import com.example.beliemeserver.data.entity.old.HistoryEntity;
import com.example.beliemeserver.data.entity.id.HistoryId;
import com.example.beliemeserver.data.repository.custom.RefreshRepository;

import java.util.List;

public interface HistoryRepository extends RefreshRepository<HistoryEntity, HistoryId> {
    List<HistoryEntity> findByRequesterId(String requesterId);
}
