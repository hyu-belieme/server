package com.belieme.apiserver.data.repository;

import com.belieme.apiserver.data.entity.ItemEntity;
import com.belieme.apiserver.data.repository.custom.RefreshRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ItemRepository extends RefreshRepository<ItemEntity, UUID> {
    List<ItemEntity> findByStuffId(UUID stuffId);

    Optional<ItemEntity> findByStuffIdAndNum(UUID stuffId, int num);

    boolean existsByStuffIdAndNum(UUID stuffId, int num);
}
