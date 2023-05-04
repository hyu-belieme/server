package com.example.beliemeserver.data.daoimpl._new;

import com.example.beliemeserver.data.entity._new.NewHistoryEntity;
import com.example.beliemeserver.data.entity._new.NewItemEntity;
import com.example.beliemeserver.data.entity._new.NewStuffEntity;
import com.example.beliemeserver.data.repository._new.*;
import com.example.beliemeserver.domain.dao._new.ItemDao;
import com.example.beliemeserver.domain.dto._new.ItemDto;
import com.example.beliemeserver.error.exception.ConflictException;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class NewItemDaoImpl extends NewBaseDaoImpl implements ItemDao {
    public NewItemDaoImpl(NewUniversityRepository universityRepository, NewDepartmentRepository departmentRepository, NewUserRepository userRepository, NewMajorRepository majorRepository, NewMajorDepartmentJoinRepository majorDepartmentJoinRepository, NewAuthorityRepository authorityRepository, NewAuthorityUserJoinRepository authorityUserJoinRepository, NewStuffRepository stuffRepository, NewItemRepository itemRepository, NewHistoryRepository historyRepository) {
        super(universityRepository, departmentRepository, userRepository, majorRepository, majorDepartmentJoinRepository, authorityRepository, authorityUserJoinRepository, stuffRepository, itemRepository, historyRepository);
    }

    @Override
    public List<ItemDto> getAllList() {
        List<ItemDto> output = new ArrayList<>();
        for (NewItemEntity itemEntity : itemRepository.findAll()) {
            output.add(itemEntity.toItemDto());
        }
        return output;
    }

    @Override
    public List<ItemDto> getListByStuff(@NonNull UUID stuffId) {
        List<ItemDto> output = new ArrayList<>();
        for (NewItemEntity itemEntity : itemRepository.findByStuffId(stuffId)) {
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
        NewStuffEntity stuffOfNewItem = getStuffEntityOrThrowInvalidIndexException(stuffId);

        checkItemIdConflict(itemId);
        checkItemConflict(stuffOfNewItem.getId(), num);

        NewItemEntity newItemEntity = new NewItemEntity(
                itemId,
                stuffOfNewItem,
                num,
                null
        );
        return itemRepository.save(newItemEntity).toItemDto();
    }

    @Override
    public ItemDto update(@NonNull UUID itemId, @NonNull UUID stuffId, int num, UUID lastHistoryId) {
        NewItemEntity target = findItemEntity(itemId);
        NewStuffEntity stuffOfNewItem = getStuffEntityOrThrowInvalidIndexException(stuffId);
        NewHistoryEntity lastHistoryOfNewItem = getHistoryEntityIfIdIsNotNull(lastHistoryId);

        if (doesIndexChange(target, stuffId, num)) {
            checkItemConflict(stuffId, num);
        }

        target = target.withStuff(stuffOfNewItem)
                .withNum(num)
                .withLastHistory(lastHistoryOfNewItem);
        return itemRepository.save(target).toItemDto();
    }

    private NewHistoryEntity getHistoryEntityIfIdIsNotNull(UUID historyId) {
        if (historyId == null) {
            return null;
        }
        return getHistoryEntityOrThrowInvalidIndexException(historyId);
    }

    private boolean doesIndexChange(NewItemEntity target, UUID stuffId, int num) {
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
