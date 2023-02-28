package com.example.beliemeserver.data;

import com.example.beliemeserver.exception.ConflictException;
import com.example.beliemeserver.exception.NotFoundException;
import com.example.beliemeserver.model.dao.MajorDao;
import com.example.beliemeserver.model.dto.MajorDto;
import com.example.beliemeserver.util.DummyDataSet;
import com.example.beliemeserver.util.TestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MajorDaoTest extends DaoTest {
    @Autowired
    private MajorDao majorDao;

    @Test
    public void getAllMajorsTest() {
        TestHelper.listCompareTest(
                () -> majorDao.getAllList(),
                majorFakeDao.getAll()
        );
    }

    @Test
    public void createNewMajorTest1() {
        MajorDto newMajor = new MajorDto(
                DummyDataSet.universityDummies.get(1),
                "FH04070");

        testCreatingMajor(newMajor);
    }

    @Test
    public void createNewMajorTest2() {
        MajorDto newMajor = new MajorDto(
                DummyDataSet.universityDummies.get(2), "FH04068");

        testCreatingMajor(newMajor);
    }

    @Test
    public void createNewMajorFailByConflictExceptionTest() {
        MajorDto newMajor = new MajorDto(
                DummyDataSet.universityDummies.get(1), "FH04068");

        TestHelper.exceptionTest(
                () -> majorDao.create(newMajor),
                ConflictException.class
        );
    }

    @Test
    public void createNewMajorFailByNotFoundExceptionTest() {
        MajorDto newMajor = new MajorDto(DummyDataSet.notFoundUniversity, "FH04070");

        TestHelper.exceptionTest(
                () -> majorDao.create(newMajor),
                NotFoundException.class
        );
    }

    @Test
    public void updateNewMajorTest1() {
        String targetUniversityCode = "HYU";
        String targetMajorCode = "FH04069";
        MajorDto newMajor = new MajorDto(
                DummyDataSet.universityDummies.get(1), "FH04070");

        testUpdatingMajor(targetUniversityCode, targetMajorCode, newMajor);
    }

    @Test
    public void updateNewMajorTest2() {
        String targetUniversityCode = "HYU";
        String targetMajorCode = "FH04069";
        MajorDto newMajor = new MajorDto(
                DummyDataSet.universityDummies.get(2), "FH04069");

        testUpdatingMajor(targetUniversityCode, targetMajorCode, newMajor);
    }

    @Test
    public void updateNewMajorTest3() {
        String targetUniversityCode = "HYU";
        String targetMajorCode = "FH04069";
        MajorDto newMajor = new MajorDto(
                DummyDataSet.universityDummies.get(2), "FH04068");

        testUpdatingMajor(targetUniversityCode, targetMajorCode, newMajor);
    }

    @Test
    public void updateNewMajorFailByConflictExceptionTest() {
        String targetUniversityCode = "HYU";
        String targetMajorCode = "FH04069";
        MajorDto newMajor = new MajorDto(
                DummyDataSet.universityDummies.get(1), "FH04068");

        TestHelper.exceptionTest(
                () -> majorDao.update(targetUniversityCode, targetMajorCode, newMajor),
                ConflictException.class
        );
    }

    @Test
    public void updateNewMajorFailByNotFoundExceptionTest() {
        String targetUniversityCode = "HYU";
        String targetMajorCode = "FH04072";
        MajorDto newMajor = new MajorDto(
                DummyDataSet.universityDummies.get(1), "FH04080");

        TestHelper.exceptionTest(
                () -> majorDao.update(targetUniversityCode, targetMajorCode, newMajor),
                NotFoundException.class
        );
    }

    private void testCreatingMajor(MajorDto newMajor) {
        TestHelper.objectCompareTest(
                () -> majorDao.create(newMajor),
                newMajor
        );

        TestHelper.listCompareTest(
                () -> majorDao.getAllList(),
                majorFakeDao.dummyStatusAfterCreate(newMajor)
        );
    }

    private void testUpdatingMajor(String targetUniversityCode, String targetMajorCode, MajorDto newMajor) {
        TestHelper.objectCompareTest(
                () -> majorDao.update(targetUniversityCode, targetMajorCode, newMajor),
                newMajor
        );

        MajorDto targetOnDummy =
                getMajorDummy(targetUniversityCode, targetMajorCode);
        TestHelper.listCompareTest(
                () -> majorDao.getAllList(),
                majorFakeDao.dummyStatusAfterUpdate(targetOnDummy, newMajor)
        );
    }
}