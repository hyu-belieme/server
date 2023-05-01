package com.example.beliemeserver.domain.service._new;

import com.example.beliemeserver.config.initdata._new.InitialData;
import com.example.beliemeserver.config.initdata._new.container.AuthorityInfo;
import com.example.beliemeserver.config.initdata._new.container.UniversityInfo;
import com.example.beliemeserver.config.initdata._new.container.UserInfo;
import com.example.beliemeserver.domain.dao._new.*;
import com.example.beliemeserver.domain.dto._new.AuthorityDto;
import com.example.beliemeserver.domain.dto._new.DepartmentDto;
import com.example.beliemeserver.domain.dto._new.UniversityDto;
import com.example.beliemeserver.domain.dto._new.UserDto;
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
public class NewUserService extends NewBaseService {
    public static final String DEVELOPER_UNIVERSITY_KEY = "DEV";
    public static final String HANYANG_UNIVERSITY_KEY = "HYU";

    public static final int HANYANG_UNIVERSITY_ENTRANCE_YEAR_LOWER_BOUND = 1900;
    public static final int HANYANG_UNIVERSITY_ENTRANCE_YEAR_UPPER_BOUND = 2500;

    public NewUserService(InitialData initialData, UniversityDao universityDao, DepartmentDao departmentDao, UserDao userDao, MajorDao majorDao, AuthorityDao authorityDao, StuffDao stuffDao, ItemDao itemDao, HistoryDao historyDao) {
        super(initialData, universityDao, departmentDao, userDao, majorDao, authorityDao, stuffDao, itemDao, historyDao);
    }


    public List<UserDto> getListByDepartment(
            @NonNull String userToken,
            @NonNull String universityName, @NonNull String departmentName
    ) {
        DepartmentDto department = getDepartmentOrThrowInvalidIndexException(universityName, departmentName);
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
            @NonNull String universityName, @NonNull String studentId
    ) {
        checkDeveloperPermission(userToken);
        return userDao.getByIndex(universityName, studentId);
    }

    public UserDto getByToken(
            @NonNull String userToken
    ) {
        return validateTokenAndGetUser(userToken);
    }

    public UserDto updateAuthority(
            @NonNull String userToken,
            @NonNull String universityName, @NonNull String studentId,
            @NonNull String authorityUniversityName,
            @NonNull String authorityDepartmentName,
            Permission newPermission
    ) {
        UserDto requester = validateTokenAndGetUser(userToken);

        DepartmentDto department = getDepartmentOrThrowInvalidIndexException(authorityUniversityName, authorityDepartmentName);
        checkMasterPermission(department, requester);

        UserDto targetUser = userDao.getByIndex(universityName, studentId);
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
        return userDao.update(universityName, studentId, newUser);
    }

    public String getDeveloperUniversityName() {
        return initialData.universityInfos().get(NewUserService.DEVELOPER_UNIVERSITY_KEY).name();
    }

    public String getHanyangUniversityName() {
        return initialData.universityInfos().get(NewUserService.HANYANG_UNIVERSITY_KEY).name();
    }

    public @Nullable
    UserDto reloadInitialUser(@NonNull String universityName, @NonNull String apiToken) {
        UserInfo targetUserInfo = null;
        for (UserInfo userInfo : initialData.userInfos()) {
            if (universityName.equals(userInfo.universityName())
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
        int entranceYear = extractEntranceYearFromStudentId(studentId);

        List<String> majorCodes = List.of(sosokId);

        return updateOrCreateUser(hyuInfo.name(), studentId, name, entranceYear, majorCodes);
    }

    private int extractEntranceYearFromStudentId(String studentId) {
        int entranceYear = 0;
        try {
            entranceYear = Integer.parseInt(studentId.substring(0, 4));
        } catch (Exception ignored) { }

        if(entranceYear > HANYANG_UNIVERSITY_ENTRANCE_YEAR_LOWER_BOUND
                && entranceYear < HANYANG_UNIVERSITY_ENTRANCE_YEAR_UPPER_BOUND) return entranceYear;
        return 0;
    }

    private UserDto updateOrCreateUser(UserInfo userInfo) {
        boolean isNew = false;
        UniversityDto university = universityDao.getByIndex(userInfo.universityName());

        UserDto targetUser = getOrNull(university, userInfo.studentId());
        if (targetUser == null) {
            targetUser = UserDto.init(university, userInfo.studentId(), userInfo.name(), userInfo.entranceYear());
            isNew = true;
        }

        targetUser = targetUser
                .withUniversity(university)
                .withStudentId(userInfo.studentId())
                .withName(userInfo.name())
                .withEntranceYear(userInfo.entranceYear())
                .withAuthorities(toAuthorityDtoList(userInfo.authorities()))
                .withApprovedAt(currentTime())
                .withToken(UUID.randomUUID().toString());

        if (isNew) return userDao.create(targetUser);
        return userDao.update(userInfo.universityName(), userInfo.studentId(), targetUser);
    }

    private UserDto updateOrCreateUser(String universityName, String studentId, String name, int entranceYear, List<String> majorCodes) {
        boolean isNew = false;
        UniversityDto university = universityDao.getByIndex(universityName);

        UserDto targetUser = getOrNull(university, studentId);
        if (targetUser == null) {
            targetUser = UserDto.init(university, studentId, name, entranceYear);
            isNew = true;
        }

        List<AuthorityDto> newAuthorities = makeNewAuthorities(targetUser, majorCodes);
        targetUser = targetUser.withName(name)
                .withEntranceYear(entranceYear)
                .withApprovedAt(currentTime())
                .withAuthorities(newAuthorities)
                .withToken(UUID.randomUUID().toString());

        if (isNew) return userDao.create(targetUser);
        return userDao.update(universityName, studentId, targetUser);
    }

    private UserDto getOrNull(UniversityDto university, String studentId) {
        try {
            return userDao.getByIndex(university.name(), studentId);
        } catch (NotFoundException e) {
            return null;
        }
    }

    private List<AuthorityDto> toAuthorityDtoList(List<AuthorityInfo> authorityInfos) {
        List<AuthorityDto> newAuthorities = new ArrayList<>();
        for (AuthorityInfo authorityInfo : authorityInfos) {
            UUID departmentId = authorityInfo.departmentId();
            String permissionText = authorityInfo.permission();
            DepartmentDto department = departmentDao.getById(departmentId);
            newAuthorities.add(new AuthorityDto(department, Permission.valueOf(permissionText)));
        }
        return newAuthorities;
    }

    private List<AuthorityDto> makeNewAuthorities(UserDto user, List<String> newMajorCodes) {
        List<AuthorityDto> newAuthorities = user.authorities();
        newAuthorities.removeIf((authority) -> authority.permission() == Permission.DEFAULT);

        List<DepartmentDto> candidateDepartments = departmentDao.getListByUniversity(user.university().name());
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
