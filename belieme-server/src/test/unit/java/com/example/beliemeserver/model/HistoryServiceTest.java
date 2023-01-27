package com.example.beliemeserver.model;

import com.example.beliemeserver.exception.ForbiddenException;
import com.example.beliemeserver.exception.InvalidIndexException;
import com.example.beliemeserver.exception.NotFoundException;
import com.example.beliemeserver.exception.UnauthorizedException;
import com.example.beliemeserver.model.dto.DepartmentDto;
import com.example.beliemeserver.model.dto.HistoryDto;
import com.example.beliemeserver.model.dto.UserDto;
import com.example.beliemeserver.model.service.HistoryService;
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
public class HistoryServiceTest extends BaseServiceTest {
    @InjectMocks
    private HistoryService historyService;

    @Nested
    @DisplayName("getListByDepartment")
    public final class TestGetListByDepartment extends BaseNestedTestClass {
        private List<HistoryDto> historyList;

        @Override
        protected void setUpDefault() {
            setUp(HYU_CSE_DEPT, HYU_CSE_STAFF_USER);

            historyList = getHistoryListByDepartmentFromStub();
        }

        @Override
        protected void setRequesterAccessDenied() {
            requester = HYU_CSE_NORMAL_1_USER;
        }

        @Override
        protected List<HistoryDto> execMethod() {
            return historyService.getListByDepartment(userToken, universityCode, departmentCode);
        }

        @Test
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(historyDao.getListByDepartment(universityCode, departmentCode))
                    .thenReturn(historyList);

            TestHelper.listCompareTest(
                    this::execMethod,
                    execMethod()
            );
        }

        private List<HistoryDto> getHistoryListByDepartmentFromStub() {
            List<HistoryDto> output = new ArrayList<>();
            for(HistoryDto history : ALL_HISTORIES) {
                if(department.matchUniqueKey(history.item().stuff().department())) {
                    output.add(history);
                }
            }
            return output;
        }
    }

    @Nested
    @DisplayName("getListByDepartmentAndRequester()")
    public final class TestGetListByDepartmentAndRequester extends BaseNestedTestClass {
        private UserDto historyRequester;

        private List<HistoryDto> historyList;

        @Override
        protected void setUpDefault() {
            setUp(HYU_CSE_DEPT, HYU_CSE_NORMAL_1_USER);

            historyRequester = HYU_CSE_NORMAL_1_USER;
            historyList = getHistoryListByDepartmentAndRequesterFromStub();
        }

        @Override
        protected List<HistoryDto> execMethod() {
            return historyService.getListByDepartmentAndRequester(
                    userToken, universityCode, departmentCode,
                    historyRequester.university().code(),
                    historyRequester.studentId()
            );
        }

        @Test
        @DisplayName("[SUCCESS]_[본인의 `History List`에 대한 `request`일 시]_[-]")
        public void SUCCESS_getHistoryListHerSelf() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(userDao.getByIndex(
                    historyRequester.university().code(), historyRequester.studentId())
            ).thenReturn(historyRequester);
            when(historyDao.getListByDepartmentAndRequester(
                    universityCode, departmentCode,
                    historyRequester.university().code(),
                    historyRequester.studentId())
            ).thenReturn(historyList);

            TestHelper.listCompareTest(this::execMethod, execMethod());
        }

        @Test
        @DisplayName("[SUCCESS]_[타인의 `History List`에 대한 `request`가 아니지만 `requester`가 `staff` 이상의 권한을 가질 시]_[-]")
        public void SUCCESS_getHistoryListOfOthers() {
            setUp(HYU_CSE_DEPT, HYU_CSE_STAFF_USER);
            historyRequester = HYU_CSE_NORMAL_2_USER;
            historyList = getHistoryListByDepartmentAndRequesterFromStub();

            mockDepartmentAndRequester();
            when(userDao.getByIndex(
                    historyRequester.university().code(),
                    historyRequester.studentId())
            ).thenReturn(historyRequester);
            when(historyDao.getListByDepartmentAndRequester(
                    universityCode, departmentCode,
                    historyRequester.university().code(),
                    historyRequester.studentId())
            ).thenReturn(historyList);

            TestHelper.listCompareTest(this::execMethod, execMethod());
        }

        @Test
        @DisplayName("[ERROR]_[권한이 없을 시]_[ForbiddenException]")
        @Override
        public void ERROR_accessDenied_ForbiddenException() {
            setUp(HYU_CSE_DEPT, HYU_CSE_BANNED_USER);
            historyRequester = HYU_CSE_NORMAL_2_USER;

            mockDepartmentAndRequester();
            when(userDao.getByIndex(
                    historyRequester.university().code(),
                    historyRequester.studentId())
            ).thenReturn(historyRequester);

            TestHelper.exceptionTest(this::execMethod, ForbiddenException.class);
        }

        @Test
        @DisplayName("[ERROR]_[본인의 `History List`에 대한 `request`가 아닐 시]_[ForbiddenException]")
        public void ERROR_getHistoryListOfOthers_ForbiddenException() {
            setUp(HYU_CSE_DEPT, HYU_CSE_NORMAL_1_USER);
            historyRequester = HYU_CSE_NORMAL_2_USER;

            mockDepartmentAndRequester();
            when(userDao.getByIndex(
                    historyRequester.university().code(),
                    historyRequester.studentId())
            ).thenReturn(historyRequester);

            TestHelper.exceptionTest(this::execMethod, ForbiddenException.class);
        }

        @Test
        @DisplayName("[ERROR]_[`History Requester`의 `index`가 유효하지 않을 시]_[InvalidException]")
        public void ERROR_userInvalidIndex_InvalidException() {
            setUp(HYU_CSE_DEPT, HYU_CSE_NORMAL_1_USER);
            historyRequester = HYU_CSE_NORMAL_2_USER;

            mockDepartmentAndRequester();
            when(userDao.getByIndex(
                    historyRequester.university().code(),
                    historyRequester.studentId())
            ).thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
        }

        private List<HistoryDto> getHistoryListByDepartmentAndRequesterFromStub() {
            List<HistoryDto> output = new ArrayList<>();
            for(HistoryDto history : ALL_HISTORIES) {
                if(department.matchUniqueKey(history.item().stuff().department())
                        && historyRequester.matchUniqueKey(history.requester()) ) {
                    output.add(history);
                }
            }
            return output;
        }
    }

    private abstract class BaseNestedTestClass {
        protected DepartmentDto department;
        protected String universityCode;
        protected String departmentCode;

        protected UserDto requester;

        protected abstract void setUpDefault();
        protected abstract Object execMethod();

        protected void setUp(DepartmentDto department, UserDto requester) {
            this.department = department;
            this.universityCode = department.university().code();
            this.departmentCode = department.code();

            this.requester = requester;
        }

        protected void setRequesterAccessDenied() {
            requester = HYU_CSE_BANNED_USER;
        }

        protected void mockDepartmentAndRequester() {
            when(departmentDao.getDepartmentByUniversityCodeAndDepartmentCodeData(universityCode, departmentCode))
                    .thenReturn(department);
            when(userDao.getByToken(userToken)).thenReturn(requester);
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
            setRequesterAccessDenied();

            mockDepartmentAndRequester();

            TestHelper.exceptionTest(this::execMethod, ForbiddenException.class);
        }
    }
}
