package com.example.beliemeserver.model;

import com.example.beliemeserver.exception.ForbiddenException;
import com.example.beliemeserver.exception.NotFoundException;
import com.example.beliemeserver.model.dto.AuthorityDto;
import com.example.beliemeserver.model.dto.DepartmentDto;
import com.example.beliemeserver.model.dto.UserDto;
import com.example.beliemeserver.model.service.UserService;
import com.example.beliemeserver.util.RandomFilter;
import com.example.beliemeserver.util.TestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest extends BaseServiceTest {
    @InjectMocks
    private UserService userService;

    @Nested
    @DisplayName("getListByDepartment")
    public final class TestGetListByDepartment extends BaseNestedTestWithDept {
        @Override
        protected void setUpDefault() {
            setDept(randomDept());
            setRequester(randomUserHaveAuthOnDept(dept, AuthorityDto.Permission.MASTER));
        }

        @Override
        protected List<UserDto> execMethod() {
            return userService.getListByDepartment(userToken, univCode, deptCode);
        }

        @Override
        protected void setRequesterAccessDenied() {
            setRequester(randomUserHaveLessAuthOnDept(dept, AuthorityDto.Permission.MASTER));
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(userDao.getAllList()).thenReturn(stub.ALL_USERS);

            List<UserDto> expected = new ArrayList<>();
            for(UserDto user : stub.ALL_USERS) {
                if(user.getMaxPermission(dept).hasMorePermission(AuthorityDto.Permission.USER))
                    expected.add(user);
            }

            TestHelper.listCompareTest(this::execMethod, expected);
        }

        private DepartmentDto randomDept() {
            RandomFilter<DepartmentDto> randomFilter = RandomFilter.makeInstance(
                    stub.ALL_DEPTS, (dept) -> true);
            return randomFilter.get().orElse(null);
        }
    }

    @Nested
    @DisplayName("getByIndex()")
    public final class TestGetByIndex extends UserNestedTest {
        private UserDto target;
        private String targetUnivCode;
        private String targetStudentId;

        @Override
        protected void setUpDefault() {
            setRequester(randomDevUser());
            setTarget(randomUser());
        }

        private void setTarget(UserDto user) {
            this.target = user;
            this.targetUnivCode = user.university().code();
            this.targetStudentId = user.studentId();
        }

        @Override
        protected UserDto execMethod() {
            return userService.getByIndex(userToken, targetUnivCode, targetStudentId);
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[개발자는 모든 `User`에 접근 가능하다]_[-]")
        public void SUCCESS_devAccess() {
            setUpDefault();

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(userDao.getByIndex(targetUnivCode, targetStudentId))
                    .thenReturn(target);

            TestHelper.objectCompareTest(this::execMethod, target);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[해당 `index`에 `User`가 존재하지 않을 시]_[NotFoundException]")
        public void ERROR_departmentNotFound_NotFoundException() {
            setUpDefault();

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(userDao.getByIndex(targetUnivCode, targetStudentId))
                    .thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(this::execMethod, NotFoundException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[권한이 없을 시]_[ForbiddenException]")
        public void ERROR_accessDenied_ForbiddenException() {
            setUpDefault();
            setRequester(randomNonDevUser());

            when(userDao.getByToken(userToken)).thenReturn(requester);

            TestHelper.exceptionTest(this::execMethod, ForbiddenException.class);
        }
    }

    @Nested
    @DisplayName("getByToken()")
    public final class TestGetByToken extends UserNestedTest {
        @Override
        protected void setUpDefault() {
            setRequester(randomUser());
        }

        @Override
        protected UserDto execMethod() {
            return userService.getByToken(userToken);
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setUpDefault();

            when(userDao.getByToken(userToken)).thenReturn(requester);

            TestHelper.objectCompareTest(this::execMethod, requester);
        }
    }

    private abstract class UserNestedTest extends BaseNestedTest {
        protected UserDto randomUser() {
            RandomFilter<UserDto> randomFilter = RandomFilter.makeInstance(stub.ALL_USERS, (user) -> true);
            return randomFilter.get().orElse(null);
        }
    }
}
