package com.example.beliemeserver.model;

import com.example.beliemeserver.exception.ForbiddenException;
import com.example.beliemeserver.exception.MethodNotAllowedException;
import com.example.beliemeserver.exception.NotFoundException;
import com.example.beliemeserver.model.dto.AuthorityDto;
import com.example.beliemeserver.model.dto.DepartmentDto;
import com.example.beliemeserver.model.dto.UserDto;
import com.example.beliemeserver.model.service.UserService;
import com.example.beliemeserver.util.RandomGetter;
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
    private final DepartmentDto TEST_DEPT = stub.HYU_CSE_DEPT;

    @InjectMocks
    private UserService userService;

    @Nested
    @DisplayName("getListByDepartment")
    public final class TestGetListByDepartment extends BaseNestedTestWithDept {
        @Override
        protected void setUpDefault() {
            setDept(randomDept());
            setRequester(randomUserHaveMorePermissionOnDept(dept, AuthorityDto.Permission.MASTER));
        }

        @Override
        protected List<UserDto> execMethod() {
            return userService.getListByDepartment(userToken, univCode, deptCode);
        }

        @Override
        protected void setRequesterAccessDenied() {
            setRequester(randomUserHaveLessPermissionOnDept(dept, AuthorityDto.Permission.MASTER));
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

    @Nested
    @DisplayName("updateAuthority()")
    public final class TestUpdateAuthority extends UserNestedTest {
        private UserDto targetUser;
        private String targetUserUnivCode;
        private String targetUserStudentId;

        private DepartmentDto authDept;
        private String authUnivCode;
        private String authDeptCode;
        private AuthorityDto.Permission authPermission;

        @Override
        protected void setUpDefault() {
            setAuthDept(TEST_DEPT);
            setAuthPermission(randomUnderMasterPermission());
            setRequester(randomUserHaveExactPermissionOnDept(authDept, AuthorityDto.Permission.MASTER));
            setTargetUser(randomUsersNotHaveAdditionalAuthOnDept(authDept));
        }

        private void setTargetUser(UserDto user) {
            this.targetUser = user;
            this.targetUserUnivCode = user.university().code();
            this.targetUserStudentId = user.studentId();
        }

        private void setAuthDept(DepartmentDto dept) {
            this.authDept = dept;
            this.authUnivCode = dept.university().code();
            this.authDeptCode = dept.code();
        }

        private void setAuthPermission(AuthorityDto.Permission permission) {
            this.authPermission = permission;
        }

        @Override
        protected UserDto execMethod() {
            return userService.updateAuthority(
                    userToken, targetUserUnivCode, targetUserStudentId,
                    authUnivCode, authDeptCode, authPermission);
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[`Requester`가 `Master`이고 새로운 `dept`의 권한을 추가할 시]_[-]")
        public void SUCCESS_requesterIsMasterAndAddNewAuth() {
            setUpDefault();

            mockAndTestHappyPath();
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[`Requester`가 `Master`이고 기존 `dept`의 권한을 변경할 시]_[-]")
        public void SUCCESS_requesterIsMasterAndUpdateAuth() {
            setUpDefault();
            setTargetUser(randomUserHaveAdditionalAuthButLessPermissionOnDept(authDept, AuthorityDto.Permission.MASTER));
            setAuthPermission(randomUnderMasterPermission());

            mockAndTestHappyPath();
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[`Requester`가 `Master`이고 기존 `dept`의 권한을 `default`로 변경할 시]_[-]")
        public void SUCCESS_requesterIsMasterAndPermissionIsNull() {
            setUpDefault();
            setTargetUser(randomUserHaveAdditionalAuthButLessPermissionOnDept(authDept, AuthorityDto.Permission.MASTER));
            setAuthPermission(null);

            mockAndTestHappyPath();
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[`Requester`가 `Developer`이고 새로운 `dept`의 권한을 추가할 시]_[-]")
        public void SUCCESS_requesterIsDeveloperAndAddNewAuth() {
            setUpDefault();
            setRequester(randomDevUser());
            setAuthPermission(AuthorityDto.Permission.MASTER);

            mockAndTestHappyPath();
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[`Requester`가 `Developer`이고 기존 `dept`의 권한을 변경할 시]_[-]")
        public void SUCCESS_requesterIsDeveloperAndUpdateAuth() {
            setUpDefault();
            setRequester(randomDevUser());
            setTargetUser(randomUserHaveAdditionalAuthButLessPermissionOnDept(authDept, AuthorityDto.Permission.DEVELOPER));
            setAuthPermission(randomUnderDevPermission());

            mockAndTestHappyPath();
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[`Requester`가 `Developer`이고 새로운 `dept`의 권한을 추가할 시]_[-]")
        public void SUCCESS_requesterIsDeveloperAndPermissionIsNull() {
            setUpDefault();
            setRequester(randomDevUser());
            setTargetUser(randomUserHaveAdditionalAuthButLessPermissionOnDept(authDept, AuthorityDto.Permission.DEVELOPER));
            setAuthPermission(null);

            mockAndTestHappyPath();
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`requester`가 `MASTER`권한을 갖고 있지 않을 시]_[Forbidden]")
        public void ERROR_requesterDoesNotHaveMasterPermission_Forbidden() {
            setUpDefault();
            setRequester(randomUserHaveLessPermissionOnDept(authDept, AuthorityDto.Permission.MASTER));

            when(departmentDao.getByIndex(authUnivCode, authDeptCode))
                    .thenReturn(authDept);
            when(userDao.getByToken(userToken)).thenReturn(requester);

            TestHelper.exceptionTest(this::execMethod, ForbiddenException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`newPermission`이 `DEVELOPER` 일 시]_[MethodNotAllowed]")
        public void ERROR_newPermissionIsDeveloper_MethodNotAllowed() {
            setUpDefault();
            setAuthPermission(AuthorityDto.Permission.DEVELOPER);

            when(departmentDao.getByIndex(authUnivCode, authDeptCode))
                    .thenReturn(authDept);
            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(userDao.getByIndex(targetUserUnivCode, targetUserStudentId))
                    .thenReturn(targetUser);

            TestHelper.exceptionTest(this::execMethod, MethodNotAllowedException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`targetUser`가`Developer`일 시]_[MethodNotAllowed]")
        public void ERROR_targetUserIsDeveloper_MethodNotAllowed() {
            setUpDefault();
            setTargetUser(randomDevUser());

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(userDao.getByIndex(targetUserUnivCode, targetUserStudentId))
                    .thenReturn(targetUser);
            when(departmentDao.getByIndex(authUnivCode, authDeptCode))
                    .thenReturn(authDept);

            TestHelper.exceptionTest(this::execMethod, MethodNotAllowedException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`requester`이 `DEVELOPER`가 아닌데`newPermission`이 `MASTER` 일 시]_[Forbidden]")
        public void ERROR_requesterIsNotDevAndNewPermissionIsMaster_Forbidden() {
            setUpDefault();
            setAuthPermission(AuthorityDto.Permission.MASTER);

            when(departmentDao.getByIndex(authUnivCode, authDeptCode))
                    .thenReturn(authDept);
            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(userDao.getByIndex(targetUserUnivCode, targetUserStudentId))
                    .thenReturn(targetUser);

            TestHelper.exceptionTest(this::execMethod, ForbiddenException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`requester`이 `DEVELOPER`가 아닌데 `targetUser`가`MASTER`일 시]_[Forbidden]")
        public void ERROR_requesterIsNotDevAndTargetUserIsMaster_Forbidden() {
            setUpDefault();
            setTargetUser(randomUserHaveExactPermissionOnDept(authDept, AuthorityDto.Permission.MASTER));

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(userDao.getByIndex(targetUserUnivCode, targetUserStudentId))
                    .thenReturn(targetUser);
            when(departmentDao.getByIndex(authUnivCode, authDeptCode))
                    .thenReturn(authDept);

            TestHelper.exceptionTest(this::execMethod, ForbiddenException.class);
        }

        private void mockAndTestHappyPath() {
            when(departmentDao.getByIndex(authUnivCode, authDeptCode))
                    .thenReturn(authDept);
            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(userDao.getByIndex(targetUserUnivCode, targetUserStudentId))
                    .thenReturn(targetUser);

            execMethod();

            UserDto newUser = targetUser.withAuthorityUpdate(authDept, authPermission);
            verify(userDao).update(targetUserUnivCode, targetUserStudentId, newUser);
        }

        private AuthorityDto.Permission randomUnderDevPermission() {
            return permissionsUnderDev(allPermissions()).randomSelect();
        }

        private AuthorityDto.Permission randomUnderMasterPermission() {
            return permissionsUnderMaster(allPermissions()).randomSelect();
        }

        private UserDto randomUsersNotHaveAdditionalAuthOnDept(DepartmentDto dept) {
            RandomGetter<UserDto> users = allUsers();
            users = usersNotHaveAdditionalAuthOnDept(users, dept);
            return users.randomSelect();
        }

        private UserDto randomUserHaveAdditionalAuthButLessPermissionOnDept(DepartmentDto dept, AuthorityDto.Permission permission) {
            RandomGetter<UserDto> users = allUsers();
            users = usersHaveLessPermissionOnDept(users, dept, permission);
            users = usersHaveAdditionalAuthOnDept(users, dept);
            return users.randomSelect();
        }
    }

    private RandomGetter<AuthorityDto.Permission> permissionsUnderDev(RandomGetter<AuthorityDto.Permission> rs) {
        return rs.filter((permission) -> !permission.hasDeveloperPermission());
    }

    private RandomGetter<AuthorityDto.Permission> permissionsUnderMaster(RandomGetter<AuthorityDto.Permission> rs) {
        return rs.filter((permission) -> !permission.hasMasterPermission());
    }

    private RandomGetter<UserDto> usersHaveAdditionalAuthOnDept(RandomGetter<UserDto> rs, DepartmentDto dept) {
        return rs.filter((user) -> {
            for(AuthorityDto auth : user.authorities()) {
                if(auth.department().matchUniqueKey(dept) ||
                        auth.permission() == AuthorityDto.Permission.DEVELOPER) {
                    return true;
                }
            }
            return false;
        });
    }

    private RandomGetter<UserDto> usersNotHaveAdditionalAuthOnDept(RandomGetter<UserDto> rs, DepartmentDto dept) {
        return rs.filter((user) -> {
            for(AuthorityDto auth : user.authorities()) {
                if(auth.department().matchUniqueKey(dept) ||
                        auth.permission() == AuthorityDto.Permission.DEVELOPER) {
                    return false;
                }
            }
            return true;
        });
    }

    private abstract class UserNestedTest extends BaseNestedTest {
    }
}
