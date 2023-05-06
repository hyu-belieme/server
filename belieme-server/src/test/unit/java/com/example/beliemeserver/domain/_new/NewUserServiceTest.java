package com.example.beliemeserver.domain._new;

import com.example.beliemeserver.config.initdata._new.container.AuthorityInfo;
import com.example.beliemeserver.config.initdata._new.container.MajorInfo;
import com.example.beliemeserver.config.initdata._new.container.UniversityInfo;
import com.example.beliemeserver.config.initdata._new.container.UserInfo;
import com.example.beliemeserver.domain.dto._new.*;
import com.example.beliemeserver.domain.dto.enumeration.Permission;
import com.example.beliemeserver.domain.exception.PermissionDeniedException;
import com.example.beliemeserver.domain.service._new.NewUserService;
import com.example.beliemeserver.domain.util.HttpRequest;
import com.example.beliemeserver.error.exception.BadGatewayException;
import com.example.beliemeserver.error.exception.ForbiddenException;
import com.example.beliemeserver.error.exception.InvalidIndexException;
import com.example.beliemeserver.error.exception.NotFoundException;
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
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NewUserServiceTest extends NewBaseServiceTest {
    private final DepartmentDto TEST_DEPT = stub.HYU_CSE_DEPT;

    @InjectMocks
    private NewUserService userService;

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
            return userService.getListByDepartment(userToken, deptId);
        }

        @Override
        protected void setRequesterAccessDenied() {
            setRequester(randomUserHaveLessPermissionOnDept(dept, Permission.MASTER));
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setUpDefault();

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(departmentDao.getById(deptId)).thenReturn(dept);
            when(userDao.getAllList()).thenReturn(stub.ALL_USERS);

            List<UserDto> expected = new ArrayList<>();
            for (UserDto user : stub.ALL_USERS) {
                if (user.getMaxPermission(dept).hasMorePermission(Permission.USER))
                    expected.add(user);
            }

            TestHelper.listCompareTest(this::execMethod, expected);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[해당 `index`의 `department`가 존재하지 않을 시]_[InvalidIndexException]")
        public void ERROR_getInvalidIndex_InvalidIndexException() {
            setUpDefault();

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(departmentDao.getById(deptId)).thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
        }

        @Override
        @RepeatedTest(10)
        @DisplayName("[ERROR]_[권한이 없을 시]_[PermissionDeniedException]")
        public void ERROR_accessDenied_PermissionDeniedException() {
            setUpDefault();
            setRequesterAccessDenied();

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(departmentDao.getById(deptId)).thenReturn(dept);

            TestHelper.exceptionTest(this::execMethod, PermissionDeniedException.class);
        }
    }

    @Nested
    @DisplayName("getById()")
    public final class TestGetById extends UserNestedTest {
        private UserDto target;
        private UUID targetUserId;

        @Override
        protected void setUpDefault() {
            setRequester(randomDevUser());
            setTarget(randomUser());
        }

        private void setTarget(UserDto user) {
            this.target = user;
            this.targetUserId = user.id();
        }

        @Override
        protected UserDto execMethod() {
            return userService.getById(userToken, targetUserId);
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[개발자는 모든 `User`에 접근 가능하다]_[-]")
        public void SUCCESS_devAccess() {
            setUpDefault();

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(userDao.getById(targetUserId)).thenReturn(target);

            TestHelper.objectCompareTest(this::execMethod, target);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[해당 `index`에 `User`가 존재하지 않을 시]_[NotFoundException]")
        public void ERROR_departmentNotFound_NotFoundException() {
            setUpDefault();

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(userDao.getById(targetUserId)).thenThrow(NotFoundException.class);

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
    @DisplayName("updateAuthorityOfUser()")
    public final class TestUpdateAuthorityOfUser extends UserNestedTest {
        private UserDto targetUser;
        private UUID targetUserId;

        private DepartmentDto authDept;
        private UUID authDeptId;
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
            this.targetUserId = user.id();
        }

        private void setAuthDept(DepartmentDto dept) {
            this.authDept = dept;
            this.authDeptId = dept.id();
        }

        private void setAuthPermission(Permission permission) {
            this.authPermission = permission;
        }

        @Override
        protected UserDto execMethod() {
            return userService.updateAuthorityOfUser(
                    userToken, targetUserId, authDeptId, authPermission);
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

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(departmentDao.getById(authDeptId)).thenReturn(authDept);

            TestHelper.exceptionTest(this::execMethod, ForbiddenException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`newPermission`이 `DEVELOPER` 일 시]_[ForbiddenException]")
        public void ERROR_newPermissionIsDeveloper_ForbiddenException() {
            setUpDefault();
            setAuthPermission(Permission.DEVELOPER);

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(departmentDao.getById(authDeptId)).thenReturn(authDept);
            when(userDao.getById(targetUserId)).thenReturn(targetUser);

            TestHelper.exceptionTest(this::execMethod, ForbiddenException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`targetUser`가`Developer`일 시]_[ForbiddenException]")
        public void ERROR_targetUserIsDeveloper_ForbiddenException() {
            setUpDefault();
            setTargetUser(randomDevUser());

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(departmentDao.getById(authDeptId)).thenReturn(authDept);
            when(userDao.getById(targetUserId)).thenReturn(targetUser);

            TestHelper.exceptionTest(this::execMethod, ForbiddenException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`requester`이 `DEVELOPER`가 아닌데`newPermission`이 `MASTER` 일 시]_[PermissionDeniedException]")
        public void ERROR_requesterIsNotDevAndNewPermissionIsMaster_PermissionDeniedException() {
            setUpDefault();
            setAuthPermission(Permission.MASTER);

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(departmentDao.getById(authDeptId)).thenReturn(authDept);
            when(userDao.getById(targetUserId)).thenReturn(targetUser);

            TestHelper.exceptionTest(this::execMethod, PermissionDeniedException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`requester`이 `DEVELOPER`가 아닌데 `targetUser`가`MASTER`일 시]_[PermissionDeniedException]")
        public void ERROR_requesterIsNotDevAndTargetUserIsMaster_PermissionDeniedException() {
            setUpDefault();
            setTargetUser(randomUserHaveExactPermissionOnDept(authDept, Permission.MASTER));

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(departmentDao.getById(authDeptId)).thenReturn(authDept);
            when(userDao.getById(targetUserId)).thenReturn(targetUser);

            TestHelper.exceptionTest(this::execMethod, PermissionDeniedException.class);
        }

        private void mockAndTestHappyPath() {
            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(departmentDao.getById(authDeptId)).thenReturn(authDept);
            when(userDao.getById(targetUserId)).thenReturn(targetUser);

            execMethod();

            List<AuthorityDto> newAuthorities = updateAuthorities(targetUser.authorities(), authDept, authPermission);
            verify(userDao).update(
                    eq(targetUserId),
                    eq(targetUser.university().id()),
                    eq(targetUser.studentId()),
                    eq(targetUser.name()),
                    eq(targetUser.entranceYear()),
                    any(),
                    eq(targetUser.createdAt()),
                    anyLong(),
                    eq(newAuthorities)
            );
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

        private List<AuthorityDto> updateAuthorities(List<AuthorityDto> authorities, DepartmentDto department, Permission newPermission) {
            List<AuthorityDto> output = new ArrayList<>(authorities);

            if (newPermission == null) {
                output.removeIf((e) -> e.department().matchId(department));
                return output;
            }

            for (int i = 0; i < output.size(); i++) {
                AuthorityDto authority = output.get(i);
                if (authority.department().matchId(department)) {
                    output.set(i, new AuthorityDto(department, newPermission));
                    return output;
                }
            }

            output.add(new AuthorityDto(department, newPermission));
            return output;
        }
    }

    @Nested
    @DisplayName("reloadInitialUser()")
    public final class TestReloadDeveloperUser {
        @Captor
        private ArgumentCaptor<List<AuthorityDto>> authListCaptor;

        private UserInfo userInfo;
        private UUID univId;
        private String apiToken;

        private UserDto targetUser;
        private List<DepartmentDto> deptList;

        private void setUpDefault() {
            RandomGetter<UserInfo> userInfoGetter = new RandomGetter<>(stub.INIT_DATA.userInfos());
            this.userInfo = userInfoGetter.randomSelect();
            this.apiToken = userInfo.apiToken();
            this.univId = userInfo.universityId();

            UniversityDto univ = getUnivByUnivIdFromStub(userInfo.universityId());
            this.targetUser = makeUser(univ, userInfo);
            this.deptList = getDeptListByUnivIdFromStub(univ);
        }

        private UniversityDto getUnivByUnivIdFromStub(UUID univId) {
            UniversityInfo univInfo = stub.INIT_DATA.universityInfos().values().stream()
                    .filter(e -> e.id().equals(univId)).findFirst().get();
            return new UniversityDto(univInfo.id(), univInfo.name(), univInfo.externalApiInfo().get("url"));
        }

        private List<DepartmentDto> getDeptListByUnivIdFromStub(UniversityDto univ) {
            return stub.INIT_DATA.departmentInfos().values().stream().map(
                    e -> new DepartmentDto(e.id(), univ, e.name(), makeMajorList(univ, e.baseMajors()))
            ).toList();
        }

        private UserDto makeUser(UniversityDto univ, UserInfo info) {
            return new UserDto(
                    info.id(), univ, info.studentId(), info.name(),
                    info.entranceYear(), UUID.randomUUID().toString(),
                    currentTime(), currentTime(), new ArrayList<>()
            );
        }

        private List<MajorDto> makeMajorList(UniversityDto univ, List<MajorInfo> infos) {
            return infos.stream().map(info -> new MajorDto(info.id(), univ, info.code())).toList();
        }

        private UserDto execMethod() {
            return userService.reloadInitialUser(univId, apiToken);
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[이미 DB에 등록된 user일 시]_[-]")
        public void SUCCESS_userIsAlreadyOnDatabase() {
            setUpDefault();

            when(initialData.userInfos()).thenReturn(stub.INIT_DATA.userInfos());
            when(userDao.getById(userInfo.id())).thenReturn(targetUser);
            mockDepartmentDao();

            execMethod();

            verify(userDao).update(eq(userInfo.id()), eq(univId), eq(userInfo.studentId()), eq(userInfo.name()), eq(userInfo.entranceYear()), any(), eq(targetUser.createdAt()), anyLong(), authListCaptor.capture());
            List<AuthorityDto> authDtoList = authListCaptor.getValue();
            Assertions.assertThat(checkMatchAuthDtoAndInfoList(authDtoList, userInfo.authorities())).isTrue();
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[DB에 등록되지 않은 developer일 시]_[-]")
        public void SUCCESS_devIsNotOnDatabase() {
            setUpDefault();

            when(initialData.userInfos()).thenReturn(stub.INIT_DATA.userInfos());
            when(userDao.getById(userInfo.id())).thenThrow(NotFoundException.class);
            mockDepartmentDao();

            execMethod();

            verify(userDao).create(any(), eq(univId), eq(userInfo.studentId()), eq(userInfo.name()), eq(userInfo.entranceYear()), any(), anyLong(), anyLong(), authListCaptor.capture());
            List<AuthorityDto> authDtoList = authListCaptor.getValue();
            Assertions.assertThat(checkMatchAuthDtoAndInfoList(authDtoList, userInfo.authorities())).isTrue();
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
                        .filter(dept -> dept.id().equals(authInfo.departmentId()))
                        .findFirst().orElse(null);
                when(departmentDao.getById(authInfo.departmentId())).thenReturn(targetDept);
            }
        }

        private boolean checkMatchAuthDtoAndInfoList(List<AuthorityDto> authorities, List<AuthorityInfo> authorityInfos) {
            return authorityInfos.stream().allMatch(authorityInfo ->
                    authorities.stream().anyMatch(
                            authorityDto ->checkMatchAuthDtoAndInfo(authorityDto, authorityInfo)
                    )
            );
        }

        private boolean checkMatchAuthDtoAndInfo(AuthorityDto dto, AuthorityInfo info) {
            return  dto.department().id().equals(info.departmentId())
                    && dto.permission().toString().equals(info.permission());
        }
    }

    @Nested
    @DisplayName("updateUserFromHanyangUniversity()")
    public final class TestUpdateUserFromHanyangUniversity {
        private static MockedStatic<HttpRequest> httpRequest;

        @Captor
        private ArgumentCaptor<List<AuthorityDto>> authListCaptor;

        private final String apiToken = "";

        private UniversityDto univ;
        private UUID univId;
        private String apiUrl;
        private String clientKey;

        private UserDto targetUser;
        private String targetStudentId;

        private String newName;
        private String newMajorCode;

        private List<DepartmentDto> deptsByUniv;
        private List<DepartmentDto> newDepts;

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
            setNewMajorCode(randomMajor().code());

            deptsByUniv = stub.ALL_DEPTS.stream()
                    .filter((dept) -> dept.university().matchId(univ))
                    .toList();
        }

        private void setUniv() {
            UniversityInfo univInfo = stub.HYU_UNIV_INIT_INFO;
            univ = stub.HYU_UNIV;
            univId = univ.id();
            apiUrl = univInfo.externalApiInfo().get("url");
            clientKey = univInfo.externalApiInfo().get("clientKey");
        }

        private void setTargetUser(UserDto user) {
            this.targetUser = user;
            this.targetStudentId = user.studentId();
        }

        private void setNewMajorCode(String newMajorCode) {
            this.newMajorCode = newMajorCode;
            newDepts = stub.ALL_DEPTS.stream().filter((dept) -> {
                if (!dept.university().matchId(univ)) return false;
                return dept.baseMajors().stream().anyMatch(major -> newMajorCode.equals(major.code()));
            }).toList();
        }

        private JSONObject makeJsonResponse() {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("gaeinNo", targetStudentId);
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
            when(userDao.getByIndex(univId, targetStudentId)).thenReturn(targetUser);
            when(departmentDao.getListByUniversity(univId)).thenReturn(deptsByUniv);

            execMethod();

            verify(userDao).update(
                    eq(targetUser.id()), eq(univId), eq(targetStudentId), eq(newName),
                    eq(extractEntranceYearFromStudentId(targetStudentId)), any(),
                    eq(targetUser.createdAt()), anyLong(), authListCaptor.capture()
            );
            List<AuthorityDto> authList = authListCaptor.getValue();
            Assertions.assertThat(checkAuthUpdate(authList)).isTrue();
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[새로운 유저이고 새로운 `majorCode`가 기존에 존재 할 시]_[-]")
        public void SUCCESS_userIsNewAndNoMajorCreate() {
            setUpDefault();

            when(initialData.universityInfos()).thenReturn(stub.INIT_DATA.universityInfos());
            when(HttpRequest.getUserInfoFromHanyangApi(apiUrl, clientKey, apiToken)).thenReturn(makeJsonResponse());
            when(departmentDao.getListByUniversity(univId)).thenReturn(deptsByUniv);
            when(userDao.getByIndex(univId, targetStudentId)).thenThrow(NotFoundException.class);

            execMethod();

            verify(userDao).create(any(), eq(univId), eq(targetStudentId), eq(newName),
                    eq(extractEntranceYearFromStudentId(targetStudentId)), any(),
                    anyLong(), anyLong(), authListCaptor.capture()
            );
            List<AuthorityDto> authList = authListCaptor.getValue();
            Assertions.assertThat(checkAuthUpdate(authList)).isTrue();
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[한양 api 통신 과정에 문제가 생겼을 시]_[BadGateWayException]")
        public void ERROR_networkProblemOnHanyangApi_BadGateWayException() {
            setUpDefault();

            when(initialData.universityInfos()).thenReturn(stub.INIT_DATA.universityInfos());
            when(HttpRequest.getUserInfoFromHanyangApi(apiUrl, clientKey, apiToken)).thenThrow(BadGatewayException.class);

            TestHelper.exceptionTest(this::execMethod, BadGatewayException.class);
        }

        private int extractEntranceYearFromStudentId(String studentId) {
            int entranceYear = 0;
            try {
                entranceYear = Integer.parseInt(studentId.substring(0, 4));
            } catch (Exception ignored) { }

            if(entranceYear >  NewUserService.HANYANG_UNIVERSITY_ENTRANCE_YEAR_LOWER_BOUND
                    && entranceYear < NewUserService.HANYANG_UNIVERSITY_ENTRANCE_YEAR_UPPER_BOUND) return entranceYear;
            return 0;
        }

        private boolean checkAuthUpdate(List<AuthorityDto> auths) {
            for (DepartmentDto dept : newDepts) {
                if (auths.stream()
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
        return rs.filter((user) -> user.university().matchId(univ));
    }

    private RandomGetter<UserDto> usersHaveAdditionalAuthOnDept(RandomGetter<UserDto> rs, DepartmentDto dept) {
        return rs.filter((user) -> {
            for (AuthorityDto auth : user.authorities()) {
                if (auth.department().matchId(dept)
                        && auth.permission() != Permission.DEFAULT) return true;
                if (auth.permission() == Permission.DEVELOPER) return true;
            }
            return false;
        });
    }

    private RandomGetter<UserDto> usersNotHaveAdditionalAuthOnDept(RandomGetter<UserDto> rs, DepartmentDto dept) {
        return rs.filter((user) -> {
            for (AuthorityDto auth : user.authorities()) {
                if (auth.department().matchId(dept)
                        && auth.permission() != Permission.DEFAULT) return false;
                if (auth.permission() == Permission.DEVELOPER) return false;
            }
            return true;
        });
    }

    private abstract class UserNestedTest extends BaseNestedTest {
    }
}
