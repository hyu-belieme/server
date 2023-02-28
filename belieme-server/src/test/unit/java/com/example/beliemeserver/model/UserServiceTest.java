package com.example.beliemeserver.model;

import com.example.beliemeserver.common.DeveloperInfo;
import com.example.beliemeserver.common.Globals;
import com.example.beliemeserver.exception.*;
import com.example.beliemeserver.model.dto.*;
import com.example.beliemeserver.model.service.UserService;
import com.example.beliemeserver.model.util.HttpRequest;
import com.example.beliemeserver.util.RandomGetter;
import com.example.beliemeserver.util.TestHelper;
import org.assertj.core.api.Assertions;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
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

    @Nested
    @DisplayName("reloadDeveloperUser()")
    public final class TestReloadDeveloperUser {
        @Captor
        private ArgumentCaptor<UserDto> userArgumentCaptor;

        private final UniversityDto univ = Globals.DEV_UNIVERSITY;
        private final String univCode = Globals.DEV_UNIVERSITY_CODE;
        private String apiToken;
        private String studentId;
        private String name;

        private UserDto targetUser;

        private void setUpDefault() {
            RandomGetter<DeveloperInfo> devInfoGetter = new RandomGetter<>(Globals.developers);
            DeveloperInfo devInfo = devInfoGetter.randomSelect();

            this.apiToken = devInfo.apiToken();
            this.studentId = devInfo.studentId();
            this.name = devInfo.name();
            this.targetUser = UserDto.init(univ, studentId, name)
                    .withApprovalTimeStamp(0)
                    .withAuthorityAdd(Globals.DEV_AUTHORITY);
        }

        private UserDto execMethod() {
            return userService.reloadDeveloperUser(apiToken);
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[이미 DB에 등록된 developer일 시]_[-]")
        public void SUCCESS_devIsAlreadyOnDatabase() {
            setUpDefault();

            when(universityDao.getByIndex(univCode)).thenReturn(univ);
            when(userDao.getByIndex(univCode, studentId)).thenReturn(targetUser);

            execMethod();

            verify(userDao).update(eq(univCode), eq(studentId), userArgumentCaptor.capture());
            UserDto newUser = userArgumentCaptor.getValue();
            Assertions.assertThat(checkUpdatedUser(newUser)).isTrue();
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[DB에 등록되지 않은 developer일 시]_[-]")
        public void SUCCESS_devIsNotOnDatabase() {
            setUpDefault();

            when(universityDao.getByIndex(univCode)).thenReturn(univ);
            when(userDao.getByIndex(univCode, studentId)).thenThrow(NotFoundException.class);

            execMethod();

            verify(userDao).create(userArgumentCaptor.capture());
            UserDto newUser = userArgumentCaptor.getValue();
            Assertions.assertThat(checkCreatedUser(newUser)).isTrue();
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[API Token이 존재하지 않을 시]_[UnauthorizedException]")
        public void ERROR_isUnauthorizedApiToken_UnauthorizedException() {
            setUpDefault();
            apiToken = "";

            TestHelper.exceptionTest(this::execMethod, UnauthorizedException.class);
        }

        private boolean checkUpdatedUser(UserDto newUser) {
            System.out.println(newUser);
            if(newUser.studentId().equals(studentId)
                    && newUser.university().equals(univ)
                    && newUser.name().equals(name)
                    && !newUser.token().equals(targetUser.token())
                    && newUser.approvalTimeStamp() > targetUser.approvalTimeStamp()
            ) {
                return checkDeveloperAuth(newUser);
            }
            return false;
        }

        private boolean checkCreatedUser(UserDto newUser) {
            if(newUser.studentId().equals(studentId)
                    && newUser.university().equals(univ)
                    && newUser.name().equals(name)) {
                return checkDeveloperAuth(newUser);
            }
            return false;
        }

        private boolean checkDeveloperAuth(UserDto newUser) {
            return newUser.authorities().stream()
                    .anyMatch(authority -> authority.equals(Globals.DEV_AUTHORITY));
        }
    }

    @Nested
    @DisplayName("updateUserFromHanyangUniversity()")
    public final class TestUpdateUserFromHanyangUniversity {
        private static MockedStatic<HttpRequest> httpRequest;
        @Captor
        private ArgumentCaptor<UserDto> userArgumentCaptor;

        private final String apiToken = "";

        private UniversityDto univ;
        private String univCode;

        private UserDto targetUser;
        private String studentId;

        private String newName;
        private String newMajorCode;

        private List<DepartmentDto> deptByUniv;
        private List<DepartmentDto> newDepartments;

        @BeforeEach
        void setUp() {
            httpRequest = mockStatic(HttpRequest.class);
        }

        @AfterEach
        void tearDown() {
            httpRequest.close();
        }

        private void setUpDefault() {
            setUniv();
            setTargetUser(randomUserOnUniv(univ));
            newName = "이석환";
            newMajorCode = "FH04067";
            deptByUniv = stub.ALL_DEPTS.stream()
                    .filter((dept) -> dept.university().matchUniqueKey(univ))
                    .toList();
            newDepartments = stub.ALL_DEPTS.stream().filter((dept) -> {
                if(!dept.university().matchUniqueKey(univ)) return false;
                return dept.baseMajors().stream().anyMatch(major -> newMajorCode.equals(major.code()));
            }).toList();
        }

        private void setUniv() {
            univ = stub.HYU_UNIV;
            univCode = stub.HYU_UNIV.code();
        }

        private void setTargetUser(UserDto user) {
            this.targetUser = user.withApprovalTimeStamp(0);
            this.studentId = user.studentId();
        }

        private JSONObject makeJsonResponse() {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("gaeinNo", studentId);
            jsonObject.put("userNm", newName);
            jsonObject.put("sosokId", newMajorCode);

            return jsonObject;
        }

        private UserDto execMethod() {
            return userService.reloadHanyangUniversityUser(apiToken);
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[기존에 존재하는 유저이고 새로운 `majorCode`또한 기존에 존재 할 시]_[-]")
        public void SUCCESS_userIsAlreadyCreatedAndNoMajorCreate() {
            setUpDefault();

            when(HttpRequest.getUserInfoFromHanyangApi(apiToken)).thenReturn(makeJsonResponse());
            when(universityDao.getByIndex(univCode)).thenReturn(univ);
            when(departmentDao.getListByUniversity(univCode)).thenReturn(deptByUniv);
            when(userDao.getByIndex(univCode, studentId)).thenReturn(targetUser);

            execMethod();

            verify(userDao).update(eq(univCode), eq(studentId), userArgumentCaptor.capture());
            UserDto newUser = userArgumentCaptor.getValue();
            Assertions.assertThat(checkUpdatedUser(newUser)).isTrue();
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[기존에 존재하는 유저이고 새로운 `majorCode`가 기존에 존재 하지 않을 시]_[-]")
        public void SUCCESS_userIsAlreadyCreatedAndNewMajorCreate() {
            setUpDefault();

            when(HttpRequest.getUserInfoFromHanyangApi(apiToken)).thenReturn(makeJsonResponse());
            when(universityDao.getByIndex(univCode)).thenReturn(univ);
            when(departmentDao.getListByUniversity(univCode)).thenReturn(deptByUniv);
            when(userDao.getByIndex(univCode, studentId)).thenReturn(targetUser);

            execMethod();

            verify(userDao).update(eq(univCode), eq(studentId), userArgumentCaptor.capture());
            UserDto newUser = userArgumentCaptor.getValue();
            Assertions.assertThat(checkUpdatedUser(newUser)).isTrue();
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[새로운 유저이고 새로운 `majorCode`가 기존에 존재 할 시]_[-]")
        public void SUCCESS_userIsNewAndNoMajorCreate() {
            setUpDefault();

            when(HttpRequest.getUserInfoFromHanyangApi(apiToken)).thenReturn(makeJsonResponse());
            when(universityDao.getByIndex(univCode)).thenReturn(univ);
            when(departmentDao.getListByUniversity(univCode)).thenReturn(deptByUniv);
            when(userDao.getByIndex(univCode, studentId)).thenThrow(NotFoundException.class);

            execMethod();

            verify(userDao).create(userArgumentCaptor.capture());
            UserDto newUser = userArgumentCaptor.getValue();
            Assertions.assertThat(checkCreatedUser(newUser)).isTrue();
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[새로운 유저이고 새로운 `majorCode`가 기존에 존재 하지 않을 시]_[-]")
        public void SUCCESS_userIsNewAndNewMajorCreate() {
            setUpDefault();

            when(HttpRequest.getUserInfoFromHanyangApi(apiToken)).thenReturn(makeJsonResponse());
            when(universityDao.getByIndex(univCode)).thenReturn(univ);
            when(departmentDao.getListByUniversity(univCode)).thenReturn(deptByUniv);
            when(userDao.getByIndex(univCode, studentId)).thenThrow(NotFoundException.class);

            execMethod();

            verify(userDao).create(userArgumentCaptor.capture());
            UserDto newUser = userArgumentCaptor.getValue();
            Assertions.assertThat(checkCreatedUser(newUser)).isTrue();
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[한양 api 통신 과정에 문제가 생겼을 시]_[BadGateWayException]")
        public void ERROR_networkProblemOnHanyangApi_BadGateWayException() {
            setUpDefault();

            when(HttpRequest.getUserInfoFromHanyangApi(apiToken)).thenThrow(BadGateWayException.class);

            TestHelper.exceptionTest(this::execMethod, BadGateWayException.class);
        }

        private boolean checkUpdatedUser(UserDto newUser) {
            if(newUser.studentId().equals(studentId)
                    && newUser.university().equals(univ)
                    && newUser.name().equals(newName)
                    && !newUser.token().equals(targetUser.token())
                    && newUser.approvalTimeStamp() > targetUser.approvalTimeStamp()
            ) {
                return checkAuthUpdate(newUser);
            }
            return false;
        }

        private boolean checkCreatedUser(UserDto newUser) {
            if(newUser.studentId().equals(studentId)
                    && newUser.university().equals(univ)
                    && newUser.name().equals(newName)) {
                return checkAuthUpdate(newUser);
            }
            return false;
        }

        private boolean checkAuthUpdate(UserDto newUser) {
            for(DepartmentDto dept: newDepartments) {
                if(newUser.authorities().stream()
                        .filter(authority -> authority.permission() == AuthorityDto.Permission.DEFAULT)
                        .noneMatch(authority -> authority.department().equals(dept))
                ) {
                    return false;
                }
            }
            return true;
        }
    }

    private UserDto randomUserOnUniv(UniversityDto univ) {
        return randomSelectAndLog(usersOnUniv(allUsers(), univ));
    }

    private RandomGetter<AuthorityDto.Permission> permissionsUnderDev(RandomGetter<AuthorityDto.Permission> rs) {
        return rs.filter((permission) -> !permission.hasDeveloperPermission());
    }

    private RandomGetter<AuthorityDto.Permission> permissionsUnderMaster(RandomGetter<AuthorityDto.Permission> rs) {
        return rs.filter((permission) -> !permission.hasMasterPermission());
    }

    private RandomGetter<UserDto> usersOnUniv(RandomGetter<UserDto> rs, UniversityDto univ) {
        return rs.filter((user) -> user.university().matchUniqueKey(univ));
    }

    private RandomGetter<UserDto> usersHaveAdditionalAuthOnDept(RandomGetter<UserDto> rs, DepartmentDto dept) {
        return rs.filter((user) -> {
            for(AuthorityDto auth : user.authorities()) {
                if(auth.department().matchUniqueKey(dept)
                        && auth.permission() != AuthorityDto.Permission.DEFAULT) return true;
                if(auth.permission() == AuthorityDto.Permission.DEVELOPER) return true;
            }
            return false;
        });
    }

    private RandomGetter<UserDto> usersNotHaveAdditionalAuthOnDept(RandomGetter<UserDto> rs, DepartmentDto dept) {
        return rs.filter((user) -> {
            for(AuthorityDto auth : user.authorities()) {
                if(auth.department().matchUniqueKey(dept)
                        && auth.permission() != AuthorityDto.Permission.DEFAULT) return false;
                if(auth.permission() == AuthorityDto.Permission.DEVELOPER) return false;
            }
            return true;
        });
    }

    private abstract class UserNestedTest extends BaseNestedTest {
    }
}
