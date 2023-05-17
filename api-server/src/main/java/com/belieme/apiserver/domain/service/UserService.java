package com.belieme.apiserver.domain.service;

import com.belieme.apiserver.config.initdata.InitialDataConfig;
import com.belieme.apiserver.config.initdata.container.AuthorityInfo;
import com.belieme.apiserver.config.initdata.container.UniversityInfo;
import com.belieme.apiserver.config.initdata.container.UserInfo;
import com.belieme.apiserver.domain.dao.AuthorityDao;
import com.belieme.apiserver.domain.dao.DepartmentDao;
import com.belieme.apiserver.domain.dao.HistoryDao;
import com.belieme.apiserver.domain.dao.ItemDao;
import com.belieme.apiserver.domain.dao.MajorDao;
import com.belieme.apiserver.domain.dao.StuffDao;
import com.belieme.apiserver.domain.dao.UniversityDao;
import com.belieme.apiserver.domain.dao.UserDao;
import com.belieme.apiserver.domain.dto.AuthorityDto;
import com.belieme.apiserver.domain.dto.DepartmentDto;
import com.belieme.apiserver.domain.dto.UserDto;
import com.belieme.apiserver.domain.dto.enumeration.Permission;
import com.belieme.apiserver.domain.exception.PermissionDeniedException;
import com.belieme.apiserver.domain.util.HttpRequest;
import com.belieme.apiserver.error.exception.ForbiddenException;
import com.belieme.apiserver.error.exception.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import org.json.simple.JSONObject;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Service
public class UserService extends BaseService {

  public static final String DEVELOPER_UNIVERSITY_KEY = "DEV";
  public static final String HANYANG_UNIVERSITY_KEY = "HYU";

  public static final int HANYANG_UNIVERSITY_ENTRANCE_YEAR_LOWER_BOUND = 1900;
  public static final int HANYANG_UNIVERSITY_ENTRANCE_YEAR_UPPER_BOUND = 2500;

  public UserService(
      InitialDataConfig initialData, UniversityDao universityDao, DepartmentDao departmentDao,
      UserDao userDao, MajorDao majorDao, AuthorityDao authorityDao, StuffDao stuffDao,
      ItemDao itemDao, HistoryDao historyDao) {
    super(initialData, universityDao, departmentDao, userDao, majorDao, authorityDao, stuffDao,
        itemDao, historyDao);
  }

  public List<UserDto> getAllList(
      @NonNull String userToken
  ) {
    UserDto requester = validateTokenAndGetUser(userToken);
    checkDeveloperPermission(requester);
    return userDao.getAllList();
  }

  public List<UserDto> getListByDepartment(
      @NonNull String userToken, @NonNull UUID departmentId
  ) {
    UserDto requester = validateTokenAndGetUser(userToken);
    DepartmentDto department = getDepartmentOrThrowInvalidIndexException(departmentId);
    checkMasterPermission(requester, department);

    List<UserDto> output = new ArrayList<>();
    for (UserDto user : userDao.getAllList()) {
      if (user.getMaxPermission(department).hasMorePermission(Permission.USER)) {
        output.add(user);
      }
    }

    return output;
  }

  public UserDto getById(
      @NonNull String userToken, @NonNull UUID userId
  ) {
    UserDto requester = validateTokenAndGetUser(userToken);
    checkDeveloperPermission(requester);
    return userDao.getById(userId);
  }

  public UserDto getByToken(
      @NonNull String userToken
  ) {
    return validateTokenAndGetUser(userToken);
  }

  public UserDto updateAuthorityOfUser(
      @NonNull String userToken,
      @NonNull UUID userId,
      @NonNull UUID departmentId,
      Permission newPermission
  ) {
    UserDto requester = validateTokenAndGetUser(userToken);

    DepartmentDto department = getDepartmentOrThrowInvalidIndexException(departmentId);
    checkMasterPermission(requester, department);

    UserDto targetUser = userDao.getById(userId);
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

    return userDao.update(
        userId,
        targetUser.university().id(),
        targetUser.studentId(),
        targetUser.name(),
        targetUser.entranceYear(),
        UUID.randomUUID().toString(),
        targetUser.createdAt(),
        currentTime(),
        updateAuthorities(targetUser.authorities(), department, newPermission)
    );
  }

