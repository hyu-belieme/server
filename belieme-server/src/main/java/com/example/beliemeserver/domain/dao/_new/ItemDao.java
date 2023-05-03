package com.example.beliemeserver.domain.dao._new;

import com.example.beliemeserver.domain.dto._new.ItemDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface ItemDao {
    @Transactional
    List<ItemDto> getAllList();

    @Transactional
    List<ItemDto> getListByStuff(UUID stuffId);

    @Transactional
    ItemDto getById(UUID itemId);

    @Transactional
    ItemDto create(ItemDto newItem);

    @Transactional
    ItemDto update(UUID itemId, ItemDto newItem);
}
