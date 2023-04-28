package com.example.beliemeserver.data.repository._new;

import com.example.beliemeserver.data.entity._new.NewItemEntity;
import com.example.beliemeserver.data.repository.custom.RefreshRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NewItemRepository extends RefreshRepository<NewItemEntity, UUID> {
    List<NewItemEntity> findByStuffId(UUID stuffId);

    Optional<NewItemEntity> findByStuffIdAndNum(UUID stuffId, int num);

    boolean existsByStuffIdAndNum(UUID stuffId, int num);
}
