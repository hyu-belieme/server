package com.example.beliemeserver.data;

import com.example.beliemeserver.error.exception.ConflictException;
import com.example.beliemeserver.error.exception.NotFoundException;
import com.example.beliemeserver.model.dao.AuthorityDao;
import com.example.beliemeserver.model.dto.AuthorityDto;
import com.example.beliemeserver.model.dto.Permission;
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
        AuthorityDto newAuthority = new AuthorityDto(
                DummyDataSet.departmentDummies.get(5),
                Permission.BANNED
        );

        testCreatingAuthority(newAuthority);
    }

    @Test
    public void createFailByNotFound() {
        AuthorityDto newAuthority = new AuthorityDto(
                DummyDataSet.notFoundDepartment,
                Permission.MASTER
        );

        TestHelper.exceptionTest(
                () -> authorityDao.create(newAuthority),
                NotFoundException.class
        );
    }

    @Test
    public void createFailByConflict() {
        AuthorityDto newAuthority = new AuthorityDto(
                DummyDataSet.departmentDummies.get(3),
                Permission.MASTER
        );

        TestHelper.exceptionTest(
                () -> authorityDao.create(newAuthority),
                ConflictException.class
        );
    }

    @Test
    public void updateTest() {
        String universityCode = "CKU";
        String departmentCode = "MED";
        Permission permission = Permission.STAFF;

        AuthorityDto newAuthority = new AuthorityDto(
                DummyDataSet.departmentDummies.get(5),
                Permission.STAFF
        );

        testUpdatingAuthority(universityCode, departmentCode, permission, newAuthority);
    }

    @Test
    public void updateFailByNotFound() {
        String universityCode = "CKU";
        String departmentCode = "ME";
        Permission permission = Permission.BANNED;
        AuthorityDto newAuthority = new AuthorityDto(
                DummyDataSet.departmentDummies.get(5),
                Permission.STAFF
        );

        TestHelper.exceptionTest(
                () -> authorityDao.update(universityCode, departmentCode, permission, newAuthority),
                NotFoundException.class
        );
    }

    @Test
    public void updateFailByConflict() {
        String universityCode = "CKU";
        String departmentCode = "MED";
        Permission permission = Permission.STAFF;

        AuthorityDto newAuthority = new AuthorityDto(
                DummyDataSet.departmentDummies.get(3),
                Permission.MASTER
        );

        TestHelper.exceptionTest(
                () -> authorityDao.update(universityCode, departmentCode, permission, newAuthority),
                ConflictException.class
        );
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

    private void testUpdatingAuthority(String universityCode, String departmentCode,
                                       Permission permission, AuthorityDto newAuthority) {
        TestHelper.objectCompareTest(
                () -> authorityDao.update(universityCode, departmentCode, permission, newAuthority),
                newAuthority
        );

        AuthorityDto targetOnDummy =
                getAuthorityDummy(universityCode, departmentCode, permission);
        TestHelper.listCompareTest(
                () -> authorityDao.getAllList(),
                authorityFakeDao.dummyStatusAfterUpdate(targetOnDummy, newAuthority)
        );
    }
}
