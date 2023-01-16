package com.example.beliemeserver.data;

import com.example.beliemeserver.model.dao.HistoryDao;
import com.example.beliemeserver.model.dto.HistoryDto;
import com.example.beliemeserver.util.DummyDataSet;
import com.example.beliemeserver.util.TestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class HistoryDaoTest extends DaoTest {
    @Autowired
    private HistoryDao historyDao;

    @Test
    public void getAllListTest() {
        TestHelper.listCompareTest(
                () -> historyDao.getAllList(),
                historyFakeDao.getAll()
        );
    }

    @Test
    public void getListByDepartmentTest() {
        String universityCode = "HYU";
        String departmentCode = "CSE";

        TestHelper.listCompareTest(
                () -> historyDao.getListByDepartment(universityCode, departmentCode),
                historyFakeDao.getAllByCondition(
                        (target) -> {
                            return universityCode.equals(target.item().stuff().department().university().code())
                                    && departmentCode.equals(target.item().stuff().department().code());
                        }
                )
        );
    }

    @Test
    public void getListByDepartmentTest2() {
        String universityCode = "HYU";
        String departmentCode = "STU";

        TestHelper.listCompareTest(
                () -> historyDao.getListByDepartment(universityCode, departmentCode),
                historyFakeDao.getAllByCondition(
                        (target) -> {
                            return universityCode.equals(target.item().stuff().department().university().code())
                                    && departmentCode.equals(target.item().stuff().department().code());
                        }
                )
        );
    }

    @Test
    public void getListByDepartmentAndRequesterTest() {
        String universityCode = "HYU";
        String departmentCode = "CSE";
        String universityCodeForRequester = "HYU";
        String studentId = "2018008886";

        TestHelper.listCompareTest(
                () -> historyDao.getListByDepartmentAndRequester(universityCode, departmentCode, universityCodeForRequester, studentId),
                historyFakeDao.getAllByCondition(
                        (target) -> {
                            return universityCode.equals(target.item().stuff().department().university().code())
                                    && departmentCode.equals(target.item().stuff().department().code())
                                    && target.requester() != null
                                    && universityCodeForRequester.equals(target.requester().university().code())
                                    && departmentCode.equals(target.requester().studentId());
                        }
                )
        );
    }

    @Test
    public void getByIndexTest() {
        String universityCode = "CKU";
        String departmentCode = "MED";
        String stuffName = "볼펜";
        int itemNum = 3;
        int historyNum = 0;

        TestHelper.objectCompareTest(
                () -> historyDao.getByIndex(universityCode, departmentCode,
                        stuffName, itemNum, historyNum),
                getHistoryDummy(universityCode, departmentCode, stuffName,
                        itemNum, historyNum)
        );
    }

    @Test
    public void createTest() {
        HistoryDto newHistory = new HistoryDto(
                DummyDataSet.itemDummies.get(12),
                1,
                DummyDataSet.userDummies.get(1),
                null,
                null,
                null,
                null,
                1673358789,
                0,
                0,
                0,
                0
        );

        testCreatingHistory(newHistory);
    }

    @Test
    public void updateTest() {
        String universityCode = "HYU";
        String departmentCode = "CSE";
        String stuffName = "우산";
        int itemNum = 1;
        int historyNum = 2;

        HistoryDto newHistory = new HistoryDto(
                DummyDataSet.itemDummies.get(0),
                2,
                DummyDataSet.userDummies.get(1),
                DummyDataSet.userDummies.get(0),
                DummyDataSet.userDummies.get(0),
                null,
                null,
                1673172221,
                1673172521,
                1673358789,
                0,
                0
        );

        testUpdatingHistory(universityCode, departmentCode, stuffName, itemNum,
                historyNum, newHistory);
    }

    private void testCreatingHistory(HistoryDto newHistory) {
        TestHelper.objectCompareTest(
                () -> historyDao.create(newHistory),
                newHistory
        );

        TestHelper.listCompareTest(
                () -> historyDao.getAllList(),
                historyFakeDao.dummyStatusAfterCreate(newHistory)
        );
    }

    private void testUpdatingHistory(
            String universityCode, String departmentCode,
            String stuffName, int itemNum, int historyNum, HistoryDto newHistory) {
        TestHelper.objectCompareTest(
                () -> historyDao.update(universityCode, departmentCode, stuffName,
                        itemNum, historyNum, newHistory),
                newHistory
        );

        HistoryDto targetOnDummy =
                getHistoryDummy(universityCode, departmentCode, stuffName,
                        itemNum, historyNum);
        TestHelper.listCompareTest(
                () -> historyDao.getAllList(),
                historyFakeDao.dummyStatusAfterUpdate(targetOnDummy, newHistory)
        );
    }
}
