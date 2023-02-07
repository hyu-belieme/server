package com.example.beliemeserver.model;

import com.example.beliemeserver.exception.ForbiddenException;
import com.example.beliemeserver.exception.InvalidIndexException;
import com.example.beliemeserver.exception.NotFoundException;
import com.example.beliemeserver.exception.UnauthorizedException;
import com.example.beliemeserver.model.dao.*;
import com.example.beliemeserver.model.dto.*;
import com.example.beliemeserver.util.RandomFilter;
import com.example.beliemeserver.util.RandomGetter;
import com.example.beliemeserver.util.StubData;
import com.example.beliemeserver.util.TestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

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

        private<T> T returnAfterLog(T output) {
            System.out.println(output);
            return output;
        }

        protected DepartmentDto randomDept() {
            return returnAfterLog(allDepts().randomSelect());
        }

        protected UserDto randomUser() {
            return returnAfterLog(allUsers().randomSelect());
        }

        protected UserDto randomDevUser() {
            return returnAfterLog(devs(allUsers()).randomSelect());
        }

        protected UserDto randomNonDevUser() {
            return returnAfterLog(usersNotDev(allUsers()).randomSelect());
        }

        protected UserDto randomUserMoreHaveAuthOnDept(DepartmentDto dept, AuthorityDto.Permission permission) {
            return returnAfterLog(
                    usersHaveMorePermissionOnDept(allUsers(), dept, permission).randomSelect()
            );
        }

        protected UserDto randomUserHaveLessAuthOnDept(DepartmentDto dept, AuthorityDto.Permission permission) {
            return returnAfterLog(
                    usersHaveLessPermissionOnDept(allUsers(), dept, permission).randomSelect());
        }

        protected UserDto randomUserHaveExactAuthOnDept(DepartmentDto dept, AuthorityDto.Permission permission) {
            return returnAfterLog(
                    usersHaveExactPermissionOnDept(allUsers(), dept, permission).randomSelect()
            );
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
            requester = randomUserHaveLessAuthOnDept(dept, AuthorityDto.Permission.USER);
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

            TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
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
        @DisplayName("[ERROR]_[권한이 없을 시]_[ForbiddenException]")
        public void ERROR_accessDenied_ForbiddenException() {
            setUpDefault();
            setRequesterAccessDenied();

            mockDepartmentAndRequester();

            TestHelper.exceptionTest(this::execMethod, ForbiddenException.class);
        }

        protected UserDto randomUserMoreHaveAuthOnDept(DepartmentDto dept, AuthorityDto.Permission permission) {
            RandomFilter<UserDto> randomFilter = RandomFilter.makeInstance(stub.ALL_USERS,
                    (user) -> user.getMaxPermission(dept).hasMorePermission(permission));
            UserDto output = randomFilter.get().orElse(null);
            System.out.println(output);
            return output;
        }

        protected UserDto randomUserHaveLessAuthOnDept(DepartmentDto dept, AuthorityDto.Permission permission) {
            RandomFilter<UserDto> randomFilter = RandomFilter.makeInstance(stub.ALL_USERS,
                    (user) -> !user.getMaxPermission(dept).hasMorePermission(permission));
            UserDto output = randomFilter.get().orElse(null);
            System.out.println(output);
            return output;
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

    protected RandomGetter<AuthorityDto.Permission> allPermissions() {
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

    protected RandomGetter<UserDto> usersHaveLessPermissionOnDept(RandomGetter<UserDto> rs, DepartmentDto dept, AuthorityDto.Permission permission) {
        return rs.filter((user) -> !user.getMaxPermission(dept).hasMorePermission(permission));
    }

    protected RandomGetter<UserDto> usersHaveMorePermissionOnDept(RandomGetter<UserDto> rs, DepartmentDto dept, AuthorityDto.Permission permission) {
        return rs.filter((user) -> user.getMaxPermission(dept).hasMorePermission(permission));
    }

    protected RandomGetter<UserDto> usersHaveExactPermissionOnDept(RandomGetter<UserDto> rs, DepartmentDto dept, AuthorityDto.Permission permission) {
        return rs.filter((user) -> user.getMaxPermission(dept) == permission);
    }
}
