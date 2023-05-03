package com.example.beliemeserver.data.daoimpl._new;

import com.example.beliemeserver.data.entity._new.NewHistoryEntity;
import com.example.beliemeserver.data.entity._new.NewItemEntity;
import com.example.beliemeserver.data.entity._new.NewStuffEntity;
import com.example.beliemeserver.data.repository._new.*;
import com.example.beliemeserver.domain.dao._new.ItemDao;
import com.example.beliemeserver.domain.dto._new.HistoryDto;
import com.example.beliemeserver.domain.dto._new.ItemDto;
import com.example.beliemeserver.error.exception.ConflictException;
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
    public List<ItemDto> getListByStuff(UUID stuffId) {
        List<ItemDto> output = new ArrayList<>();
        for (NewItemEntity itemEntity : itemRepository.findByStuffId(stuffId)) {
            output.add(itemEntity.toItemDto());
        }
        return output;
    }

    @Override
    public List<ItemDto> getListByStuff(String universityName, String departmentName, String stuffName) {
        NewStuffEntity targetStuff = findStuffEntity(universityName, departmentName, stuffName);
        List<ItemDto> output = new ArrayList<>();
        for (NewItemEntity itemEntity : itemRepository.findByStuffId(targetStuff.getId())) {
            output.add(itemEntity.toItemDto());
        }
        return output;
    }

    @Override
    public ItemDto getById(UUID itemId) {
        return findItemEntity(itemId).toItemDto();
    }

    @Override
    public ItemDto getByIndex(String universityName, String departmentName, String stuffName, int itemNum) {
        return findItemEntity(universityName, departmentName, stuffName, itemNum).toItemDto();
    }

    @Override
    public ItemDto create(ItemDto newItem) {
        NewStuffEntity stuffOfNewItem = findStuffEntity(newItem.stuff().id());

        checkItemConflict(stuffOfNewItem.getId(), newItem.num());

        NewItemEntity newItemEntity = new NewItemEntity(
                newItem.id(),
                stuffOfNewItem,
                newItem.num(),
                null
        );
        return itemRepository.save(newItemEntity).toItemDto();
    }

    @Override
    public ItemDto update(String universityName, String departmentName, String stuffName, int itemNum, ItemDto newItem) {
        NewItemEntity target = findItemEntity(universityName, departmentName, stuffName, itemNum);
        NewStuffEntity stuffOfNewItem = findStuffEntity(newItem.stuff().id());
        NewHistoryEntity lastHistoryOfNewItem = toHistoryEntityOrNull(newItem.lastHistory());

        if (doesIndexChange(target, newItem)) {
            checkItemConflict(stuffOfNewItem.getId(), newItem.num());
        }

        target = target.withStuff(stuffOfNewItem)
                .withNum(newItem.num())
                .withLastHistory(lastHistoryOfNewItem);
        return itemRepository.save(target).toItemDto();
    }

    @Override
    public ItemDto update(UUID itemId, ItemDto newItem) {
        NewItemEntity target = findItemEntity(itemId);
        NewStuffEntity stuffOfNewItem = findStuffEntity(newItem.stuff().id());
        NewHistoryEntity lastHistoryOfNewItem = toHistoryEntityOrNull(newItem.lastHistory());

        if (doesIndexChange(target, newItem)) {
            checkItemConflict(stuffOfNewItem.getId(), newItem.num());
        }

        target = target.withStuff(stuffOfNewItem)
                .withNum(newItem.num())
                .withLastHistory(lastHistoryOfNewItem);
        return itemRepository.save(target).toItemDto();
    }

    private NewHistoryEntity toHistoryEntityOrNull(HistoryDto historyDto) {
        if (historyDto == null) {
            return null;
        }
        return findHistoryEntity(historyDto.id());
    }

    private boolean doesIndexChange(NewItemEntity target, ItemDto newItem) {
        String oldUniversityName = target.getStuff().getDepartment().getUniversity().getName();
        String oldDepartmentName = target.getStuff().getDepartment().getName();
        String oldStuffName = target.getStuff().getName();
        int oldItemNum = target.getNum();

        return !(oldUniversityName.equals(newItem.stuff().department().university().name())
                && oldDepartmentName.equals(newItem.stuff().department().name())
                && oldStuffName.equals(newItem.stuff().name())
                && oldItemNum == newItem.num());
    }

    private void checkItemConflict(UUID stuffId, int num) {
        if (itemRepository.existsByStuffIdAndNum(stuffId, num)) {
            throw new ConflictException();
        }
    }
}
