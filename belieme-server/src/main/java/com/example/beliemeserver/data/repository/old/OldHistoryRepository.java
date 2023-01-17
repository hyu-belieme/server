package com.example.beliemeserver.data.repository.old;

import com.example.beliemeserver.data.entity.old.OldHistoryEntity;
import com.example.beliemeserver.data.entity.id.OldHistoryId;
import com.example.beliemeserver.data.repository.custom.RefreshRepository;

import java.util.List;

public interface OldHistoryRepository extends RefreshRepository<OldHistoryEntity, OldHistoryId> {
    List<OldHistoryEntity> findByRequesterId(String requesterId);
}
