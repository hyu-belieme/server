package com.example.beliemeserver.data;

import com.example.beliemeserver.model.dao.DepartmentDao;
import com.example.beliemeserver.model.dto.DepartmentDto;
import com.example.beliemeserver.model.dto.MajorDto;
import com.example.beliemeserver.model.exception.ConflictException;
import com.example.beliemeserver.model.exception.NotFoundException;
import com.example.beliemeserver.util.DummyDataSet;
import com.example.beliemeserver.util.TestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class DepartmentDaoTest extends DaoTest {
    @Autowired
    private DepartmentDao departmentDao;

    @Test
    public void getAllListTest() {
        TestHelper.listCompareTest(
                () -> departmentDao.getAllDepartmentsData(),
                departmentFakeDao.getAll()
        );
    }

    @Test
    public void getListByUniversityCodeTest() {
        String targetUniversityCode = "HYU";

        List<DepartmentDto> expected = departmentFakeDao.getAllByCondition(
                target -> targetUniversityCode.equals(target.university().code())
        );
        TestHelper.listCompareTest(
                () -> departmentDao.getAllDepartmentsByUniversityCodeData(targetUniversityCode),
                expected
        );
    }

    @Test
    public void getListByUniversityCodeFailByNotFoundExceptionTest() {
        String targetUniversityCode = "KNU";

        TestHelper.exceptionTest(
                () -> departmentDao.getAllDepartmentsByUniversityCodeData(targetUniversityCode),
                NotFoundException.class
        );
    }

    @Test
    public void getByIndexTest() {
        String targetUniversityCode = "HYU";
        String targetDepartmentCode = "CSE";

        TestHelper.objectCompareTest(
                () -> departmentDao.getDepartmentByUniversityCodeAndDepartmentCodeData(targetUniversityCode, targetDepartmentCode),
                getDepartmentDummy(targetUniversityCode, targetDepartmentCode)
        );
    }

    @Test
    public void getByIndexFailByNotFoundExceptionTest() {
        String targetUniversityCode = "HYU";
        String targetDepartmentCode = "MED";

        TestHelper.exceptionTest(
                () -> departmentDao.getDepartmentByUniversityCodeAndDepartmentCodeData(targetUniversityCode, targetDepartmentCode),
                NotFoundException.class
        );
    }

    @Test
    public void createTest1() {
        List<MajorDto> newDepartmentBaseMajor = List.of(
                DummyDataSet.majorDummies.get(0),
                DummyDataSet.majorDummies.get(1),
                DummyDataSet.majorDummies.get(2)
        );

        DepartmentDto newDepartment = new DepartmentDto(
                DummyDataSet.universityDummies.get(0),
                "ENG",
                "공과대학",
                newDepartmentBaseMajor
        );

        testCreatingDepartment(newDepartment);
    }

    @Test
    public void createNewDepartmentTest2() {
        List<MajorDto> newDepartmentBaseMajor = List.of(
                DummyDataSet.majorDummies.get(6)
        );

        DepartmentDto newDepartment = new DepartmentDto(
                DummyDataSet.universityDummies.get(1),
                "CSE",
                "컴퓨터공학과",
                newDepartmentBaseMajor
        );

        testCreatingDepartment(newDepartment);
    }

    @Test
    public void createNewDepartmentFailByConflictExceptionTest() {
        List<MajorDto> newDepartmentBaseMajor = List.of(
                DummyDataSet.majorDummies.get(6)
        );

        DepartmentDto newDepartment = new DepartmentDto(
                DummyDataSet.universityDummies.get(1),
                "MED",
                "의과대학",
                newDepartmentBaseMajor
        );

        TestHelper.exceptionTest(
                () -> departmentDao.addDepartmentData(newDepartment),
                ConflictException.class
        );
    }

    @Test
    public void createNewDepartmentFailByNotFoundExceptionTest() {
        List<MajorDto> newDepartmentBaseMajor = List.of(
                DummyDataSet.majorDummies.get(6)
        );

        DepartmentDto newDepartment = new DepartmentDto(
                DummyDataSet.notFoundUniversity,
                "MED",
                "의과대학",
                newDepartmentBaseMajor
        );

        TestHelper.exceptionTest(
                () -> departmentDao.addDepartmentData(newDepartment),
                NotFoundException.class
        );
    }

    @Test
    public void updateNewDepartmentTest1() {
        String targetUniversityCode = "HYU";
        String targetDepartmentCode = "CSE";

        List<MajorDto> newDepartmentBaseMajor = List.of(
                DummyDataSet.majorDummies.get(0),
                DummyDataSet.majorDummies.get(1)
        );

        DepartmentDto newDepartment = new DepartmentDto(
                DummyDataSet.universityDummies.get(0),
                "MED",
                "의과대학",
                newDepartmentBaseMajor
        );

        testUpdatingDepartment(targetUniversityCode, targetDepartmentCode, newDepartment);
    }

    @Test
    public void updateNewDepartmentTest2() {
        String targetUniversityCode = "HYU";
        String targetDepartmentCode = "CSE";

        List<MajorDto> newDepartmentBaseMajor = List.of(
                DummyDataSet.majorDummies.get(0),
                DummyDataSet.majorDummies.get(1),
                DummyDataSet.majorDummies.get(2)
        );

        DepartmentDto newDepartment = new DepartmentDto(
                DummyDataSet.universityDummies.get(0),
                "ENG",
                "공과대학",
                newDepartmentBaseMajor
        );

        testUpdatingDepartment(targetUniversityCode, targetDepartmentCode, newDepartment);
    }

    @Test
    public void updateNewDepartmentFailByNotFoundExceptionTest1() {
        String targetUniversityCode = "HYU";
        String targetDepartmentCode = "COMPUTER";

        List<MajorDto> newDepartmentBaseMajor = List.of(
                DummyDataSet.majorDummies.get(6)
        );

        DepartmentDto newDepartment = new DepartmentDto(
                DummyDataSet.universityDummies.get(0),
                "MED",
                "의과대학",
                newDepartmentBaseMajor
        );

        TestHelper.exceptionTest(
                () -> departmentDao.updateDepartmentData(targetUniversityCode, targetDepartmentCode, newDepartment),
                NotFoundException.class
        );
    }

    @Test
    public void updateNewDepartmentFailByNotFoundExceptionTest2() {
        String targetUniversityCode = "HYU";
        String targetDepartmentCode = "CSE";

        List<MajorDto> newDepartmentBaseMajor = List.of(
                DummyDataSet.majorDummies.get(6),
                DummyDataSet.notFoundMajor
        );

        DepartmentDto newDepartment = new DepartmentDto(
                DummyDataSet.universityDummies.get(0),
                "MED",
                "의과대학",
                newDepartmentBaseMajor
        );

        TestHelper.exceptionTest(
                () -> departmentDao.updateDepartmentData(targetUniversityCode, targetDepartmentCode, newDepartment),
                NotFoundException.class
        );
    }

    @Test
    public void updateNewDepartmentFailByConflictExceptionTest() {
        String targetUniversityCode = "HYU";
        String targetDepartmentCode = "CSE";

        List<MajorDto> newDepartmentBaseMajor = List.of(
                DummyDataSet.majorDummies.get(6)
        );

        DepartmentDto newDepartment = new DepartmentDto(
                DummyDataSet.universityDummies.get(1),
                "MED",
                "의과대학",
                newDepartmentBaseMajor
        );

        TestHelper.exceptionTest(
                () -> departmentDao.updateDepartmentData(targetUniversityCode, targetDepartmentCode, newDepartment),
                ConflictException.class
        );
    }

    @Test
    public void putBaseMajorOnDepartmentTest() {
        String targetUniversityCode = "HYU";
        String targetDepartmentCode = "CSE";

        String newUniversityCode = "HYU";
        String newMajorCode = "FH04069";

        DepartmentDto targetDepartment =
                getDepartmentDummy(targetUniversityCode, targetDepartmentCode);
        MajorDto newMajor =
                getMajorDummy(newUniversityCode, newMajorCode);

        List<MajorDto> expectedBaseMajors = targetDepartment.baseMajors();
        expectedBaseMajors.add(newMajor);

        DepartmentDto expectedResult = new DepartmentDto(
                getUniversityDummy(targetUniversityCode),
                targetDepartmentCode,
                targetDepartment.name(),
                expectedBaseMajors
        );

        TestHelper.objectCompareTest(
                () -> departmentDao.putBaseMajorOnDepartmentData(targetUniversityCode, targetDepartmentCode, newMajor),
                expectedResult
        );

        TestHelper.listCompareTest(
                () -> departmentDao.getAllDepartmentsData(),
                departmentFakeDao.dummyStatusAfterUpdate(
                        targetDepartment,
                        expectedResult
                )
        );
    }

    @Test
    public void removeBaseMajorOnDepartmentTest() {
        String targetUniversityCode = "HYU";
        String targetDepartmentCode = "CSE";

        String targetMajorUniversityCode = "HYU";
        String targetMajorCode = "FH04068";

        MajorDto targetMajor = getMajorDummy(targetMajorUniversityCode, targetMajorCode);

        DepartmentDto targetDepartment = getDepartmentDummy(targetUniversityCode, targetDepartmentCode);
        List<MajorDto> expectedBaseMajors = targetDepartment.baseMajors();
        expectedBaseMajors.remove(targetMajor);

        DepartmentDto expectedResult = new DepartmentDto(
                getUniversityDummy(targetUniversityCode),
                targetDepartmentCode,
                targetDepartment.name(),
                expectedBaseMajors
        );

        TestHelper.objectCompareTest(
                () -> departmentDao.removeBaseMajorOnDepartmentData(targetUniversityCode, targetDepartmentCode, targetMajor),
                expectedResult
        );

        TestHelper.listCompareTest(
                () -> departmentDao.getAllDepartmentsData(),
                departmentFakeDao.dummyStatusAfterUpdate(
                        targetDepartment,
                        expectedResult
                )
        );
    }

    private void testCreatingDepartment(DepartmentDto newDepartment) {
        TestHelper.objectCompareTest(
                () -> departmentDao.addDepartmentData(newDepartment),
                newDepartment
        );

        TestHelper.listCompareTest(
                () -> departmentDao.getAllDepartmentsData(),
                departmentFakeDao.dummyStatusAfterCreate(newDepartment)
        );
    }

    private void testUpdatingDepartment(String targetUniversityCode, String targetDepartmentCode, DepartmentDto newDepartment) {
        TestHelper.objectCompareTest(
                () -> departmentDao.updateDepartmentData(targetUniversityCode, targetDepartmentCode, newDepartment),
                newDepartment
        );

        DepartmentDto targetOnDummy =
                getDepartmentDummy(targetUniversityCode, targetDepartmentCode);
        TestHelper.listCompareTest(
                () -> departmentDao.getAllDepartmentsData(),
                departmentFakeDao.dummyStatusAfterUpdate(targetOnDummy, newDepartment)
        );
    }
}