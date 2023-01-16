package com.example.beliemeserver.data.repository;

import com.example.beliemeserver.data.entity.ItemEntity;
import com.example.beliemeserver.data.repository.custom.RefreshRepository;

import java.util.Optional;

public interface ItemRepository extends RefreshRepository<ItemEntity, Integer> {
    Optional<ItemEntity> findByStuffIdAndNum(int stuffId, int num);
}
