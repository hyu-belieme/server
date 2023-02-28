package com.example.beliemeserver.data;

import com.example.beliemeserver.exception.ConflictException;
import com.example.beliemeserver.exception.NotFoundException;
import com.example.beliemeserver.model.dao.UniversityDao;
import com.example.beliemeserver.model.dto.UniversityDto;
import com.example.beliemeserver.util.TestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UniversityDaoTest extends DaoTest {
    @Autowired
    private UniversityDao universityDao;

    @Test
    public void getAllUniversitiesTest() {
        TestHelper.listCompareTest(
                () -> universityDao.getAllList(),
                universityFakeDao.getAll()
        );
    }

    @Test
    public void getUniversityByCodeTest() {
        String targetUniversityCode = "CKU";
        TestHelper.objectCompareTest(
                () -> universityDao.getByIndex(targetUniversityCode),
                getUniversityDummy(targetUniversityCode)
        );
    }

    @Test
    public void getUniversityByCodeFailTest() {
        String wrongCode = "HANYANG";
        TestHelper.exceptionTest(
                () -> universityDao.getByIndex(wrongCode),
                NotFoundException.class
        );
    }

    @Test
    public void createNewUniversityTest() {
        UniversityDto newUniversity = new UniversityDto("KNU", "강원대학교", null);

        testCreatingUniversity(newUniversity);
    }

    @Test
    public void createNewUniversityFailTest() {
        UniversityDto newUniversity = new UniversityDto("HYU", "한양대학교", null);

        TestHelper.exceptionTest(
                () -> universityDao.create(newUniversity),
                ConflictException.class
        );
    }

    @Test
    public void updateNewUniversityWithSameCodeTest() {
        String targetUnivCode = "HYU";
        UniversityDto newUniversity = new UniversityDto("HYU", "한양대학교", null);

        testUpdatingUniversity(targetUnivCode, newUniversity);
    }

    @Test
    public void updateNewUniversityWithCodeChangeTest() {
        String targetUnivCode = "HYU";
        UniversityDto newUniversity = new UniversityDto("HYU-ERICA", "한양대학교 에리카", null);

        testUpdatingUniversity(targetUnivCode, newUniversity);
    }

    @Test
    public void updateNewUniversityFailByNotFoundExceptionTest() {
        UniversityDto newUniversity = new UniversityDto("HYU", "한양대학교", null);

        TestHelper.exceptionTest(
                () -> universityDao.update("HYU2", newUniversity),
                NotFoundException.class
        );
    }

    @Test
    public void updateNewUniversityFailByConflictExceptionTest() {
        UniversityDto newUniversity = new UniversityDto("HYU", "한양대학교", null);

        TestHelper.exceptionTest(
                () -> universityDao.update("SNU", newUniversity),
                ConflictException.class
        );
    }

    private void testCreatingUniversity(UniversityDto newUniversity) {
        TestHelper.objectCompareTest(
                () -> universityDao.create(newUniversity),
                newUniversity
        );

        TestHelper.listCompareTest(
                () -> universityDao.getAllList(),
                universityFakeDao.dummyStatusAfterCreate(newUniversity)
        );
    }

    private void testUpdatingUniversity(String targetUniversityCode, UniversityDto newUniversity) {
        TestHelper.objectCompareTest(
                () -> universityDao.update(targetUniversityCode, newUniversity),
                newUniversity
        );

        UniversityDto targetOnDummy =
                getUniversityDummy(targetUniversityCode);
        TestHelper.listCompareTest(
                () -> universityDao.getAllList(),
                universityFakeDao.dummyStatusAfterUpdate(targetOnDummy, newUniversity)
        );
    }
}