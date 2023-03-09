package com.example.beliemeserver.data;

import com.example.beliemeserver.error.exception.ConflictException;
import com.example.beliemeserver.error.exception.NotFoundException;
import com.example.beliemeserver.model.dao.DepartmentDao;
import com.example.beliemeserver.model.dto.DepartmentDto;
import com.example.beliemeserver.model.dto.MajorDto;
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
                () -> departmentDao.getAllList(),
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
                () -> departmentDao.getListByUniversity(targetUniversityCode),
                expected
        );
    }

    @Test
    public void getListByUniversityCodeFailByNotFoundExceptionTest() {
        String targetUniversityCode = "KNU";

        TestHelper.exceptionTest(
                () -> departmentDao.getListByUniversity(targetUniversityCode),
                NotFoundException.class
        );
    }

    @Test
    public void getByIndexTest() {
        String targetUniversityCode = "HYU";
        String targetDepartmentCode = "CSE";

        TestHelper.objectCompareTest(
                () -> departmentDao.getByIndex(targetUniversityCode, targetDepartmentCode),
                getDepartmentDummy(targetUniversityCode, targetDepartmentCode)
        );
    }

    @Test
    public void getByIndexFailByNotFoundExceptionTest() {
        String targetUniversityCode = "HYU";
        String targetDepartmentCode = "MED";

        TestHelper.exceptionTest(
                () -> departmentDao.getByIndex(targetUniversityCode, targetDepartmentCode),
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
                DummyDataSet.universityDummies.get(1),
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
                DummyDataSet.universityDummies.get(2),
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
                DummyDataSet.universityDummies.get(2),
                "MED",
                "의과대학",
                newDepartmentBaseMajor
        );

        TestHelper.exceptionTest(
                () -> departmentDao.create(newDepartment),
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
                () -> departmentDao.create(newDepartment),
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
                DummyDataSet.universityDummies.get(1),
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
                DummyDataSet.universityDummies.get(1),
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
                DummyDataSet.universityDummies.get(1),
                "MED",
                "의과대학",
                newDepartmentBaseMajor
        );

        TestHelper.exceptionTest(
                () -> departmentDao.update(targetUniversityCode, targetDepartmentCode, newDepartment),
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
                DummyDataSet.universityDummies.get(1),
                "MED",
                "의과대학",
                newDepartmentBaseMajor
        );

        TestHelper.exceptionTest(
                () -> departmentDao.update(targetUniversityCode, targetDepartmentCode, newDepartment),
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
                DummyDataSet.universityDummies.get(2),
                "MED",
                "의과대학",
                newDepartmentBaseMajor
        );

        TestHelper.exceptionTest(
                () -> departmentDao.update(targetUniversityCode, targetDepartmentCode, newDepartment),
                ConflictException.class
        );
    }

    private void testCreatingDepartment(DepartmentDto newDepartment) {
        TestHelper.objectCompareTest(
                () -> departmentDao.create(newDepartment),
                newDepartment
        );

        TestHelper.listCompareTest(
                () -> departmentDao.getAllList(),
                departmentFakeDao.dummyStatusAfterCreate(newDepartment)
        );
    }

    private void testUpdatingDepartment(String targetUniversityCode, String targetDepartmentCode, DepartmentDto newDepartment) {
        TestHelper.objectCompareTest(
                () -> departmentDao.update(targetUniversityCode, targetDepartmentCode, newDepartment),
                newDepartment
        );

        DepartmentDto targetOnDummy =
                getDepartmentDummy(targetUniversityCode, targetDepartmentCode);
        TestHelper.listCompareTest(
                () -> departmentDao.getAllList(),
                departmentFakeDao.dummyStatusAfterUpdate(targetOnDummy, newDepartment)
        );
    }
}