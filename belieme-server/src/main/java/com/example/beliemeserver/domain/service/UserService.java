package com.example.beliemeserver.domain.service;

import com.example.beliemeserver.config.initdata.InitialData;
import com.example.beliemeserver.config.initdata.container.AuthorityInfo;
import com.example.beliemeserver.config.initdata.container.UniversityInfo;
import com.example.beliemeserver.config.initdata.container.UserInfo;
import com.example.beliemeserver.domain.dao.*;
import com.example.beliemeserver.domain.dto.AuthorityDto;
import com.example.beliemeserver.domain.dto.DepartmentDto;
import com.example.beliemeserver.domain.dto.UniversityDto;
import com.example.beliemeserver.domain.dto.UserDto;
import com.example.beliemeserver.domain.dto.enumeration.Permission;
import com.example.beliemeserver.domain.exception.PermissionDeniedException;
import com.example.beliemeserver.domain.util.HttpRequest;
import com.example.beliemeserver.error.exception.ForbiddenException;
import com.example.beliemeserver.error.exception.NotFoundException;
import lombok.NonNull;
import org.json.simple.JSONObject;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService extends BaseService {
    public static final String DEVELOPER_UNIVERSITY_KEY = "DEV";
    public static final String HANYANG_UNIVERSITY_KEY = "HYU";

    public UserService(InitialData initialData, UniversityDao universityDao, DepartmentDao departmentDao, UserDao userDao, MajorDao majorDao, AuthorityDao authorityDao, StuffDao stuffDao, ItemDao itemDao, HistoryDao historyDao) {
        super(initialData, universityDao, departmentDao, userDao, majorDao, authorityDao, stuffDao, itemDao, historyDao);
    }


    public List<UserDto> getListByDepartment(
            @NonNull String userToken,
            @NonNull String universityCode, @NonNull String departmentCode
    ) {
        DepartmentDto department = getDepartmentOrThrowInvalidIndexException(universityCode, departmentCode);
        checkMasterPermission(userToken, department);

        List<UserDto> output = new ArrayList<>();
        for (UserDto user : userDao.getAllList()) {
            if (user.getMaxPermission(department).hasMorePermission(Permission.USER)) {
                output.add(user);
            }
        }

        return output;
    }

    public UserDto getByIndex(
            @NonNull String userToken,
            @NonNull String universityCode, @NonNull String studentId
    ) {
        checkDeveloperPermission(userToken);
        return userDao.getByIndex(universityCode, studentId);
    }

    public UserDto getByToken(
            @NonNull String userToken
    ) {
        return validateTokenAndGetUser(userToken);
    }

    public UserDto updateAuthority(
            @NonNull String userToken,
            @NonNull String universityCode, @NonNull String studentId,
            @NonNull String authorityUniversityCode,
            @NonNull String authorityDepartmentCode,
            Permission newPermission
    ) {
        UserDto requester = validateTokenAndGetUser(userToken);

        DepartmentDto department = getDepartmentOrThrowInvalidIndexException(authorityUniversityCode, authorityDepartmentCode);
        checkMasterPermission(department, requester);

        UserDto targetUser = userDao.getByIndex(universityCode, studentId);
        if (targetUser.isDeveloper()) {
            throw new ForbiddenException();
        }
        if (newPermission != null && newPermission.hasDeveloperPermission()) {
            throw new ForbiddenException();
        }

        if (!requester.isDeveloper()) {
            if (targetUser.getMaxPermission(department).hasMasterPermission()) {
                throw new PermissionDeniedException();
            }
            if (newPermission != null && newPermission.hasMasterPermission()) {
                throw new PermissionDeniedException();
            }
        }

        UserDto newUser = targetUser.withAuthorityUpdate(department, newPermission);
        return userDao.update(universityCode, studentId, newUser);
    }

    public String getDeveloperUniversityCode() {
        return initialData.universityInfos().get(UserService.DEVELOPER_UNIVERSITY_KEY).code();
    }

    public String getHanyangUniversityCode() {
        return initialData.universityInfos().get(UserService.HANYANG_UNIVERSITY_KEY).code();
    }

    public @Nullable
    UserDto reloadInitialUser(@NonNull String universityCode, @NonNull String apiToken) {
        UserInfo targetUserInfo = null;
        for (UserInfo userInfo : initialData.userInfos()) {
            if (universityCode.equals(userInfo.universityCode())
                    && userInfo.apiToken().equals(apiToken)) {
                targetUserInfo = userInfo;
                break;
            }
        }
        if (targetUserInfo == null) return null;
        return updateOrCreateUser(targetUserInfo);
    }

    public UserDto reloadHanyangUniversityUser(@NonNull String apiToken) {
        UniversityInfo hyuInfo = initialData.universityInfos().get(HANYANG_UNIVERSITY_KEY);
        JSONObject jsonResponse = HttpRequest.getUserInfoFromHanyangApi(
                hyuInfo.externalApiInfo().get("url"),
                hyuInfo.externalApiInfo().get("client-key"),
                apiToken);
        String studentId = (String) (jsonResponse.get("gaeinNo"));
        String name = (String) (jsonResponse.get("userNm"));
        String sosokId = (String) jsonResponse.get("sosokId");
        List<String> majorCodes = List.of(sosokId);

        return updateOrCreateUser(hyuInfo.code(), studentId, name, majorCodes);
    }

    private UserDto updateOrCreateUser(UserInfo userInfo) {
        boolean isNew = false;
        UniversityDto university = universityDao.getByIndex(userInfo.universityCode());

        UserDto targetUser = getOrNull(university, userInfo.studentId());
        if (targetUser == null) {
            targetUser = UserDto.init(university, userInfo.studentId(), userInfo.name());
            isNew = true;
        }

        targetUser = targetUser
                .withUniversity(university)
                .withStudentId(userInfo.studentId())
                .withName(userInfo.name())
                .withAuthorities(toAuthorityDtoList(userInfo.authorities()))
                .withApprovedAt(currentTime())
                .withToken(UUID.randomUUID().toString());

        if (isNew) return userDao.create(targetUser);
        return userDao.update(userInfo.universityCode(), userInfo.studentId(), targetUser);
    }

    private UserDto updateOrCreateUser(String universityCode, String studentId, String name, List<String> majorCodes) {
        boolean isNew = false;
        UniversityDto university = universityDao.getByIndex(universityCode);

        UserDto targetUser = getOrNull(university, studentId);
        if (targetUser == null) {
            targetUser = UserDto.init(university, studentId, name);
            isNew = true;
        }

        List<AuthorityDto> newAuthorities = makeNewAuthorities(targetUser, majorCodes);
        targetUser = targetUser.withName(name)
                .withApprovedAt(currentTime())
                .withAuthorities(newAuthorities)
                .withToken(UUID.randomUUID().toString());

        if (isNew) return userDao.create(targetUser);
        return userDao.update(universityCode, studentId, targetUser);
    }

    private UserDto getOrNull(UniversityDto university, String studentId) {
        try {
            return userDao.getByIndex(university.code(), studentId);
        } catch (NotFoundException e) {
            return null;
        }
    }

    private List<AuthorityDto> toAuthorityDtoList(List<AuthorityInfo> authorityInfos) {
        List<AuthorityDto> newAuthorities = new ArrayList<>();
        for (AuthorityInfo authorityInfo : authorityInfos) {
            String universityCode = authorityInfo.universityCode();
            String departmentCode = authorityInfo.departmentCode();
            String permissionText = authorityInfo.permission();
            DepartmentDto department = departmentDao.getByIndex(universityCode, departmentCode);
            newAuthorities.add(new AuthorityDto(department, Permission.valueOf(permissionText)));
        }
        return newAuthorities;
    }

    private List<AuthorityDto> makeNewAuthorities(UserDto user, List<String> newMajorCodes) {
        List<AuthorityDto> newAuthorities = user.authorities();
        newAuthorities.removeIf((authority) -> authority.permission() == Permission.DEFAULT);

        List<DepartmentDto> candidateDepartments = departmentDao.getListByUniversity(user.university().code());
        for (DepartmentDto department : candidateDepartments) {
            if (notContainAnyMajorCodesInBaseMajors(department, newMajorCodes)) continue;

            AuthorityDto newAuthority = new AuthorityDto(department, Permission.DEFAULT);
            if (!newAuthorities.contains(newAuthority)) {
                newAuthorities.add(newAuthority);
            }
        }
        return newAuthorities;
    }

    private boolean notContainAnyMajorCodesInBaseMajors(DepartmentDto department, List<String> majorCodes) {
        return department.baseMajors().stream()
                .noneMatch((major) -> majorCodes.contains(major.code()));
    }

    private long currentTime() {
        return System.currentTimeMillis() / 1000;
    }
}
