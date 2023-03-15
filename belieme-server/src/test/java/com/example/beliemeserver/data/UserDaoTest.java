package com.example.beliemeserver.data;

import com.example.beliemeserver.domain.dao.UserDao;
import com.example.beliemeserver.domain.dto.AuthorityDto;
import com.example.beliemeserver.domain.dto.UserDto;
import com.example.beliemeserver.error.exception.ConflictException;
import com.example.beliemeserver.error.exception.NotFoundException;
import com.example.beliemeserver.util.DummyDataSet;
import com.example.beliemeserver.util.TestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserDaoTest extends DaoTest {
    @Autowired
    private UserDao userDao;

    @Test
    public void getAllListTest() {
        TestHelper.listCompareTest(() -> userDao.getAllList(), userFakeDao.getAll());
    }

    @Test
    public void getListByUniversityTest() {
        String targetUniversityCode = "HYU";
        TestHelper.listCompareTest(
                () -> userDao.getListByUniversity(targetUniversityCode),
                userFakeDao.getAllByCondition(
                        (record) -> targetUniversityCode.equals(record.university().code())
                )
        );
    }

    @Test
    public void getByTokenTest() {
        String token = "TEST1";
        TestHelper.objectCompareTest(
                () -> userDao.getByToken(token),
                userFakeDao.getFirstByCondition(
                        (record) -> token.equals(record.token())
                )
        );
    }

    @Test
    public void getByTokenFailByNotFound() {
        String token = "NOT-FOUND";
        TestHelper.exceptionTest(
                () -> userDao.getByToken(token),
                NotFoundException.class
        );
    }

    @Test
    public void getByIndexTest() {
        String universityCode = "HYU";
        String studentId = "2018008886";

        TestHelper.objectCompareTest(
                () -> userDao.getByIndex(universityCode, studentId),
                getUserDummy(universityCode, studentId)
        );
    }

    @Test
    public void getByIndexFailByNotFound() {
        String universityCode = "HYU";
        String studentId = "12341234";
        TestHelper.exceptionTest(
                () -> userDao.getByIndex(universityCode, studentId),
                NotFoundException.class
        );
    }

    @Test
    public void createTest() {
        UserDto newUser = UserDto.init(
                getUniversityDummy("HYU"),
                "202212345678",
                "홍길동"
        );

        testCreatingUser(newUser);
    }

    @Test
    public void createFailByNotFound() {
        UserDto newUser = UserDto.init(
                DummyDataSet.notFoundUniversity,
                "202212345678",
                "홍길동"
        );

        TestHelper.exceptionTest(
                () -> userDao.create(newUser),
                NotFoundException.class
        );
    }

    @Test
    public void createFailByConflict() {
        UserDto newUser = UserDto.init(
                getUniversityDummy("HYU"),
                "2018008886",
                "홍길동"
        );

        TestHelper.exceptionTest(
                () -> userDao.create(newUser),
                ConflictException.class
        );
    }

    @Test
    public void updateNameOnlyTest() {
        String targetUniversityCode = "HYU";
        String targetStudentId = "2018008886";

        List<AuthorityDto> authorities = List.of(
                DummyDataSet.authorityDummies.get(0),
                DummyDataSet.authorityDummies.get(1),
                DummyDataSet.authorityDummies.get(2)
        );

        UserDto newUser = new UserDto(
                getUniversityDummy("HYU"),
                "2018008886",
                "홍길동",
                "TEST1",
                1673155358,
                1673155358,
                authorities
        );

        testUpdatingUser(targetUniversityCode, targetStudentId, newUser);
    }

    @Test
    public void updateTokenOnlyTest() {
        String targetUniversityCode = "HYU";
        String targetStudentId = "2018008886";

        List<AuthorityDto> authorities = List.of(
                DummyDataSet.authorityDummies.get(0),
                DummyDataSet.authorityDummies.get(1),
                DummyDataSet.authorityDummies.get(2)
        );

        UserDto newUser = new UserDto(
                getUniversityDummy("HYU"),
                "2018008886",
                "이석환",
                "TEST10",
                1673155358,
                1673155358,
                authorities
        );

        testUpdatingUser(targetUniversityCode, targetStudentId, newUser);
    }

    @Test
    public void updateTokenAndMajorsAndAuthoritiesTest() {
        String targetUniversityCode = "HYU";
        String targetStudentId = "2018008886";

        List<AuthorityDto> authorities = List.of(
                DummyDataSet.authorityDummies.get(0),
                DummyDataSet.authorityDummies.get(1)
        );

        UserDto newUser = new UserDto(
                getUniversityDummy("HYU"),
                "2018008886",
                "이석환",
                "TEST10",
                1673155358,
                1673155358,
                authorities
        );

        testUpdatingUser(targetUniversityCode, targetStudentId, newUser);
    }

    @Test
    public void updateIndexAndOtherInfoTest() {
        String targetUniversityCode = "HYU";
        String targetStudentId = "2018008886";

        List<AuthorityDto> authorities = List.of(
                DummyDataSet.authorityDummies.get(0),
                DummyDataSet.authorityDummies.get(1),
                DummyDataSet.authorityDummies.get(2)
        );

        UserDto newUser = new UserDto(
                getUniversityDummy("HYU"),
                "2018008896",
                "이석환",
                "TEST1",
                1673155356,
                1673155356,
                authorities
        );

        testUpdatingUser(targetUniversityCode, targetStudentId, newUser);
    }

    @Test
    public void updateFailByNotFound() {
        String targetUniversityCode = "HYU";
        String targetStudentId = "2022005678";

        UserDto newUser = UserDto.init(
                DummyDataSet.universityDummies.get(1),
                "2022001234",
                "홍길동"
        );

        TestHelper.exceptionTest(
                () -> userDao.update(targetUniversityCode, targetStudentId, newUser),
                NotFoundException.class
        );
    }

    @Test
    public void updateFailByConflict() {
        String targetUniversityCode = "HYU";
        String targetStudentId = "2018008886";

        UserDto newUser = UserDto.init(
                DummyDataSet.universityDummies.get(1),
                "2018008887",
                "홍길동"
        );

        TestHelper.exceptionTest(
                () -> userDao.update(targetUniversityCode, targetStudentId, newUser),
                ConflictException.class
        );
    }

    private void testCreatingUser(UserDto newUser) {
        TestHelper.objectCompareTest(
                () -> userDao.create(newUser),
                newUser
        );

        TestHelper.listCompareTest(
                () -> userDao.getAllList(),
                userFakeDao.dummyStatusAfterCreate(newUser)
        );
    }

    private void testUpdatingUser(String targetUniversityCode, String targetStudentId, UserDto newUser) {
        TestHelper.objectCompareTest(
                () -> userDao.update(targetUniversityCode, targetStudentId, newUser),
                newUser
        );

        UserDto targetOnDummy =
                getUserDummy(targetUniversityCode, targetStudentId);
        TestHelper.listCompareTest(
                () -> userDao.getAllList(),
                userFakeDao.dummyStatusAfterUpdate(targetOnDummy, newUser)
        );
    }
}
