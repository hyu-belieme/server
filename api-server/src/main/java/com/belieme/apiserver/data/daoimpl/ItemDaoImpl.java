package com.belieme.apiserver.data.daoimpl;

import com.belieme.apiserver.data.entity.HistoryEntity;
import com.belieme.apiserver.data.entity.ItemEntity;
import com.belieme.apiserver.data.entity.StuffEntity;
import com.belieme.apiserver.data.repository.AuthorityRepository;
import com.belieme.apiserver.data.repository.AuthorityUserJoinRepository;
import com.belieme.apiserver.data.repository.DepartmentRepository;
import com.belieme.apiserver.data.repository.HistoryRepository;
import com.belieme.apiserver.data.repository.ItemRepository;
import com.belieme.apiserver.data.repository.MajorDepartmentJoinRepository;
import com.belieme.apiserver.data.repository.MajorRepository;
import com.belieme.apiserver.data.repository.StuffRepository;
import com.belieme.apiserver.data.repository.UniversityRepository;
import com.belieme.apiserver.data.repository.UserRepository;
import com.belieme.apiserver.domain.dao.ItemDao;
import com.belieme.apiserver.domain.dto.ItemDto;
import com.belieme.apiserver.error.exception.ConflictException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ItemDaoImpl extends BaseDaoImpl implements ItemDao {

  public ItemDaoImpl(UniversityRepository universityRepository,
      DepartmentRepository departmentRepository, UserRepository userRepository,
      MajorRepository majorRepository, MajorDepartmentJoinRepository majorDepartmentJoinRepository,
      AuthorityRepository authorityRepository,
      AuthorityUserJoinRepository authorityUserJoinRepository, StuffRepository stuffRepository,
      ItemRepository itemRepository, HistoryRepository historyRepository) {
    super(universityRepository, departmentRepository, userRepository, majorRepository,
        majorDepartmentJoinRepository, authorityRepository, authorityUserJoinRepository,
        stuffRepository, itemRepository, historyRepository);
  }

  @Override
  public List<ItemDto> getAllList() {
    List<ItemDto> output = new ArrayList<>();
    for (ItemEntity itemEntity : itemRepository.findAll()) {
      output.add(itemEntity.toItemDto());
    }
    return output;
  }

  @Override
  public List<ItemDto> getListByStuff(@NonNull UUID stuffId) {
    validateStuffId(stuffId);

    List<ItemDto> output = new ArrayList<>();
    for (ItemEntity itemEntity : itemRepository.findByStuffId(stuffId)) {
      output.add(itemEntity.toItemDto());
    }
    return output;
  }

  @Override
  public ItemDto getById(@NonNull UUID itemId) {
    return findItemEntity(itemId).toItemDto();
  }

  @Override
  public ItemDto create(@NonNull UUID itemId, @NonNull UUID stuffId, int num) {
    StuffEntity stuffOfNewItem = getStuffEntityOrThrowInvalidIndexException(stuffId);

    checkItemIdConflict(itemId);
    checkItemConflict(stuffOfNewItem.getId(), num);

    ItemEntity newItemEntity = new ItemEntity(itemId, stuffOfNewItem, num, null);
    return itemRepository.save(newItemEntity).toItemDto();
  }

  @Override
  public ItemDto update(@NonNull UUID itemId, @NonNull UUID stuffId, int num, UUID lastHistoryId) {
    ItemEntity target = findItemEntity(itemId);
    StuffEntity stuffOfNewItem = getStuffEntityOrThrowInvalidIndexException(stuffId);
    HistoryEntity lastHistoryOfNewItem = getHistoryEntityIfIdIsNotNull(lastHistoryId);

    if (doesIndexChange(target, stuffId, num)) {
      checkItemConflict(stuffId, num);
    }

    target = target.withStuff(stuffOfNewItem).withNum(num).withLastHistory(lastHistoryOfNewItem);
    return itemRepository.save(target).toItemDto();
  }

  private HistoryEntity getHistoryEntityIfIdIsNotNull(UUID historyId) {
    if (historyId == null) {
      return null;
    }
    return getHistoryEntityOrThrowInvalidIndexException(historyId);
  }

  private boolean doesIndexChange(ItemEntity target, UUID stuffId, int num) {
    return !(target.getStuffId().equals(stuffId) && target.getNum() == num);
  }

  private void checkItemIdConflict(UUID itemId) {
    if (itemRepository.existsById(itemId)) {
      throw new ConflictException();
    }
  }

  private void checkItemConflict(UUID stuffId, int num) {
    if (itemRepository.existsByStuffIdAndNum(stuffId, num)) {
      throw new ConflictException();
    }
  }
}
