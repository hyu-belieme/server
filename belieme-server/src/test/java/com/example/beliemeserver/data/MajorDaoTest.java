package com.example.beliemeserver.data;

import com.example.beliemeserver.model.dao.MajorDao;
import com.example.beliemeserver.model.dto.MajorDto;
import com.example.beliemeserver.model.exception.ConflictException;
import com.example.beliemeserver.model.exception.NotFoundException;
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
                ()->majorDao.getAllMajorsData(),
                majorFakeDao.getAll()
        );
    }

    @Test
    public void createNewMajorTest1() {
        MajorDto newMajor = new MajorDto(
                DummyDataSet.universityDummies.get(0),
                "FH04070");

        testCreatingMajor(newMajor);
    }

    @Test
    public void createNewMajorTest2() {
        MajorDto newMajor = new MajorDto(
                DummyDataSet.universityDummies.get(1), "FH04068");

        testCreatingMajor(newMajor);
    }

    @Test
    public void createNewMajorFailByConflictExceptionTest() {
        MajorDto newMajor = new MajorDto(
                DummyDataSet.universityDummies.get(0), "FH04068");

        TestHelper.exceptionTest(
                () -> majorDao.addMajorData(newMajor),
                ConflictException.class
        );
    }

    @Test
    public void createNewMajorFailByNotFoundExceptionTest() {
        MajorDto newMajor = new MajorDto(DummyDataSet.notFoundUniversity, "FH04070");

        TestHelper.exceptionTest(
                () -> majorDao.addMajorData(newMajor),
                NotFoundException.class
        );
    }

    @Test
    public void updateNewMajorTest1() {
        String targetUniversityCode = "HYU";
        String targetMajorCode = "FH04069";
        MajorDto newMajor = new MajorDto(
                DummyDataSet.universityDummies.get(0), "FH04070");

        testUpdatingMajor(targetUniversityCode, targetMajorCode, newMajor);
    }

    @Test
    public void updateNewMajorTest2() {
        String targetUniversityCode = "HYU";
        String targetMajorCode = "FH04069";
        MajorDto newMajor = new MajorDto(
                DummyDataSet.universityDummies.get(1), "FH04069");

        testUpdatingMajor(targetUniversityCode, targetMajorCode, newMajor);
    }

    @Test
    public void updateNewMajorTest3() {
        String targetUniversityCode = "HYU";
        String targetMajorCode = "FH04069";
        MajorDto newMajor = new MajorDto(
                DummyDataSet.universityDummies.get(1), "FH04068");

        testUpdatingMajor(targetUniversityCode, targetMajorCode, newMajor);
    }

    @Test
    public void updateNewMajorFailByConflictExceptionTest() {
        String targetUniversityCode = "HYU";
        String targetMajorCode = "FH04069";
        MajorDto newMajor = new MajorDto(
                DummyDataSet.universityDummies.get(0), "FH04068");

        TestHelper.exceptionTest(
                () -> majorDao.updateMajorData(targetUniversityCode, targetMajorCode, newMajor),
                ConflictException.class
        );
    }

    @Test
    public void updateNewMajorFailByNotFoundExceptionTest() {
        String targetUniversityCode = "HYU";
        String targetMajorCode = "FH04072";
        MajorDto newMajor = new MajorDto(
                DummyDataSet.universityDummies.get(0), "FH04080");

        TestHelper.exceptionTest(
                () -> majorDao.updateMajorData(targetUniversityCode, targetMajorCode, newMajor),
                NotFoundException.class
        );
    }

    private void testCreatingMajor(MajorDto newMajor) {
        TestHelper.objectCompareTest(
                () -> majorDao.addMajorData(newMajor),
                newMajor
        );

        TestHelper.listCompareTest(
                () -> majorDao.getAllMajorsData(),
                majorFakeDao.dummyStatusAfterCreate(newMajor)
        );
    }

    private void testUpdatingMajor(String targetUniversityCode, String targetMajorCode, MajorDto newMajor) {
        TestHelper.objectCompareTest(
                () -> majorDao.updateMajorData(targetUniversityCode, targetMajorCode, newMajor),
                newMajor
        );

        MajorDto targetOnDummy =
                getMajorDummy(targetUniversityCode, targetMajorCode);
        TestHelper.listCompareTest(
                () -> majorDao.getAllMajorsData(),
                majorFakeDao.dummyStatusAfterUpdate(targetOnDummy, newMajor)
        );
    }
}