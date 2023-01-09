package com.example.beliemeserver.data;

import com.example.beliemeserver.model.dao.ItemDao;
import com.example.beliemeserver.model.dto.ItemDto;
import com.example.beliemeserver.util.DummyDataSet;
import com.example.beliemeserver.util.TestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
        // TODO set good new data
        String universityCode = "";
        String departmentCode = "";
        String stuffName = "";

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
        // TODO set good new data
        String universityCode = "";
        String departmentCode = "";
        String stuffName = "";
        int itemNum = 0;

        TestHelper.objectCompareTest(
                () -> itemDao.getByIndex(universityCode, departmentCode, stuffName, itemNum),
                getItemDummy(universityCode, departmentCode, stuffName, itemNum)
        );
    }

    @Test
    public void createTest() {
        // TODO set good new data
        ItemDto newItem = ItemDto.init(
                DummyDataSet.stuffDummies.get(0),
                4
        );

        testCreatingItem(newItem);
    }

    @Test
    public void updateTest() {
        String universityCode = "";
        String departmentCode = "";
        String stuffName = "";
        int itemNum = 0;

        // TODO set good new data
        ItemDto newItem = new ItemDto(
                DummyDataSet.stuffDummies.get(0),
                1,
                DummyDataSet.historyDummies.get(0)
        );

        testUpdatingItem(universityCode, departmentCode, stuffName, itemNum, newItem);
    }

    private void testCreatingItem(ItemDto newItem) {
        TestHelper.objectCompareTest(
                () -> itemDao.create(newItem),
                newItem
        );

        TestHelper.listCompareTest(
                () -> itemDao.getAllList(),
                itemFakeDao.dummyStatusAfterCreate(newItem)
        );
    }

    private void testUpdatingItem(
            String universityCode, String departmentCode,
            String stuffName, int itemNum, ItemDto newItem) {
        TestHelper.objectCompareTest(
                () -> itemDao.update(universityCode, departmentCode, stuffName, itemNum, newItem),
                newItem
        );

        ItemDto targetOnDummy =
                getItemDummy(universityCode, departmentCode, stuffName, itemNum);
        TestHelper.listCompareTest(
                () -> itemDao.getAllList(),
                itemFakeDao.dummyStatusAfterUpdate(targetOnDummy, newItem)
        );
    }
}
