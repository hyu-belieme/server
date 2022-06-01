package com.example.beliemeserver.data.repository;

import com.example.beliemeserver.data.entity.ItemEntity;
import com.example.beliemeserver.data.entity.id.ItemId;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ItemRepository extends CrudRepository<ItemEntity, ItemId> {
    List<ItemEntity> findByStuffId(int stuffId);
}
