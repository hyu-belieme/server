package com.example.beliemeserver.model;

import com.example.beliemeserver.exception.ForbiddenException;
import com.example.beliemeserver.exception.InvalidIndexException;
import com.example.beliemeserver.exception.NotFoundException;
import com.example.beliemeserver.exception.UnauthorizedException;
import com.example.beliemeserver.model.dao.*;
import com.example.beliemeserver.model.dto.*;
import com.example.beliemeserver.util.RandomFilter;
import com.example.beliemeserver.util.StubData;
import com.example.beliemeserver.util.TestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public abstract class BaseServiceTest {
    protected StubData stub = new StubData();

    @Mock
    protected UniversityDao universityDao;
    @Mock
    protected DepartmentDao departmentDao;
    @Mock
    protected UserDao userDao;
    @Mock
    protected MajorDao majorDao;
    @Mock
    protected AuthorityDao authorityDao;
    @Mock
    protected StuffDao stuffDao;
    @Mock
    protected ItemDao itemDao;
    @Mock
    protected HistoryDao historyDao;

    public static final String userToken = "";

    protected abstract class BaseNestedTest {
        protected String userToken = "";

        protected UserDto requester;
        protected String requesterUnivCode;
        protected String requesterStudentId;

        protected abstract void setUpDefault();
        protected abstract Object execMethod();

        protected void setRequester(UserDto requester) {
            this.requester = requester;
            this.requesterUnivCode = requester.university().code();
            this.requesterStudentId = requester.studentId();
        }

        protected UserDto randomDevUser() {
            RandomFilter<UserDto> randomFilter = RandomFilter.makeInstance(stub.ALL_USERS,
                    UserDto::isDeveloper);
            return randomFilter.get().orElse(null);
        }

        protected UserDto randomNonDevUser() {
            RandomFilter<UserDto> randomFilter = RandomFilter.makeInstance(stub.ALL_USERS,
                    (user) -> !user.isDeveloper());
            return randomFilter.get().orElse(null);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[토큰이 검증되지 않을 시]_[UnauthorizedException]")
        public void ERROR_isUnauthorizedToken_UnauthorizedException() {
            setUpDefault();

            when(userDao.getByToken(userToken)).thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(this::execMethod, UnauthorizedException.class);
        }
    }

    protected abstract class BaseNestedTestWithDept {
        protected String userToken = "";

        protected DepartmentDto dept;
        protected String univCode;
        protected String deptCode;

        protected UserDto requester;
        protected String requesterUnivCode;
        protected String requesterStudentId;

        protected abstract void setUpDefault();
        protected abstract Object execMethod();

        protected void setDept(DepartmentDto dept) {
            this.dept = dept;
            this.univCode = dept.university().code();
            this.deptCode = dept.code();
        }

        protected void setRequester(UserDto requester) {
            this.requester = requester;
            this.requesterUnivCode = requester.university().code();
            this.requesterStudentId = requester.studentId();
        }

        protected void setRequesterAccessDenied() {
            requester = randomUserByDeptAndAuth(dept, AuthorityDto.Permission.BANNED);
        }

        protected void mockDepartmentAndRequester() {
            when(departmentDao.getDepartmentByUniversityCodeAndDepartmentCodeData(univCode, deptCode))
                    .thenReturn(dept);
            when(userDao.getByToken(userToken)).thenReturn(requester);
        }

        protected UserDto randomUserByDeptAndAuth(DepartmentDto dept, AuthorityDto.Permission permission) {
            RandomFilter<UserDto> randomFilter = RandomFilter.makeInstance(stub.ALL_USERS,
                    (user) -> user.getMaxPermission(dept) == permission);
            return randomFilter.get().orElse(null);
        }

        protected UserDto randomUserByDeptAndAuthWithExclude(DepartmentDto dept, AuthorityDto.Permission permission, UserDto exclude) {
            RandomFilter<UserDto> randomFilter = RandomFilter.makeInstance(stub.ALL_USERS,
                    (user) -> user.getMaxPermission(dept) == permission
                            && !user.matchUniqueKey(exclude));
            return randomFilter.get().orElse(null);
        }

        protected StuffDto randomStuffByDept(DepartmentDto dept) {
            RandomFilter<StuffDto> randomFilter = RandomFilter.makeInstance(stub.ALL_STUFFS,
                    (stuff) -> stuff.department().matchUniqueKey(dept));
            return randomFilter.get().orElse(null);
        }

        protected ItemDto randomItemByDept(DepartmentDto dept) {
            RandomFilter<ItemDto> randomFilter = RandomFilter.makeInstance(stub.ALL_ITEMS,
                    (item) -> item.stuff().department().matchUniqueKey(dept));
            return randomFilter.get().orElse(null);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[해당 `index`의 `department`가 존재하지 않을 시]_[InvalidIndexException]")
        public void ERROR_getInvalidIndex_InvalidIndexException() {
            setUpDefault();

            when(departmentDao.getDepartmentByUniversityCodeAndDepartmentCodeData(univCode, deptCode))
                    .thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[토큰이 검증되지 않을 시]_[UnauthorizedException]")
        public void ERROR_isUnauthorizedToken_UnauthorizedException() {
            setUpDefault();

            when(departmentDao.getDepartmentByUniversityCodeAndDepartmentCodeData(univCode, deptCode))
                    .thenReturn(dept);
            when(userDao.getByToken(userToken)).thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(this::execMethod, UnauthorizedException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[권한이 없을 시]_[ForbiddenException]")
        public void ERROR_accessDenied_ForbiddenException() {
            setUpDefault();
            setRequesterAccessDenied();

            mockDepartmentAndRequester();

            TestHelper.exceptionTest(this::execMethod, ForbiddenException.class);
        }
    }
}
