package com.example.beliemeserver.data;

import com.example.beliemeserver.data.daoimpl._new.NewAuthorityDaoImpl;
import com.example.beliemeserver.data.entity._new.NewAuthorityEntity;
import com.example.beliemeserver.domain.dto._new.AuthorityDto;
import com.example.beliemeserver.domain.dto.enumeration.Permission;
import com.example.beliemeserver.error.exception.ConflictException;
import com.example.beliemeserver.error.exception.InvalidIndexException;
import com.example.beliemeserver.error.exception.NotFoundException;
import com.example.beliemeserver.util.TestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NewAuthorityDaoTest  extends BaseDaoTest {
    @InjectMocks
    private NewAuthorityDaoImpl authDao;

    @Nested
    @DisplayName("getAllList()")
    public class TestGetAllList {
        protected List<AuthorityDto> execMethod() {
            return authDao.getAllList();
        }

        @Test()
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            when(authRepository.findAll()).thenReturn(stub.ALL_AUTHS);
            TestHelper.listCompareTest(this::execMethod, toAuthorityDtoList(stub.ALL_AUTHS));
        }
    }

    @Nested
    @DisplayName("checkExistByIndex()")
    public class TestCheckByIndex {
        private NewAuthorityEntity auth;

        private void setAuth(NewAuthorityEntity auth) {
            this.auth = auth;
        }

        protected boolean execMethod() {
            return authDao.checkExistByIndex(auth.getDepartmentId(), Permission.valueOf(auth.getPermission()));
        }

        @Test()
        @DisplayName("[SUCCESS]_[`index`에 대응하는 `authority`가 존재할 시]_[-]")
        public void SUCCESS_exist() {
            setAuth(randomAuth());
            when(authRepository.existsByDepartmentIdAndPermission(auth.getDepartmentId(), auth.getPermission())).thenReturn(true);

            TestHelper.objectCompareTest(this::execMethod, true);
        }

        @Test()
        @DisplayName("[SUCCESS]_[`index`에 대응하는 `authority`가 존재하자 않을 시]_[-]")
        public void SUCCESS_notExist() {
            setAuth(randomAuth());
            when(authRepository.existsByDepartmentIdAndPermission(auth.getDepartmentId(), auth.getPermission())).thenReturn(false);

            TestHelper.objectCompareTest(this::execMethod, false);
        }
    }

    @Nested
    @DisplayName("create()")
    public class TestCreate {
        private NewAuthorityEntity auth;

        private void setAuth(NewAuthorityEntity auth) {
            this.auth = auth;
        }

        protected AuthorityDto execMethod() {
            return authDao.create(auth.getDepartmentId(), Permission.valueOf(auth.getPermission()));
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setAuth(randomAuth());

            when(deptRepository.findById(auth.getDepartmentId())).thenReturn(Optional.of(auth.getDepartment()));
            when(authRepository.existsByDepartmentIdAndPermission(auth.getDepartmentId(), auth.getPermission())).thenReturn(false);
            when(authRepository.save(mockArg(auth))).thenReturn(auth);

            TestHelper.objectCompareTest(this::execMethod, auth.toAuthorityDto());

            verify(authRepository).save(mockArg(auth));
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`departmentId`에 해당하는 `department`가 존재하지 않을 시]_[InvalidIndexException]")
        public void ERROR_universityNotFound_InvalidIndexException() {
            setAuth(randomAuth());

            when(deptRepository.findById(auth.getDepartmentId())).thenReturn(Optional.empty());

            TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[동일한 `index`를 갖는 `auth`가 존재할 시]_[ConflictException]")
        public void ERROR_IndexConflict_ConflictException() {
            setAuth(randomAuth());

            when(deptRepository.findById(auth.getDepartmentId())).thenReturn(Optional.of(auth.getDepartment()));
            when(authRepository.existsByDepartmentIdAndPermission(auth.getDepartmentId(), auth.getPermission())).thenReturn(true);

            TestHelper.exceptionTest(this::execMethod, ConflictException.class);
        }
    }
}