  private List<AuthorityDto> updateAuthorities(List<AuthorityDto> authorities,
      DepartmentDto department, Permission newPermission) {
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

  public UUID getDeveloperUniversityId() {
    return initialData.universityInfos().get(UserService.DEVELOPER_UNIVERSITY_KEY).id();
  }

  public UUID getHanyangUniversityId() {
    return initialData.universityInfos().get(UserService.HANYANG_UNIVERSITY_KEY).id();
  }

  public @Nullable
  UserDto reloadInitialUser(@NonNull UUID universityId, @NonNull String apiToken) {
    UserInfo targetUserInfo = null;
    for (UserInfo userInfo : initialData.userInfos()) {
      if (universityId.equals(userInfo.universityId())
          && userInfo.apiToken().equals(apiToken)) {
        targetUserInfo = userInfo;
        break;
      }
    }
      if (targetUserInfo == null) {
          return null;
      }
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

    return updateOrCreateUser(hyuInfo.id(), studentId, name, entranceYear, majorCodes);
  }

  private int extractEntranceYearFromStudentId(String studentId) {
    int entranceYear = 0;
    try {
      entranceYear = Integer.parseInt(studentId.substring(0, 4));
    } catch (Exception ignored) {
    }

      if (entranceYear > HANYANG_UNIVERSITY_ENTRANCE_YEAR_LOWER_BOUND
          && entranceYear < HANYANG_UNIVERSITY_ENTRANCE_YEAR_UPPER_BOUND) {
          return entranceYear;
      }
    return 0;
  }

  private UserDto updateOrCreateUser(UserInfo userInfo) {
    UserDto targetUser = getOrNull(userInfo.id());
    return updateOrCreateUser(
        targetUser,
        userInfo.id(),
        userInfo.universityId(),
        userInfo.studentId(),
        userInfo.name(),
        userInfo.entranceYear(),
        toAuthorityDtoList(userInfo.authorities())
    );
  }

  private UserDto updateOrCreateUser(UUID universityId, String studentId, String name,
      int entranceYear, List<String> majorCodes) {
    UserDto targetUser = getOrNull(universityId, studentId);

    List<AuthorityDto> oldAuthorities = new ArrayList<>();
      if (targetUser != null) {
          oldAuthorities = targetUser.authorities();
      }

    return updateOrCreateUser(
        targetUser,
        null,
        universityId,
        studentId,
        name,
        entranceYear,
        makeNewAuthorities(oldAuthorities, universityId, majorCodes)
    );
  }

  private UserDto updateOrCreateUser(UserDto targetUser, UUID userId, UUID universityId,
      String studentId, String name, int entranceYear, List<AuthorityDto> authorities) {
      if (userId == null) {
          userId = UUID.randomUUID();
      }
    if (targetUser == null) {
      return userDao.create(
          userId,
          universityId,
          studentId,
          name,
          entranceYear,
          UUID.randomUUID().toString(),
          currentTime(),
          currentTime(),
          authorities
      );
    }
    return userDao.update(
        targetUser.id(),
        universityId,
        studentId,
        name,
        entranceYear,
        UUID.randomUUID().toString(),
        targetUser.createdAt(),
        currentTime(),
        authorities
    );
  }

  private UserDto getOrNull(UUID userId) {
    try {
      return userDao.getById(userId);
    } catch (NotFoundException e) {
      return null;
    }
  }

  private UserDto getOrNull(UUID universityId, String studentId) {
    try {
      return userDao.getByIndex(universityId, studentId);
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

  private List<AuthorityDto> makeNewAuthorities(List<AuthorityDto> oldAuthorities,
      UUID universityId, List<String> newMajorCodes) {
    List<AuthorityDto> newAuthorities = new ArrayList<>(oldAuthorities);
    newAuthorities.removeIf((authority) -> authority.permission() == Permission.DEFAULT);

    List<DepartmentDto> candidateDepartments = departmentDao.getListByUniversity(universityId);
    for (DepartmentDto department : candidateDepartments) {
        if (notContainAnyMajorCodesInBaseMajors(department, newMajorCodes)) {
            continue;
        }

      AuthorityDto newAuthority = new AuthorityDto(department, Permission.DEFAULT);
      if (!newAuthorities.contains(newAuthority)) {
        newAuthorities.add(newAuthority);
      }
    }
    return newAuthorities;
  }

  private boolean notContainAnyMajorCodesInBaseMajors(DepartmentDto department,
      List<String> majorCodes) {
    return department.baseMajors().stream()
        .noneMatch((major) -> majorCodes.contains(major.code()));
  }
}
