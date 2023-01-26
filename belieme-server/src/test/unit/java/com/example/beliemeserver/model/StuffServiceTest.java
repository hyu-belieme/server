package com.example.beliemeserver.model;

import com.example.beliemeserver.exception.*;
import com.example.beliemeserver.model.dto.DepartmentDto;
import com.example.beliemeserver.model.dto.StuffDto;
import com.example.beliemeserver.model.dto.UserDto;
import com.example.beliemeserver.model.service.StuffService;
import com.example.beliemeserver.util.TestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static com.example.beliemeserver.util.StubHelper.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StuffServiceTest extends BaseServiceTest {
    @InjectMocks
    private StuffService stuffService;

    @Nested
    @DisplayName("getListByDepartment()")
    public final class TestGetListByDepartment extends BaseNestedTestClass {
        private List<StuffDto> expected;

        @Override
        protected void setUpDefault() {
            department = HYU_CSE_DEPT;
            universityCode = department.university().code();
            departmentCode = department.code();

            requester = HYU_CSE_NORMAL_2_USER;

            expected = getStuffListByDepartmentFromStub(department);
        }

        @Override
        protected List<StuffDto> execMethod() {
            return stuffService.getListByDepartment(userToken, universityCode, departmentCode);
        }

        @Test
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setUpDefault();
            when(departmentDao.getDepartmentByUniversityCodeAndDepartmentCodeData(universityCode, departmentCode))
                    .thenReturn(department);
            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(stuffDao.getListByDepartment(universityCode, departmentCode))
                    .thenReturn(expected);

            TestHelper.listCompareTest(
                    this::execMethod,
                    expected
            );
        }

        private List<StuffDto> getStuffListByDepartmentFromStub(DepartmentDto department) {
            List<StuffDto> output = new ArrayList<>();
            for(StuffDto stuff : ALL_STUFFS) {
                if(stuff.department().matchUniqueKey(department)) {
                    output.add(stuff);
                }
            }
            return output;
        }
    }

    @Nested
    @DisplayName("getByIndex()")
    public class TestGetByIndex extends BaseNestedTestClass {
        private StuffDto stuff;
        private String stuffName;

        @Override
        protected void setUpDefault() {
            department = HYU_CSE_DEPT;
            universityCode = department.university().code();
            departmentCode = department.code();

            requester = HYU_CSE_NORMAL_1_USER;

            stuff = ALL_STUFFS.get(0);
            stuffName = stuff.name();
        }

        @Override
        protected StuffDto execMethod() {
            return stuffService.getByIndex(userToken, universityCode, departmentCode, stuffName);
        }

        @Test
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setUpDefault();

            when(departmentDao.getDepartmentByUniversityCodeAndDepartmentCodeData(universityCode, departmentCode))
                    .thenReturn(department);
            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(stuffDao.getByIndex(universityCode, departmentCode, stuffName))
                    .thenReturn(stuff);

            TestHelper.objectCompareTest(
                    this::execMethod,
                    stuff
            );
        }

        @Test
        @DisplayName("[ERROR]_[해당 `index`에 `stuff`가 존재하지 않을 시]_[NotFoundException")
        public void ERROR_stuffNotFound_NotFoundException() {
            setUpDefault();

            when(departmentDao.getDepartmentByUniversityCodeAndDepartmentCodeData(universityCode, departmentCode))
                    .thenReturn(department);
            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(stuffDao.getByIndex(universityCode, departmentCode, stuffName))
                    .thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(
                    this::execMethod,
                    NotFoundException.class
            );
        }
    }

    private abstract class BaseNestedTestClass {
        protected DepartmentDto department;
        protected String universityCode;
        protected String departmentCode;

        protected UserDto requester;

        protected abstract void setUpDefault();
        protected abstract Object execMethod();

        protected void setRequesterAccessDenied1() {
            requester = HYU_CSE_BANNED_USER;
        }

        protected void setRequesterAccessDenied2() {
            requester = CKU_DUMMY_USER_2;
        }

        @Test
        @DisplayName("[ERROR]_[해당 `index`의 `department`가 존재하지 않을 시]_[InvalidIndexException]")
        public void ERROR_getInvalidIndex_InvalidIndexException() {
            setUpDefault();

            when(departmentDao.getDepartmentByUniversityCodeAndDepartmentCodeData(universityCode, departmentCode))
                    .thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
        }

        @Test
        @DisplayName("[ERROR]_[토큰이 검증되지 않을 시]_[UnauthorizedException]")
        public void ERROR_isUnauthorizedToken_UnauthorizedException() {
            setUpDefault();

            when(departmentDao.getDepartmentByUniversityCodeAndDepartmentCodeData(universityCode, departmentCode))
                    .thenReturn(department);
            when(userDao.getByToken(userToken)).thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(this::execMethod, UnauthorizedException.class);
        }

        @Test
        @DisplayName("[ERROR]_[권한이 없을 시]_[ForbiddenException]")
        public void ERROR_accessDenied_ForbiddenException() {
            setUpDefault();
            setRequesterAccessDenied1();

            when(departmentDao.getDepartmentByUniversityCodeAndDepartmentCodeData(universityCode, departmentCode))
                    .thenReturn(department);
            when(userDao.getByToken(userToken)).thenReturn(requester);

            TestHelper.exceptionTest(this::execMethod, ForbiddenException.class);
        }

        @Test
        @DisplayName("[ERROR]_[`requester`가 다른 학과의 권한만 갖고 있을 시]_[ForbiddenException]")
        public void ERROR_accessDenied2_ForbiddenException() {
            setUpDefault();
            setRequesterAccessDenied2();

            when(departmentDao.getDepartmentByUniversityCodeAndDepartmentCodeData(universityCode, departmentCode))
                    .thenReturn(department);
            when(userDao.getByToken(userToken)).thenReturn(requester);

            TestHelper.exceptionTest(this::execMethod, ForbiddenException.class);
        }
    }
}
