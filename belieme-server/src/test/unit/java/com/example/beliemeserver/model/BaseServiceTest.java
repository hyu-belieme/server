package com.example.beliemeserver.model;

import com.example.beliemeserver.config.initdata.InitialData;
import com.example.beliemeserver.error.exception.NotFoundException;
import com.example.beliemeserver.error.exception.UnauthorizedException;
import com.example.beliemeserver.model.dao.*;
import com.example.beliemeserver.model.dto.*;
import com.example.beliemeserver.model.exception.IndexInvalidException;
import com.example.beliemeserver.model.exception.PermissionDeniedException;
import com.example.beliemeserver.model.exception.TokenExpiredException;
import com.example.beliemeserver.model.service.BaseService;
import com.example.beliemeserver.util.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public abstract class BaseServiceTest {
    protected StubWithInitialData stub = new StubWithInitialData();

    @Mock
    protected InitialData initialData;

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

    protected abstract class BaseNestedTest {
        protected final String userToken = "";

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

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[토큰이 검증되지 않을 시]_[UnauthorizedException]")
        public void ERROR_isUnauthorizedToken_UnauthorizedException() {
            setUpDefault();

            when(userDao.getByToken(userToken)).thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(this::execMethod, UnauthorizedException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[토큰이 만료되었을 시]_[ExpiredTokenException]")
        public void ERROR_isExpiredToken_ExpiredTokenException() {
            setUpDefault();
            long newApprovalTimestamp = requester.approvalTimeStamp() - BaseService.TOKEN_EXPIRED_TIME - 10;

            when(userDao.getByToken(userToken)).thenReturn(requester.withApprovalTimeStamp(newApprovalTimestamp));

            TestHelper.exceptionTest(this::execMethod, TokenExpiredException.class);
        }
    }

    protected abstract class BaseNestedTestWithDept extends BaseNestedTest {
        protected DepartmentDto dept;
        protected String univCode;
        protected String deptCode;

        protected abstract void setUpDefault();

        protected abstract Object execMethod();

        protected void setDept(DepartmentDto dept) {
            this.dept = dept;
            this.univCode = dept.university().code();
            this.deptCode = dept.code();
        }

        protected void setRequesterAccessDenied() {
            requester = randomUserHaveLessPermissionOnDept(dept, Permission.USER);
        }

        protected void mockDepartmentAndRequester() {
            when(departmentDao.getByIndex(univCode, deptCode))
                    .thenReturn(dept);
            when(userDao.getByToken(userToken)).thenReturn(requester);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[해당 `index`의 `department`가 존재하지 않을 시]_[InvalidIndexException]")
        public void ERROR_getInvalidIndex_InvalidIndexException() {
            setUpDefault();

            when(departmentDao.getByIndex(univCode, deptCode))
                    .thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(this::execMethod, IndexInvalidException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[토큰이 검증되지 않을 시]_[UnauthorizedException]")
        public void ERROR_isUnauthorizedToken_UnauthorizedException() {
            setUpDefault();

            when(departmentDao.getByIndex(univCode, deptCode))
                    .thenReturn(dept);
            when(userDao.getByToken(userToken)).thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(this::execMethod, UnauthorizedException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[권한이 없을 시]_[PermissionDeniedException]")
        public void ERROR_accessDenied_PermissionDeniedException() {
            setUpDefault();
            setRequesterAccessDenied();

            mockDepartmentAndRequester();

            TestHelper.exceptionTest(this::execMethod, PermissionDeniedException.class);
        }
    }

    protected RandomGetter<UniversityDto> allUnivs() {
        return new RandomGetter<>(stub.ALL_UNIVS);
    }

    protected RandomGetter<MajorDto> allMajors() {
        return new RandomGetter<>(stub.ALL_MAJORS);
    }

    protected RandomGetter<DepartmentDto> allDepts() {
        return new RandomGetter<>(stub.ALL_DEPTS);
    }

    protected RandomGetter<Permission> allPermissions() {
        return new RandomGetter<>(stub.ALL_PERMISSIONS);
    }

    protected RandomGetter<UserDto> allUsers() {
        return new RandomGetter<>(stub.ALL_USERS);
    }

    protected RandomGetter<StuffDto> allStuffs() {
        return new RandomGetter<>(stub.ALL_STUFFS);
    }

    protected RandomGetter<ItemDto> allItems() {
        return new RandomGetter<>(stub.ALL_ITEMS);
    }

    protected RandomGetter<HistoryDto> allHistories() {
        return new RandomGetter<>(stub.ALL_HISTORIES);
    }

    protected <T> RandomGetter<T> withExclude(RandomGetter<T> rs, List<T> exclude) {
        return rs.filter((element) -> !exclude.contains(element));
    }

    protected RandomGetter<UserDto> devs(RandomGetter<UserDto> rs) {
        return rs.filter(UserDto::isDeveloper);
    }

    protected RandomGetter<UserDto> usersNotDev(RandomGetter<UserDto> rs) {
        return rs.filter((user) -> !user.isDeveloper());
    }

    protected RandomGetter<UserDto> usersHaveLessPermissionOnDept(RandomGetter<UserDto> rs, DepartmentDto dept, Permission permission) {
        return rs.filter((user) -> !user.getMaxPermission(dept).hasMorePermission(permission));
    }

    protected RandomGetter<UserDto> usersHaveMorePermissionOnDept(RandomGetter<UserDto> rs, DepartmentDto dept, Permission permission) {
        return rs.filter((user) -> user.getMaxPermission(dept).hasMorePermission(permission));
    }

    protected RandomGetter<UserDto> usersHaveExactPermissionOnDept(RandomGetter<UserDto> rs, DepartmentDto dept, Permission permission) {
        return rs.filter((user) -> user.getMaxPermission(dept) == permission);
    }

    protected RandomGetter<StuffDto> stuffsOnDept(RandomGetter<StuffDto> rs, DepartmentDto dept) {
        return rs.filter((stuff) -> stuff.department().matchUniqueKey(dept));
    }

    protected RandomGetter<ItemDto> itemsOnDept(RandomGetter<ItemDto> rs, DepartmentDto dept) {
        return rs.filter((item) -> item.stuff().department().matchUniqueKey(dept));
    }

    protected RandomGetter<HistoryDto> historiesOnDept(RandomGetter<HistoryDto> rs, DepartmentDto dept) {
        return rs.filter((history) -> history.item().stuff().department().matchUniqueKey(dept));
    }

    protected <T> T randomSelectAndLog(RandomGetter<T> rs) {
        T output = rs.randomSelect();
        System.out.println(output);
        return output;
    }

    protected DepartmentDto randomDept() {
        return randomSelectAndLog(allDepts());
    }

    protected UserDto randomUser() {
        return randomSelectAndLog(allUsers());
    }

    protected UserDto randomDevUser() {
        return randomSelectAndLog(devs(allUsers()));
    }

    protected UserDto randomNonDevUser() {
        return randomSelectAndLog(usersNotDev(allUsers()));
    }

    protected UserDto randomUserHaveMorePermissionOnDept(DepartmentDto dept, Permission permission) {
        return randomSelectAndLog(
                usersHaveMorePermissionOnDept(allUsers(), dept, permission));
    }

    protected UserDto randomUserHaveLessPermissionOnDept(DepartmentDto dept, Permission permission) {
        return randomSelectAndLog(
                usersHaveLessPermissionOnDept(allUsers(), dept, permission));
    }

    protected UserDto randomUserHaveExactPermissionOnDept(DepartmentDto dept, Permission permission) {
        return randomSelectAndLog(
                usersHaveExactPermissionOnDept(allUsers(), dept, permission));
    }

    protected StuffDto randomStuffOnDept(DepartmentDto dept) {
        return randomSelectAndLog(stuffsOnDept(allStuffs(), dept));
    }

    protected ItemDto randomItemOnDept(DepartmentDto dept) {
        return randomSelectAndLog(itemsOnDept(allItems(), dept));
    }

    protected HistoryDto randomHistoryOnDept(DepartmentDto dept) {
        return randomSelectAndLog(historiesOnDept(allHistories(), dept));
    }
}
