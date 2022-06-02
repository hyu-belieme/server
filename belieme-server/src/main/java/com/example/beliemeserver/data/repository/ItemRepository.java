package com.example.beliemeserver.data.repository;

import com.example.beliemeserver.data.entity.ItemEntity;
import com.example.beliemeserver.data.entity.id.ItemId;
import com.example.beliemeserver.data.repository.custom.RefreshRepository;

import java.util.List;

public interface ItemRepository extends RefreshRepository<ItemEntity, ItemId> {
    List<ItemEntity> findByStuffId(int stuffId);
}
