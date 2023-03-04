package com.example.beliemeserver.model.service;

import com.example.beliemeserver.common.DeveloperInfo;
import com.example.beliemeserver.common.Globals;
import com.example.beliemeserver.exception.ForbiddenException;
import com.example.beliemeserver.exception.NotFoundException;
import com.example.beliemeserver.exception.UnauthorizedException;
import com.example.beliemeserver.model.dao.*;
import com.example.beliemeserver.model.dto.AuthorityDto;
import com.example.beliemeserver.model.dto.DepartmentDto;
import com.example.beliemeserver.model.dto.UniversityDto;
import com.example.beliemeserver.model.dto.UserDto;
import com.example.beliemeserver.model.exception.PermissionDeniedException;
import com.example.beliemeserver.model.util.HttpRequest;
import lombok.NonNull;
import org.json.simple.JSONObject;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService extends BaseService {
    public UserService(UniversityDao universityDao, DepartmentDao departmentDao, UserDao userDao, MajorDao majorDao, AuthorityDao authorityDao, StuffDao stuffDao, ItemDao itemDao, HistoryDao historyDao) {
        super(universityDao, departmentDao, userDao, majorDao, authorityDao, stuffDao, itemDao, historyDao);
    }

    public List<UserDto> getListByDepartment(
            @NonNull String userToken,
            @NonNull String universityCode, @NonNull String departmentCode
    ) {
        DepartmentDto department = getDepartmentOrThrowInvalidIndexException(universityCode, departmentCode);
        checkMasterPermission(userToken, department);

        List<UserDto> output = new ArrayList<>();
        for (UserDto user : userDao.getAllList()) {
            if (user.getMaxPermission(department).hasMorePermission(AuthorityDto.Permission.USER)) {
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
            AuthorityDto.Permission newPermission
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

    public UserDto reloadDeveloperUser(@NonNull String apiToken) {
        DeveloperInfo targetDeveloper = null;
        for (DeveloperInfo info : Globals.developers) {
            if (info.apiToken().equals(apiToken)) {
                targetDeveloper = info;
                break;
            }
        }
        if (targetDeveloper == null) {
            throw new UnauthorizedException();
        }

        Pair<Boolean, UserDto> isNewAndUser = getOrMakeDeveloperUser(targetDeveloper.studentId(), targetDeveloper.name());
        boolean isNew = isNewAndUser.getFirst();
        UserDto newUser = isNewAndUser.getSecond();
        newUser = newUser.withApprovalTimeStamp(currentTimestamp())
                .withToken(UUID.randomUUID().toString());

        if (isNew) return userDao.create(newUser);
        return userDao.update(Globals.DEV_UNIVERSITY.code(), targetDeveloper.studentId(), newUser);
    }

    public UserDto reloadHanyangUniversityUser(@NonNull String apiToken) {
        JSONObject jsonResponse = HttpRequest.getUserInfoFromHanyangApi(apiToken);
        String studentId = (String) (jsonResponse.get("gaeinNo"));
        String name = (String) (jsonResponse.get("userNm"));
        String sosokId = (String) jsonResponse.get("sosokId");
        List<String> majorCodes = List.of(sosokId);

        return updateOrInitAndSave(Globals.HANYANG_UNIVERSITY.code(), studentId, name, majorCodes);
    }

    private UserDto updateOrInitAndSave(String universityCode, String studentId, String name, List<String> majorCodes) {
        Pair<Boolean, UserDto> isNewAndUser = getOrMakeUser(universityCode, studentId, name);
        boolean isNew = isNewAndUser.getFirst();
        UserDto newUser = isNewAndUser.getSecond();

        List<AuthorityDto> newAuthorities = makeNewAuthorities(newUser, majorCodes);
        newUser = newUser.withName(name)
                .withApprovalTimeStamp(currentTimestamp())
                .withAuthorities(newAuthorities)
                .withToken(UUID.randomUUID().toString());

        if (isNew) return userDao.create(newUser);
        return userDao.update(universityCode, studentId, newUser);
    }

    private Pair<Boolean, UserDto> getOrMakeUser(String universityCode, String studentId, String name) {
        boolean isNew = false;
        UserDto newUser;
        UniversityDto university = universityDao.getByIndex(universityCode);
        try {
            newUser = userDao.getByIndex(universityCode, studentId);
        } catch (NotFoundException e) {
            isNew = true;
            newUser = UserDto.init(university, studentId, name);
        }
        return Pair.of(isNew, newUser);
    }

    private Pair<Boolean, UserDto> getOrMakeDeveloperUser(String studentId, String name) {
        boolean isNew = false;
        UserDto newUser;
        UniversityDto university = universityDao.getByIndex(Globals.DEV_UNIVERSITY.code());
        try {
            newUser = userDao.getByIndex(Globals.DEV_UNIVERSITY.code(), studentId);
        } catch (NotFoundException e) {
            isNew = true;
            newUser = UserDto.init(university, studentId, name);
            newUser = newUser.withAuthorityAdd(Globals.DEV_AUTHORITY);
        }
        return Pair.of(isNew, newUser);
    }

    private List<AuthorityDto> makeNewAuthorities(UserDto user, List<String> newMajorCodes) {
        List<AuthorityDto> newAuthorities = user.authorities();
        newAuthorities.removeIf((authority) -> authority.permission() == AuthorityDto.Permission.DEFAULT);

        List<DepartmentDto> candidateDepartments = departmentDao.getListByUniversity(user.university().code());
        for (DepartmentDto department : candidateDepartments) {
            if (notContainAnyMajorCodesInBaseMajors(department, newMajorCodes)) continue;

            AuthorityDto newAuthority = new AuthorityDto(department, AuthorityDto.Permission.DEFAULT);
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

    private long currentTimestamp() {
        return System.currentTimeMillis() / 1000;
    }
}
