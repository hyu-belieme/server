package com.belieme.apiserver.domain;

import static org.mockito.Mockito.when;

import com.belieme.apiserver.config.initdata.InitialDataConfig;
import com.belieme.apiserver.domain.dao.AuthorityDao;
import com.belieme.apiserver.domain.dao.DepartmentDao;
import com.belieme.apiserver.domain.dao.HistoryDao;
import com.belieme.apiserver.domain.dao.ItemDao;
import com.belieme.apiserver.domain.dao.MajorDao;
import com.belieme.apiserver.domain.dao.StuffDao;
import com.belieme.apiserver.domain.dao.UniversityDao;
import com.belieme.apiserver.domain.dao.UserDao;
import com.belieme.apiserver.domain.dto.DepartmentDto;
import com.belieme.apiserver.domain.dto.HistoryDto;
import com.belieme.apiserver.domain.dto.ItemDto;
import com.belieme.apiserver.domain.dto.MajorDto;
import com.belieme.apiserver.domain.dto.StuffDto;
import com.belieme.apiserver.domain.dto.UniversityDto;
import com.belieme.apiserver.domain.dto.UserDto;
import com.belieme.apiserver.domain.dto.enumeration.Permission;
import com.belieme.apiserver.domain.exception.TokenExpiredException;
import com.belieme.apiserver.domain.service.BaseService;
import com.belieme.apiserver.error.exception.NotFoundException;
import com.belieme.apiserver.error.exception.UnauthorizedException;
import com.belieme.apiserver.util.RandomGetter;
import com.belieme.apiserver.util.StubWithInitialData;
import com.belieme.apiserver.util.TestHelper;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public abstract class BaseServiceTest {

  protected StubWithInitialData stub = new StubWithInitialData();

  @Mock
  protected InitialDataConfig initialData;

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
    protected UUID requesterId;

    protected abstract void setUpDefault();

    protected abstract Object execMethod();

    protected void setRequester(UserDto requester) {
      this.requester = requester;
      this.requesterId = requester.id();
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[토큰이 검증되지 않을 시]_[UnauthorizedException]")
    public void ERROR_isUnauthorizedToken_UnauthorizedException() {
      setUpDefault();

      when(userDao.getByToken(userToken)).thenThrow(NotFoundException.class);

      TestHelper.exceptionTest(this::execMethod, UnauthorizedException.class);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[토큰이 만료되었을 시]_[ExpiredTokenException]")
    public void ERROR_isExpiredToken_ExpiredTokenException() {
      setUpDefault();
      long newApprovedAt = requester.approvedAt() - BaseService.TOKEN_EXPIRED_TIME - 10;

      when(userDao.getByToken(userToken)).thenReturn(requester.withApprovedAt(newApprovedAt));

      TestHelper.exceptionTest(this::execMethod, TokenExpiredException.class);
    }
  }

  protected abstract class BaseNestedTestWithDept extends BaseNestedTest {

    protected DepartmentDto dept;
    protected UUID deptId;

    protected abstract void setUpDefault();

    protected abstract Object execMethod();

    protected void setDept(DepartmentDto dept) {
      this.dept = dept;
      this.deptId = dept.id();
    }

    protected void setRequesterAccessDenied() {
      requester = randomUserHaveLessPermissionOnDept(dept, Permission.USER);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[토큰이 검증되지 않을 시]_[UnauthorizedException]")
    public void ERROR_isUnauthorizedToken_UnauthorizedException() {
      setUpDefault();

      when(userDao.getByToken(userToken)).thenThrow(NotFoundException.class);

      TestHelper.exceptionTest(this::execMethod, UnauthorizedException.class);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[권한이 없을 시]_[PermissionDeniedException]")
    public abstract void ERROR_accessDenied_PermissionDeniedException();
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

  protected RandomGetter<Permission> allPermissions() {
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

  protected RandomGetter<UserDto> usersHaveLessPermissionOnDept(RandomGetter<UserDto> rs,
      DepartmentDto dept, Permission permission) {
    return rs.filter((user) -> !user.getMaxPermission(dept).hasMorePermission(permission));
  }

  protected RandomGetter<UserDto> usersHaveMorePermissionOnDept(RandomGetter<UserDto> rs,
      DepartmentDto dept, Permission permission) {
    return rs.filter((user) -> user.getMaxPermission(dept).hasMorePermission(permission));
  }

  protected RandomGetter<UserDto> usersHaveExactPermissionOnDept(RandomGetter<UserDto> rs,
      DepartmentDto dept, Permission permission) {
    return rs.filter((user) -> user.getMaxPermission(dept) == permission);
  }

  protected RandomGetter<StuffDto> stuffsOnDept(RandomGetter<StuffDto> rs, DepartmentDto dept) {
    return rs.filter((stuff) -> stuff.department().matchId(dept));
  }

  protected RandomGetter<ItemDto> itemsOnDept(RandomGetter<ItemDto> rs, DepartmentDto dept) {
    return rs.filter((item) -> item.stuff().department().matchId(dept));
  }

  protected RandomGetter<HistoryDto> historiesOnDept(RandomGetter<HistoryDto> rs,
      DepartmentDto dept) {
    return rs.filter((history) -> history.item().stuff().department().matchId(dept));
  }

  protected <T> T randomSelectAndLog(RandomGetter<T> rs) {
    T output = rs.randomSelect();
    System.out.println(output);
    return output;
  }

  protected MajorDto randomMajor() {
    return randomSelectAndLog(allMajors());
  }

  protected DepartmentDto randomDept() {
    return randomSelectAndLog(allDepts());
  }

  protected UserDto randomUser() {
    return randomSelectAndLog(allUsers());
  }

  protected UserDto randomDevUser() {
    return randomSelectAndLog(devs(allUsers()));
  }

  protected UserDto randomNonDevUser() {
    return randomSelectAndLog(usersNotDev(allUsers()));
  }

  protected UserDto randomUserHaveMorePermissionOnDept(DepartmentDto dept, Permission permission) {
    return randomSelectAndLog(usersHaveMorePermissionOnDept(allUsers(), dept, permission));
  }

  protected UserDto randomUserHaveLessPermissionOnDept(DepartmentDto dept, Permission permission) {
    return randomSelectAndLog(usersHaveLessPermissionOnDept(allUsers(), dept, permission));
  }

  protected UserDto randomUserHaveExactPermissionOnDept(DepartmentDto dept, Permission permission) {
    return randomSelectAndLog(usersHaveExactPermissionOnDept(allUsers(), dept, permission));
  }

  protected StuffDto randomStuffOnDept(DepartmentDto dept) {
    return randomSelectAndLog(stuffsOnDept(allStuffs(), dept));
  }

  protected ItemDto randomItemOnDept(DepartmentDto dept) {
    return randomSelectAndLog(itemsOnDept(allItems(), dept));
  }

  protected HistoryDto randomHistoryOnDept(DepartmentDto dept) {
    return randomSelectAndLog(historiesOnDept(allHistories(), dept));
  }

  protected long currentTime() {
    return System.currentTimeMillis() / 1000;
  }
}
