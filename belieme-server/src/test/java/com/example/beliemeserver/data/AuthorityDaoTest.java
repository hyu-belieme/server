package com.example.beliemeserver.data;

import com.example.beliemeserver.model.dao.AuthorityDao;
import com.example.beliemeserver.model.dto.AuthorityDto;
import com.example.beliemeserver.util.DummyDataSet;
import com.example.beliemeserver.util.TestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AuthorityDaoTest extends DaoTest {
    @Autowired
    private AuthorityDao authorityDao;

    @Test
    public void getAllListTest() {
        TestHelper.listCompareTest(
                () -> authorityDao.getAllList(),
                authorityFakeDao.getAll()
        );
    }

    @Test
    public void createTest() {
        // TODO set good new data
        AuthorityDto newAuthority = new AuthorityDto(
                DummyDataSet.userDummies.get(0),
                DummyDataSet.departmentDummies.get(0),
                AuthorityDto.Permission.STAFF
        );

        testCreatingAuthority(newAuthority);
    }

    @Test
    public void updateTest() {
        String universityCodeForUser = "";
        String studentId = "";
        String universityCodeForDepartment = "";
        String departmentCode = "";

        // TODO set good new data
        AuthorityDto newAuthority = new AuthorityDto(
                DummyDataSet.userDummies.get(0),
                DummyDataSet.departmentDummies.get(0),
                AuthorityDto.Permission.STAFF
        );

        testUpdatingUser(universityCodeForUser, studentId, universityCodeForDepartment, departmentCode, newAuthority);
    }

    private void testCreatingAuthority(AuthorityDto newAuthority) {
        TestHelper.objectCompareTest(
                () -> authorityDao.create(newAuthority),
                newAuthority
        );

        TestHelper.listCompareTest(
                () -> authorityDao.getAllList(),
                authorityFakeDao.dummyStatusAfterCreate(newAuthority)
        );
    }

    private void testUpdatingUser(
            String universityCodeForUser, String studentId, String universityCodeForDepartment,
            String departmentCode, AuthorityDto newAuthority) {
        TestHelper.objectCompareTest(
                () -> authorityDao.update(universityCodeForUser, studentId, universityCodeForDepartment, departmentCode, newAuthority),
                newAuthority
        );

        AuthorityDto targetOnDummy =
                getAuthorityDummy(universityCodeForUser, studentId, universityCodeForDepartment, departmentCode);
        TestHelper.listCompareTest(
                () -> authorityDao.getAllList(),
                authorityFakeDao.dummyStatusAfterUpdate(targetOnDummy, newAuthority)
        );
    }
}
