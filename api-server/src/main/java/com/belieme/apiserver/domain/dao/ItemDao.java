package com.belieme.apiserver.domain.dao;

import com.belieme.apiserver.domain.dto.ItemDto;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;

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
