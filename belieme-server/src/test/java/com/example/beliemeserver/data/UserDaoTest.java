package com.example.beliemeserver.data;

import com.example.beliemeserver.util.TestHelper;
import com.example.beliemeserver.model.dao.UserDao;
import com.example.beliemeserver.model.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
                        (record) -> targetUniversityCode.equals(record.getUniversity().getCode())
                )
        );
    }

    @Test
    public void getByTokenTest() {
        String token = "";
        TestHelper.objectCompareTest(
                () -> userDao.getByToken(token),
                userFakeDao.getFirstByCondition(
                        (record) -> token.equals(record.getToken())
                )
        );
    }

    @Test
    public void getByIndexTest() {
        String universityCode = "";
        String studentId = "";

        TestHelper.objectCompareTest(
                () -> userDao.getByIndex(universityCode, studentId),
                getUserDummy(universityCode, studentId)
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
    public void updateTest() {
        String targetUniversityCode = "HYU";
        String targetStudentId = "202212345678";

        UserDto newUser = UserDto.init(
                getUniversityDummy(""),
                "",
                "홍길동"
        );

        testUpdatingUser(targetUniversityCode, targetStudentId, newUser);
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
