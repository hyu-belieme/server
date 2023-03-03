package com.example.beliemeserver.data;

import com.example.beliemeserver.model.dao.StuffDao;
import com.example.beliemeserver.model.dto.StuffDto;
import com.example.beliemeserver.util.DummyDataSet;
import com.example.beliemeserver.util.TestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class StuffDaoTest extends DaoTest {
    @Autowired
    private StuffDao stuffDao;

    @Test
    public void getAllListTest() {
        TestHelper.listCompareTest(
                () -> stuffDao.getAllList(),
                stuffFakeDao.getAll()
        );
    }

    @Test
    public void getListByDepartmentTest() {
        String universityCode = "HYU";
        String departmentCode = "CSE";
        TestHelper.listCompareTest(
                () -> stuffDao.getListByDepartment(universityCode, departmentCode),
                stuffFakeDao.getAllByCondition(
                        (target) -> {
                            return universityCode.equals(target.department().university().code())
                                    && departmentCode.equals(target.department().code());
                        }
                )
        );
    }

    @Test
    public void getByIndexTest() {
        // TODO set good new data
        String universityCode = "CKU";
        String departmentCode = "MED";
        String stuffName = "스케이트보드";
        TestHelper.objectCompareTest(
                () -> stuffDao.getByIndex(universityCode, departmentCode, stuffName),
                getStuffDummy(universityCode, departmentCode, stuffName)
        );
    }

    @Test
    public void createTest() {
        // TODO set good new data
        StuffDto newStuff = StuffDto.init(
                DummyDataSet.departmentDummies.get(3),
                "가위",
                "✂️"
        );

        testCreatingStuff(newStuff);
    }

    @Test
    public void updateTest() {
        String universityCode = "HYU";
        String departmentCode = "CSE";
        String stuffName = "블루투스스피커";

        // TODO set good new data
        StuffDto newStuff = getStuffDummy(universityCode, departmentCode, stuffName)
                .withName("스피커");

        testUpdatingStuff(universityCode, departmentCode, stuffName, newStuff);
    }

    private void testCreatingStuff(StuffDto newStuff) {
        TestHelper.objectCompareTest(
                () -> stuffDao.create(newStuff),
                newStuff
        );

        TestHelper.listCompareTest(
                () -> stuffDao.getAllList(),
                stuffFakeDao.dummyStatusAfterCreate(newStuff)
        );
    }

    private void testUpdatingStuff(
            String universityCode, String departmentCode,
            String stuffName, StuffDto newStuff) {
        TestHelper.objectCompareTest(
                () -> stuffDao.update(universityCode, departmentCode, stuffName, newStuff),
                newStuff
        );

        StuffDto targetOnDummy =
                getStuffDummy(universityCode, departmentCode, stuffName);
        TestHelper.listCompareTest(
                () -> stuffDao.getAllList(),
                stuffFakeDao.dummyStatusAfterUpdate(targetOnDummy, newStuff)
        );
    }
}
