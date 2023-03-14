package com.example.beliemeserver.model;

import com.example.beliemeserver.config.initdata.InitialDataDtoAdapter;
import com.example.beliemeserver.config.initdata.container.AuthorityInfo;
import com.example.beliemeserver.config.initdata.container.UniversityInfo;
import com.example.beliemeserver.config.initdata.container.UserInfo;
import com.example.beliemeserver.error.exception.BadGatewayException;
import com.example.beliemeserver.error.exception.ForbiddenException;
import com.example.beliemeserver.error.exception.NotFoundException;
import com.example.beliemeserver.model.dto.*;
import com.example.beliemeserver.model.exception.PermissionDeniedException;
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
            setRequester(randomUserHaveMorePermissionOnDept(dept, Permission.MASTER));
        }

        @Override
        protected List<UserDto> execMethod() {
            return userService.getListByDepartment(userToken, univCode, deptCode);
        }

        @Override
        protected void setRequesterAccessDenied() {
            setRequester(randomUserHaveLessPermissionOnDept(dept, Permission.MASTER));
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(userDao.getAllList()).thenReturn(stub.ALL_USERS);

            List<UserDto> expected = new ArrayList<>();
            for (UserDto user : stub.ALL_USERS) {
                if (user.getMaxPermission(dept).hasMorePermission(Permission.USER))
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
        private Permission authPermission;

        @Override
        protected void setUpDefault() {
            setAuthDept(TEST_DEPT);
            setAuthPermission(randomUnderMasterPermission());
            setRequester(randomUserHaveExactPermissionOnDept(authDept, Permission.MASTER));
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

        private void setAuthPermission(Permission permission) {
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
            setTargetUser(randomUserHaveAdditionalAuthButLessPermissionOnDept(authDept, Permission.MASTER));
            setAuthPermission(randomUnderMasterPermission());

            mockAndTestHappyPath();
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[`Requester`가 `Master`이고 기존 `dept`의 권한을 `default`로 변경할 시]_[-]")
        public void SUCCESS_requesterIsMasterAndPermissionIsNull() {
            setUpDefault();
            setTargetUser(randomUserHaveAdditionalAuthButLessPermissionOnDept(authDept, Permission.MASTER));
            setAuthPermission(null);

            mockAndTestHappyPath();
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[`Requester`가 `Developer`이고 새로운 `dept`의 권한을 추가할 시]_[-]")
        public void SUCCESS_requesterIsDeveloperAndAddNewAuth() {
            setUpDefault();
            setRequester(randomDevUser());
            setAuthPermission(Permission.MASTER);

            mockAndTestHappyPath();
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[`Requester`가 `Developer`이고 기존 `dept`의 권한을 변경할 시]_[-]")
        public void SUCCESS_requesterIsDeveloperAndUpdateAuth() {
            setUpDefault();
            setRequester(randomDevUser());
            setTargetUser(randomUserHaveAdditionalAuthButLessPermissionOnDept(authDept, Permission.DEVELOPER));
            setAuthPermission(randomUnderDevPermission());

            mockAndTestHappyPath();
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[`Requester`가 `Developer`이고 새로운 `dept`의 권한을 추가할 시]_[-]")
        public void SUCCESS_requesterIsDeveloperAndPermissionIsNull() {
            setUpDefault();
            setRequester(randomDevUser());
            setTargetUser(randomUserHaveAdditionalAuthButLessPermissionOnDept(authDept, Permission.DEVELOPER));
            setAuthPermission(null);

            mockAndTestHappyPath();
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`requester`가 `MASTER`권한을 갖고 있지 않을 시]_[Forbidden]")
        public void ERROR_requesterDoesNotHaveMasterPermission_Forbidden() {
            setUpDefault();
            setRequester(randomUserHaveLessPermissionOnDept(authDept, Permission.MASTER));

            when(departmentDao.getByIndex(authUnivCode, authDeptCode))
                    .thenReturn(authDept);
            when(userDao.getByToken(userToken)).thenReturn(requester);

            TestHelper.exceptionTest(this::execMethod, ForbiddenException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`newPermission`이 `DEVELOPER` 일 시]_[ForbiddenException]")
        public void ERROR_newPermissionIsDeveloper_ForbiddenException() {
            setUpDefault();
            setAuthPermission(Permission.DEVELOPER);

            when(departmentDao.getByIndex(authUnivCode, authDeptCode))
                    .thenReturn(authDept);
            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(userDao.getByIndex(targetUserUnivCode, targetUserStudentId))
                    .thenReturn(targetUser);

            TestHelper.exceptionTest(this::execMethod, ForbiddenException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`targetUser`가`Developer`일 시]_[ForbiddenException]")
        public void ERROR_targetUserIsDeveloper_ForbiddenException() {
            setUpDefault();
            setTargetUser(randomDevUser());

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(userDao.getByIndex(targetUserUnivCode, targetUserStudentId))
                    .thenReturn(targetUser);
            when(departmentDao.getByIndex(authUnivCode, authDeptCode))
                    .thenReturn(authDept);

            TestHelper.exceptionTest(this::execMethod, ForbiddenException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`requester`이 `DEVELOPER`가 아닌데`newPermission`이 `MASTER` 일 시]_[PermissionDeniedException]")
        public void ERROR_requesterIsNotDevAndNewPermissionIsMaster_PermissionDeniedException() {
            setUpDefault();
            setAuthPermission(Permission.MASTER);

            when(departmentDao.getByIndex(authUnivCode, authDeptCode))
                    .thenReturn(authDept);
            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(userDao.getByIndex(targetUserUnivCode, targetUserStudentId))
                    .thenReturn(targetUser);

            TestHelper.exceptionTest(this::execMethod, PermissionDeniedException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`requester`이 `DEVELOPER`가 아닌데 `targetUser`가`MASTER`일 시]_[PermissionDeniedException]")
        public void ERROR_requesterIsNotDevAndTargetUserIsMaster_PermissionDeniedException() {
            setUpDefault();
            setTargetUser(randomUserHaveExactPermissionOnDept(authDept, Permission.MASTER));

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(userDao.getByIndex(targetUserUnivCode, targetUserStudentId))
                    .thenReturn(targetUser);
            when(departmentDao.getByIndex(authUnivCode, authDeptCode))
                    .thenReturn(authDept);

            TestHelper.exceptionTest(this::execMethod, PermissionDeniedException.class);
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

        private Permission randomUnderDevPermission() {
            return permissionsUnderDev(allPermissions()).randomSelect();
        }

        private Permission randomUnderMasterPermission() {
            return permissionsUnderMaster(allPermissions()).randomSelect();
        }

        private UserDto randomUsersNotHaveAdditionalAuthOnDept(DepartmentDto dept) {
            RandomGetter<UserDto> users = allUsers();
            users = usersNotHaveAdditionalAuthOnDept(users, dept);
            return users.randomSelect();
        }

        private UserDto randomUserHaveAdditionalAuthButLessPermissionOnDept(DepartmentDto dept, Permission permission) {
            RandomGetter<UserDto> users = allUsers();
            users = usersHaveLessPermissionOnDept(users, dept, permission);
            users = usersHaveAdditionalAuthOnDept(users, dept);
            return users.randomSelect();
        }
    }

    @Nested
    @DisplayName("reloadInitialUser()")
    public final class TestReloadDeveloperUser {
        @Captor
        private ArgumentCaptor<UserDto> userArgumentCaptor;

        private UserInfo userInfo;
        private UniversityDto univ;
        private String univCode;
        private String apiToken;
        private String studentId;

        private UserDto targetUser;
        private List<DepartmentDto> deptList;

        private void setUpDefault() {
            RandomGetter<UserInfo> userInfoGetter = new RandomGetter<>(stub.INIT_DATA.userInfos());
            this.userInfo = userInfoGetter.randomSelect();
            this.apiToken = userInfo.apiToken();
            this.studentId = userInfo.studentId();
            this.univCode = userInfo.universityCode();

            InitialDataDtoAdapter initialDataAdapter = new InitialDataDtoAdapter(stub.INIT_DATA);
            this.univ = initialDataAdapter.universities().get(univCode);
            this.deptList = new ArrayList<>(initialDataAdapter.departments().values());
            this.targetUser = UserDto.init(univ, studentId, userInfo.name())
                    .withApprovalTimeStamp(0);
        }

        private UserDto execMethod() {
            return userService.reloadInitialUser(univCode, apiToken);
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[이미 DB에 등록된 user일 시]_[-]")
        public void SUCCESS_userIsAlreadyOnDatabase() {
            setUpDefault();

            when(initialData.userInfos()).thenReturn(stub.INIT_DATA.userInfos());
            when(universityDao.getByIndex(univCode)).thenReturn(univ);
            when(userDao.getByIndex(univCode, studentId)).thenReturn(targetUser);
            mockDepartmentDao();

            execMethod();

            verify(userDao).update(eq(univCode), eq(studentId), userArgumentCaptor.capture());
            UserDto newUser = userArgumentCaptor.getValue();
            Assertions.assertThat(checkUpdatedUser(newUser, userInfo)).isTrue();
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[DB에 등록되지 않은 developer일 시]_[-]")
        public void SUCCESS_devIsNotOnDatabase() {
            setUpDefault();

            when(initialData.userInfos()).thenReturn(stub.INIT_DATA.userInfos());
            when(universityDao.getByIndex(univCode)).thenReturn(univ);
            when(userDao.getByIndex(univCode, studentId)).thenThrow(NotFoundException.class);
            mockDepartmentDao();

            execMethod();

            verify(userDao).create(userArgumentCaptor.capture());
            UserDto newUser = userArgumentCaptor.getValue();
            Assertions.assertThat(checkCreatedUser(newUser, userInfo)).isTrue();
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[API Token이 존재하지 않을 시]_[UnauthorizedException]")
        public void ERROR_isUnauthorizedApiToken_UnauthorizedException() {
            setUpDefault();
            apiToken = "";

            when(initialData.userInfos()).thenReturn(stub.INIT_DATA.userInfos());

            TestHelper.objectCompareTest(this::execMethod, null);
        }

        private void mockDepartmentDao() {
            for (AuthorityInfo authInfo : userInfo.authorities()) {
                DepartmentDto targetDept = deptList.stream()
                        .filter(dept -> dept.matchUniqueKey(authInfo.universityCode(), authInfo.departmentCode()))
                        .findFirst().orElse(null);
                when(departmentDao.getByIndex(authInfo.universityCode(), authInfo.departmentCode())).thenReturn(targetDept);
            }
        }

        private boolean checkUpdatedUser(UserDto newUser, UserInfo userInfo) {
            System.out.println(newUser);
            if (newUser.studentId().equals(userInfo.studentId())
                    && newUser.university().matchUniqueKey(userInfo.universityCode())
                    && newUser.name().equals(userInfo.name())
                    && !newUser.token().equals(targetUser.token())
                    && newUser.approvalTimeStamp() > targetUser.approvalTimeStamp()
            ) {
                return checkUserAuth(newUser, userInfo.authorities());
            }
            return false;
        }

        private boolean checkCreatedUser(UserDto newUser, UserInfo userInfo) {
            System.out.println(newUser);
            if (newUser.studentId().equals(userInfo.studentId())
                    && newUser.university().matchUniqueKey(userInfo.universityCode())
                    && newUser.name().equals(userInfo.name())
            ) {
                return checkUserAuth(newUser, userInfo.authorities());
            }
            return false;
        }

        private boolean checkUserAuth(UserDto newUser, List<AuthorityInfo> authorityInfos) {
            return authorityInfos.stream().allMatch(authorityInfo ->
                    newUser.authorities().stream().anyMatch(authorityDto ->
                            authorityDto.department().matchUniqueKey(authorityInfo.universityCode(), authorityInfo.departmentCode())
                                    && authorityDto.permission().toString().equals(authorityInfo.permission())
                    )
            );
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
        private String apiUrl;
        private String clientKey;

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
                if (!dept.university().matchUniqueKey(univ)) return false;
                return dept.baseMajors().stream().anyMatch(major -> newMajorCode.equals(major.code()));
            }).toList();
        }

        private void setUniv() {
            UniversityInfo univInfo = stub.HYU_UNIV_INIT_INFO;
            univ = stub.HYU_UNIV;
            univCode = univ.code();
            apiUrl = univInfo.externalApiInfo().get("url");
            clientKey = univInfo.externalApiInfo().get("clientKey");
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

            when(initialData.universityInfos()).thenReturn(stub.INIT_DATA.universityInfos());
            when(HttpRequest.getUserInfoFromHanyangApi(apiUrl, clientKey, apiToken)).thenReturn(makeJsonResponse());
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

            when(initialData.universityInfos()).thenReturn(stub.INIT_DATA.universityInfos());
            when(HttpRequest.getUserInfoFromHanyangApi(apiUrl, clientKey, apiToken)).thenReturn(makeJsonResponse());
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

            when(initialData.universityInfos()).thenReturn(stub.INIT_DATA.universityInfos());
            when(HttpRequest.getUserInfoFromHanyangApi(apiUrl, clientKey, apiToken)).thenReturn(makeJsonResponse());
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

            when(initialData.universityInfos()).thenReturn(stub.INIT_DATA.universityInfos());
            when(HttpRequest.getUserInfoFromHanyangApi(apiUrl, clientKey, apiToken)).thenReturn(makeJsonResponse());
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

            when(initialData.universityInfos()).thenReturn(stub.INIT_DATA.universityInfos());
            when(HttpRequest.getUserInfoFromHanyangApi(apiUrl, clientKey, apiToken)).thenThrow(BadGatewayException.class);

            TestHelper.exceptionTest(this::execMethod, BadGatewayException.class);
        }

        private boolean checkUpdatedUser(UserDto newUser) {
            System.out.println(newUser.studentId());
            System.out.println(studentId);
            System.out.println();

            System.out.println(newUser.university());
            System.out.println(univ);
            System.out.println();
            if (newUser.studentId().equals(studentId)
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
            if (newUser.studentId().equals(studentId)
                    && newUser.university().equals(univ)
                    && newUser.name().equals(newName)) {
                return checkAuthUpdate(newUser);
            }
            return false;
        }

        private boolean checkAuthUpdate(UserDto newUser) {
            for (DepartmentDto dept : newDepartments) {
                if (newUser.authorities().stream()
                        .filter(authority -> authority.permission() == Permission.DEFAULT)
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

    private RandomGetter<Permission> permissionsUnderDev(RandomGetter<Permission> rs) {
        return rs.filter((permission) -> !permission.hasDeveloperPermission());
    }

    private RandomGetter<Permission> permissionsUnderMaster(RandomGetter<Permission> rs) {
        return rs.filter((permission) -> !permission.hasMasterPermission());
    }

    private RandomGetter<UserDto> usersOnUniv(RandomGetter<UserDto> rs, UniversityDto univ) {
        return rs.filter((user) -> user.university().matchUniqueKey(univ));
    }

    private RandomGetter<UserDto> usersHaveAdditionalAuthOnDept(RandomGetter<UserDto> rs, DepartmentDto dept) {
        return rs.filter((user) -> {
            for (AuthorityDto auth : user.authorities()) {
                if (auth.department().matchUniqueKey(dept)
                        && auth.permission() != Permission.DEFAULT) return true;
                if (auth.permission() == Permission.DEVELOPER) return true;
            }
            return false;
        });
    }

    private RandomGetter<UserDto> usersNotHaveAdditionalAuthOnDept(RandomGetter<UserDto> rs, DepartmentDto dept) {
        return rs.filter((user) -> {
            for (AuthorityDto auth : user.authorities()) {
                if (auth.department().matchUniqueKey(dept)
                        && auth.permission() != Permission.DEFAULT) return false;
                if (auth.permission() == Permission.DEVELOPER) return false;
            }
            return true;
        });
    }

    private abstract class UserNestedTest extends BaseNestedTest {
    }
}
