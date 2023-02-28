package com.example.beliemeserver.data.repository;

import com.example.beliemeserver.data.entity.ItemEntity;
import com.example.beliemeserver.data.repository.custom.RefreshRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends RefreshRepository<ItemEntity, Integer> {
    List<ItemEntity> findByStuffId(int stuffId);

    Optional<ItemEntity> findByStuffIdAndNum(int stuffId, int num);

    boolean existsByStuffIdAndNum(int stuffId, int num);
}
