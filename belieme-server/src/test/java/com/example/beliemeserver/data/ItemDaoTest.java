package com.example.beliemeserver.data;

import com.example.beliemeserver.model.dao.ItemDao;
import com.example.beliemeserver.model.dto.HistoryDto;
import com.example.beliemeserver.model.dto.ItemDto;
import com.example.beliemeserver.model.dto.StuffDto;
import com.example.beliemeserver.util.DummyDataSet;
import com.example.beliemeserver.util.TestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class ItemDaoTest extends DaoTest {
    @Autowired
    private ItemDao itemDao;

    @Test
    public void getAllListTest() {
        TestHelper.listCompareTest(
                () -> itemDao.getAllList(),
                itemFakeDao.getAll()
        );
    }

    @Test
    public void getListByStuffTest() {
        String universityCode = "HYU";
        String departmentCode = "STU";
        String stuffName = "축구공";

        TestHelper.listCompareTest(
                () -> itemDao.getListByStuff(universityCode, departmentCode, stuffName),
                itemFakeDao.getAllByCondition(
                        (target) -> {
                            return universityCode.equals(target.stuff().department().university().code())
                                    && departmentCode.equals(target.stuff().department().code())
                                    && stuffName.equals(target.stuff().name());
                        }
                )
        );
    }

    @Test
    public void getByIndexTest() {
        String universityCode = "HYU";
        String departmentCode = "CSE";
        String stuffName = "우산";
        int itemNum = 1;

        TestHelper.objectCompareTest(
                () -> itemDao.getByIndex(universityCode, departmentCode, stuffName, itemNum),
                getItemDummy(universityCode, departmentCode, stuffName, itemNum)
        );
    }

    @Test
    public void createTest() {
        ItemDto newItem = ItemDto.init(
                DummyDataSet.stuffDummies.get(0),
                5
        );

        testCreatingItem(newItem);
    }

    @Test
    public void updateTest() {
        String universityCode = "HYU";
        String departmentCode = "CSE";
        String stuffName = "우산";
        int itemNum = 1;

        ItemDto newItem = new ItemDto(
                DummyDataSet.stuffDummies.get(0),
                1,
                DummyDataSet.historyDummies.get(0)
        );

        testUpdatingItem(universityCode, departmentCode, stuffName, itemNum, newItem);
    }

    private void testCreatingItem(ItemDto newItem) {
        ItemDto expectingResult = newItem;
        if(newItem.lastHistory() != null) {
            HistoryDto nestedHistory = newItem.lastHistory().withItem(ItemDto.nestedEndpoint);
            expectingResult = newItem.withLastHistory(nestedHistory);
        }

        TestHelper.objectCompareTest(
                () -> simplify(itemDao.create(newItem)),
                simplify(expectingResult)
        );

        TestHelper.listCompareTest(
                () -> simplify(itemDao.getAllList()),
                simplify(itemFakeDao.dummyStatusAfterCreate(expectingResult))
        );
    }

    private void testUpdatingItem(
            String universityCode, String departmentCode,
            String stuffName, int itemNum, ItemDto newItem) {
        ItemDto expectingResult = newItem;
        if(newItem.lastHistory() != null) {
            HistoryDto nestedHistory = newItem.lastHistory().withItem(ItemDto.nestedEndpoint);
            expectingResult = newItem.withLastHistory(nestedHistory);
        }


        TestHelper.objectCompareTest(
                () -> simplify(itemDao.update(universityCode, departmentCode, stuffName, itemNum, newItem)),
                simplify(expectingResult)
        );

        ItemDto targetOnDummy =
                getItemDummy(universityCode, departmentCode, stuffName, itemNum);
        TestHelper.listCompareTest(
                () -> simplify(itemDao.getAllList()),
                simplify(itemFakeDao.dummyStatusAfterUpdate(targetOnDummy, expectingResult))
        );
    }

    private ItemDto simplify(ItemDto result) {
        if(result == null) {
            return null;
        }
        StuffDto simplifiedStuff = result.stuff().withItems(List.of());
        return result.withStuff(simplifiedStuff);
    }

    private List<ItemDto> simplify(List<ItemDto> itemDtoList) {
        List<ItemDto> output = new ArrayList<>();
        for(ItemDto itemDto : itemDtoList) {
            output.add(simplify(itemDto));
        }

        return output;
    }
}
