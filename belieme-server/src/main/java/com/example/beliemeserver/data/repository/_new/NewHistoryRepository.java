package com.example.beliemeserver.data.repository._new;

import com.example.beliemeserver.data.entity._new.NewHistoryEntity;
import com.example.beliemeserver.data.repository.custom.RefreshRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NewHistoryRepository extends RefreshRepository<NewHistoryEntity, UUID> {
    List<NewHistoryEntity> findByItemId(UUID itemId);

    List<NewHistoryEntity> findByRequesterId(UUID requesterId);

    Optional<NewHistoryEntity> findByItemIdAndNum(UUID itemId, int num);

    boolean existsByItemIdAndNum(UUID itemId, int num);
}
