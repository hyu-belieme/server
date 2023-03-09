package com.example.beliemeserver.data.daoimpl;

import com.example.beliemeserver.data.entity.HistoryEntity;
import com.example.beliemeserver.data.entity.ItemEntity;
import com.example.beliemeserver.data.entity.StuffEntity;
import com.example.beliemeserver.data.repository.*;
import com.example.beliemeserver.error.exception.ConflictException;
import com.example.beliemeserver.model.dao.ItemDao;
import com.example.beliemeserver.model.dto.HistoryDto;
import com.example.beliemeserver.model.dto.ItemDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemDaoImpl extends BaseDaoImpl implements ItemDao {
    public ItemDaoImpl(UniversityRepository universityRepository, DepartmentRepository departmentRepository, UserRepository userRepository, MajorRepository majorRepository, MajorDepartmentJoinRepository majorDepartmentJoinRepository, AuthorityRepository authorityRepository, AuthorityUserJoinRepository authorityUserJoinRepository, StuffRepository stuffRepository, ItemRepository itemRepository, HistoryRepository historyRepository) {
        super(universityRepository, departmentRepository, userRepository, majorRepository, majorDepartmentJoinRepository, authorityRepository, authorityUserJoinRepository, stuffRepository, itemRepository, historyRepository);
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
    public List<ItemDto> getListByStuff(String universityCode, String departmentCode, String stuffName) {
        StuffEntity targetStuff = findStuffEntity(universityCode, departmentCode, stuffName);
        List<ItemDto> output = new ArrayList<>();
        for (ItemEntity itemEntity : itemRepository.findByStuffId(targetStuff.getId())) {
            output.add(itemEntity.toItemDto());
        }
        return output;
    }

    @Override
    public ItemDto getByIndex(String universityCode, String departmentCode, String stuffName, int itemNum) {
        return findItemEntity(universityCode, departmentCode, stuffName, itemNum).toItemDto();
    }

    @Override
    public ItemDto create(ItemDto newItem) {
        StuffEntity stuffOfNewItem = findStuffEntity(newItem.stuff());

        checkItemConflict(stuffOfNewItem.getId(), newItem.num());

        ItemEntity newItemEntity = new ItemEntity(
                stuffOfNewItem,
                newItem.num(),
                null
        );
        return itemRepository.save(newItemEntity).toItemDto();
    }

    @Override
    public ItemDto update(String universityCode, String departmentCode, String stuffName, int itemNum, ItemDto newItem) {
        ItemEntity target = findItemEntity(universityCode, departmentCode, stuffName, itemNum);
        StuffEntity stuffOfNewItem = findStuffEntity(newItem.stuff());
        HistoryEntity lastHistoryOfNewItem = toHistoryEntityOrNull(newItem.lastHistory());

        if (doesIndexChange(target, newItem)) {
            checkItemConflict(stuffOfNewItem.getId(), newItem.num());
        }

        target.setStuff(stuffOfNewItem)
                .setNum(newItem.num())
                .setLastHistory(lastHistoryOfNewItem);
        return target.toItemDto();
    }

    private HistoryEntity toHistoryEntityOrNull(HistoryDto historyDto) {
        if (historyDto == null) {
            return null;
        }
        return findHistoryEntity(historyDto);
    }

    private boolean doesIndexChange(ItemEntity target, ItemDto newItem) {
        String oldUniversityCode = target.getStuff().getDepartment().getUniversity().getCode();
        String oldDepartmentCode = target.getStuff().getDepartment().getCode();
        String oldStuffName = target.getStuff().getName();
        int oldItemNum = target.getNum();

        return !(oldUniversityCode.equals(newItem.stuff().department().university().code())
                && oldDepartmentCode.equals(newItem.stuff().department().code())
                && oldStuffName.equals(newItem.stuff().name())
                && oldItemNum == newItem.num());
    }

    private void checkItemConflict(int stuffId, int num) {
        if (itemRepository.existsByStuffIdAndNum(stuffId, num)) {
            throw new ConflictException();
        }
    }
}
