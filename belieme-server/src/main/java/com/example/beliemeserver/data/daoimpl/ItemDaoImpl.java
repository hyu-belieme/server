package com.example.beliemeserver.data.daoimpl;

import com.example.beliemeserver.data.entity.HistoryEntity;
import com.example.beliemeserver.data.entity.ItemEntity;
import com.example.beliemeserver.data.entity.StuffEntity;
import com.example.beliemeserver.data.repository.*;
import com.example.beliemeserver.model.dao.ItemDao;
import com.example.beliemeserver.model.dto.ItemDto;
import com.example.beliemeserver.model.exception.ConflictException;
import com.example.beliemeserver.model.exception.DataException;
import com.example.beliemeserver.model.exception.NotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemDaoImpl extends BaseDaoImpl implements ItemDao {
    public ItemDaoImpl(UniversityRepository universityRepository, DepartmentRepository departmentRepository, UserRepository userRepository, MajorRepository majorRepository, MajorUserJoinRepository majorUserJoinRepository, MajorDepartmentJoinRepository majorDepartmentJoinRepository, AuthorityRepository authorityRepository, AuthorityUserJoinRepository authorityUserJoinRepository, StuffRepository stuffRepository, ItemRepository itemRepository, HistoryRepository historyRepository) {
        super(universityRepository, departmentRepository, userRepository, majorRepository, majorUserJoinRepository, majorDepartmentJoinRepository, authorityRepository, authorityUserJoinRepository, stuffRepository, itemRepository, historyRepository);
    }

    @Override
    public List<ItemDto> getAllList() throws DataException {
        List<ItemDto> output = new ArrayList<>();
        for(ItemEntity itemEntity : itemRepository.findAll()) {
            output.add(itemEntity.toItemDto());
        }
        return output;
    }

    @Override
    public List<ItemDto> getListByStuff(String universityCode, String departmentCode, String stuffName) throws DataException, NotFoundException {
        StuffEntity targetStuff = getStuffEntity(universityCode, departmentCode, stuffName);
        List<ItemDto> output = new ArrayList<>();
        for(ItemEntity itemEntity : itemRepository.findByStuffId(targetStuff.getId())) {
            output.add(itemEntity.toItemDto());
        }
        return output;
    }

    @Override
    public ItemDto getByIndex(String universityCode, String departmentCode, String stuffName, int itemNum) throws NotFoundException, DataException {
        return getItemEntity(universityCode, departmentCode, stuffName, itemNum).toItemDto();
    }

    @Override
    public ItemDto create(ItemDto newItem) throws ConflictException, NotFoundException, DataException {
        StuffEntity stuffOfNewItem  = getStuffEntity(newItem.stuff());

        checkItemConflict(stuffOfNewItem.getId(), newItem.num());

        ItemEntity newItemEntity = new ItemEntity(
                stuffOfNewItem,
                newItem.num(),
                null
        );
        return itemRepository.save(newItemEntity).toItemDto();
    }

    @Override
    public ItemDto update(String universityCode, String departmentCode, String stuffName, int itemNum, ItemDto newItem) throws ConflictException, NotFoundException, DataException {
        ItemEntity target = getItemEntity(universityCode, departmentCode, stuffName, itemNum);
        StuffEntity stuffOfNewItem  = getStuffEntity(newItem.stuff());
        HistoryEntity lastHistoryOfNewItem = getHistoryEntity(newItem.lastHistory());

        if(doesIndexChange(target, newItem)) {
            checkItemConflict(stuffOfNewItem.getId(), newItem.num());
        }

        target.setStuff(stuffOfNewItem)
                .setNum(newItem.num())
                .setLastHistory(lastHistoryOfNewItem);
        return target.toItemDto();
    }

    private void checkItemConflict(int stuffId, int num) throws ConflictException {
        if(itemRepository.existsByStuffIdAndNum(stuffId, num))  {
            throw new ConflictException();
        }
    }

    private boolean doesIndexChange(ItemEntity target, ItemDto newItem) {
        String oldUniversityCode = target.getStuff().getDepartment().getUniversity().getCode();
        String oldDepartmentCode = target.getStuff().getDepartment().getCode();
        String oldStuffName = target.getStuff().getName();
        int oldItemNum = target.getNum();

        String newUniversityCode = newItem.stuff().department().university().code();
        String newDepartmentCode = newItem.stuff().department().code();
        String newStuffName = newItem.stuff().name();
        int newItemNum = newItem.num();

        return !(oldUniversityCode.equals(newUniversityCode)
                && oldDepartmentCode.equals(newDepartmentCode)
                && oldStuffName.equals(newStuffName)
                && oldItemNum == newItemNum);
    }
}
