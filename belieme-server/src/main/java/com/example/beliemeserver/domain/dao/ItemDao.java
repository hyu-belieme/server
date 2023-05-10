package com.example.beliemeserver.domain.dao;

import com.example.beliemeserver.domain.dto.ItemDto;
import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface ItemDao {
    @Transactional
    List<ItemDto> getAllList();

    @Transactional
    List<ItemDto> getListByStuff(@NonNull UUID stuffId);

    @Transactional
    ItemDto getById(@NonNull UUID itemId);

    @Transactional
    ItemDto create(@NonNull UUID itemId, @NonNull UUID stuffId, int num);

    @Transactional
    ItemDto update(@NonNull UUID itemId, @NonNull UUID stuffId, int num, UUID lastHistoryId);
}
