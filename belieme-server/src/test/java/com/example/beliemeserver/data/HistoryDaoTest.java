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
        // TODO set good new data
        String universityCode = "";
        String departmentCode = "";

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
        // TODO set good new data
        String universityCode = "";
        String departmentCode = "";
        String universityCodeForRequester = "";
        String studentId = "";

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
        // TODO set good new data
        String universityCode = "";
        String departmentCode = "";
        String stuffName = "";
        int itemNum = 0;
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
        // TODO set good new data
        HistoryDto newHistory = new HistoryDto(
                DummyDataSet.itemDummies.get(0),
                4,
                DummyDataSet.userDummies.get(0),
                null,
                null,
                null,
                null,
                12341234,
                0,
                0,
                0,
                0
        );

        testCreatingHistory(newHistory);
    }

    @Test
    public void updateTest() {
        String universityCode = "";
        String departmentCode = "";
        String stuffName = "";
        int itemNum = 0;
        int historyNum = 0;

        // TODO set good new data
        HistoryDto newHistory = new HistoryDto(
                DummyDataSet.itemDummies.get(0),
                4,
                DummyDataSet.userDummies.get(0),
                null,
                null,
                null,
                null,
                12341234,
                0,
                0,
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
