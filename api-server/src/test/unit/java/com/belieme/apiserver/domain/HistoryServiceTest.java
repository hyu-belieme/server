package com.belieme.apiserver.domain;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.belieme.apiserver.domain.dto.DepartmentDto;
import com.belieme.apiserver.domain.dto.HistoryDto;
import com.belieme.apiserver.domain.dto.ItemDto;
import com.belieme.apiserver.domain.dto.StuffDto;
import com.belieme.apiserver.domain.dto.UserDto;
import com.belieme.apiserver.domain.dto.enumeration.ItemStatus;
import com.belieme.apiserver.domain.dto.enumeration.Permission;
import com.belieme.apiserver.domain.exception.LostRegistrationRequestedOnLostItemException;
import com.belieme.apiserver.domain.exception.PermissionDeniedException;
import com.belieme.apiserver.domain.exception.RentalCountLimitExceededException;
import com.belieme.apiserver.domain.exception.RentalCountOnSameStuffLimitExceededException;
import com.belieme.apiserver.domain.exception.ReservationRequestedOnNonUsableItemException;
import com.belieme.apiserver.domain.exception.RespondedOnUnrequestedItemException;
import com.belieme.apiserver.domain.exception.ReturnRegistrationRequestedOnReturnedItemException;
import com.belieme.apiserver.domain.exception.UsableItemNotExistedException;
import com.belieme.apiserver.domain.service.HistoryService;
import com.belieme.apiserver.domain.util.Constants;
import com.belieme.apiserver.error.exception.InvalidIndexException;
import com.belieme.apiserver.error.exception.NotFoundException;
import com.belieme.apiserver.util.RandomGetter;
import com.belieme.apiserver.util.TestHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class HistoryServiceTest extends BaseServiceTest {

  private final DepartmentDto TEST_DEPT = stub.HYU_CSE_DEPT;

  @InjectMocks
  private HistoryService historyService;

  @Nested
  @DisplayName("getListByDepartment")
  public final class TestGetListByDepartment extends HistoryNestedTest {

    private List<HistoryDto> historyList;

    @Override
    protected void setUpDefault() {
      setDept(TEST_DEPT);
      setRequester(randomUserHaveMorePermissionOnDept(
          dept, Permission.STAFF));

      historyList = getHistoryListByDept(dept);
    }

    @Override
    protected void setRequesterAccessDenied() {
      setRequester(randomUserHaveLessPermissionOnDept(
          dept, Permission.STAFF));
    }

    @Override
    protected List<HistoryDto> execMethod() {
      return historyService.getListByDepartment(userToken, deptId);
    }

    @RepeatedTest(10)
    @DisplayName("[SUCCESS]_[-]_[-]")
    public void SUCCESS() {
      setUpDefault();

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(departmentDao.getById(deptId)).thenReturn(dept);
      when(historyDao.getListByDepartment(deptId)).thenReturn(historyList);

      System.out.println("Expected : " + historyList);
      TestHelper.listCompareTest(
          this::execMethod,
          historyList
      );
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

    private List<HistoryDto> getHistoryListByDept(DepartmentDto dept) {
      return stub.ALL_HISTORIES.stream()
          .filter((history) -> dept.matchId(history.item().stuff().department()))
          .collect(Collectors.toList());
    }
  }

  @Nested
  @DisplayName("getListByStuff()")
  public final class TestGetListByStuff extends HistoryNestedTest {

    private StuffDto stuff;
    private UUID stuffId;

    private List<HistoryDto> historyList;

    @Override
    protected void setUpDefault() {
      setDept(TEST_DEPT);
      setRequester(randomUserHaveMorePermissionOnDept(dept, Permission.STAFF));
      setStuff(randomStuffOnDept(dept));
      historyList = getHistoryListByStuff(stuff);
    }

    private void setStuff(StuffDto stuff) {
      this.stuff = stuff;
      this.stuffId = stuff.id();
    }

    @Override
    protected void setRequesterAccessDenied() {
      setRequester(randomUserHaveLessPermissionOnDept(
          dept, Permission.STAFF));
    }

    @Override
    protected List<HistoryDto> execMethod() {
      return historyService.getListByStuff(userToken, stuffId);
    }

    @RepeatedTest(10)
    @DisplayName("[SUCCESS]_[-]_[-]")
    public void SUCCESS() {
      setUpDefault();

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(stuffDao.getById(stuffId)).thenReturn(stuff);
      when(historyDao.getListByStuff(stuffId)).thenReturn(historyList);

      System.out.println("Expected : " + historyList);
      TestHelper.listCompareTest(
          this::execMethod,
          historyList
      );
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[해당 `index`의 `stuff`가 존재하지 않을 시]_[InvalidIndexException]")
    public void ERROR_stuffInvalidIndex_InvalidIndexException() {
      setUpDefault();

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(stuffDao.getById(stuffId)).thenReturn(stuff);
      when(historyDao.getListByStuff(stuffId)).thenThrow(InvalidIndexException.class);

      TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
    }

    @Override
    @RepeatedTest(10)
    @DisplayName("[ERROR]_[권한이 없을 시]_[PermissionDeniedException]")
    public void ERROR_accessDenied_PermissionDeniedException() {
      setUpDefault();
      setRequesterAccessDenied();

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(stuffDao.getById(stuffId)).thenReturn(stuff);

      TestHelper.exceptionTest(this::execMethod, PermissionDeniedException.class);
    }

    private List<HistoryDto> getHistoryListByStuff(StuffDto stuff) {
      return stub.ALL_HISTORIES.stream()
          .filter((history) -> stuff.matchId(history.item().stuff()))
          .collect(Collectors.toList());
    }
  }

  @Nested
  @DisplayName("getListByItem()")
  public final class TestGetListByItem extends HistoryNestedTest {

    private ItemDto item;
    private UUID itemId;

    private List<HistoryDto> historyList;

    private void setItem(ItemDto item) {
      this.item = item;
      this.itemId = item.id();
    }

    @Override
    protected void setUpDefault() {
      setDept(TEST_DEPT);
      setRequester(randomUserHaveMorePermissionOnDept(dept, Permission.STAFF));
      setItem(randomItemOnDept(dept));

      historyList = getHistoryListByItem(item);
    }

    @Override
    protected void setRequesterAccessDenied() {
      setRequester(randomUserHaveLessPermissionOnDept(dept, Permission.STAFF));
    }

    @Override
    protected List<HistoryDto> execMethod() {
      return historyService.getListByItem(userToken, itemId);
    }

    @RepeatedTest(10)
    @DisplayName("[SUCCESS]_[-]_[-]")
    public void SUCCESS() {
      setUpDefault();

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(itemDao.getById(itemId)).thenReturn(item);
      when(historyDao.getListByItem(itemId)).thenReturn(historyList);

      System.out.println("Expected : " + historyList);
      TestHelper.listCompareTest(
          this::execMethod,
          historyList
      );
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[해당 `index`의 `item`가 존재하지 않을 시]_[InvalidIndexException]")
    public void ERROR_itemInvalidIndex_InvalidIndexException() {
      setUpDefault();

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(itemDao.getById(itemId)).thenReturn(item);
      when(historyDao.getListByItem(itemId)).thenThrow(InvalidIndexException.class);

      TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
    }

    @Override
    @RepeatedTest(10)
    @DisplayName("[ERROR]_[권한이 없을 시]_[PermissionDeniedException]")
    public void ERROR_accessDenied_PermissionDeniedException() {
      setUpDefault();
      setRequesterAccessDenied();

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(itemDao.getById(itemId)).thenReturn(item);

      TestHelper.exceptionTest(this::execMethod, PermissionDeniedException.class);
    }

    private List<HistoryDto> getHistoryListByItem(ItemDto item) {
      return stub.ALL_HISTORIES.stream()
          .filter((history) -> item.matchId(history.item()))
          .collect(Collectors.toList());
    }
  }

  @Nested
  @DisplayName("getListByDepartmentAndRequester()")
  public final class TestGetListByDepartmentAndRequester extends HistoryNestedTest {

    private UserDto historyRequester;
    private UUID historyRequesterId;

    private List<HistoryDto> historyList;

    @Override
    protected void setUpDefault() {
      setDept(TEST_DEPT);
      setRequester(randomUserHaveExactPermissionOnDept(dept, Permission.USER));

      setHistoryRequester(requester);
      historyList = getHistoryListByDeptAndRequester(dept, historyRequester);
    }

    private void setHistoryRequester(UserDto historyRequester) {
      this.historyRequester = historyRequester;
      this.historyRequesterId = historyRequester.id();
    }

    @Override
    protected List<HistoryDto> execMethod() {
      return historyService.getListByDepartmentAndRequester(
          userToken, deptId, historyRequesterId
      );
    }

    @RepeatedTest(10)
    @DisplayName("[SUCCESS]_[본인의 `History List`에 대한 `request`일 시]_[-]")
    public void SUCCESS_getHistoryListHerSelf() {
      setUpDefault();

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(departmentDao.getById(deptId)).thenReturn(dept);
      when(userDao.getById(historyRequesterId)).thenReturn(historyRequester);
      when(historyDao.getListByDepartmentAndRequester(deptId, historyRequesterId))
          .thenReturn(historyList);

      TestHelper.listCompareTest(this::execMethod, historyList);
    }

    @RepeatedTest(10)
    @DisplayName("[SUCCESS]_[타인의 `History List`에 대한 `request`가 아니지만 `requester`가 `staff` 이상의 권한을 가질 시]_[-]")
    public void SUCCESS_getHistoryListOfOthers() {
      setDept(TEST_DEPT);
      setRequester(randomUserHaveExactPermissionOnDept(
          dept, Permission.STAFF));
      setHistoryRequester(randomUserHaveExactPermissionOnDept(
          dept, Permission.USER));
      historyList = getHistoryListByDeptAndRequester(dept, historyRequester);

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(departmentDao.getById(deptId)).thenReturn(dept);
      when(userDao.getById(historyRequesterId)).thenReturn(historyRequester);
      when(historyDao.getListByDepartmentAndRequester(deptId, historyRequesterId))
          .thenReturn(historyList);

      System.out.println("Expected : " + historyList);
      TestHelper.listCompareTest(this::execMethod, historyList);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[권한이 없을 시]_[PermissionDeniedException]")
    @Override
    public void ERROR_accessDenied_PermissionDeniedException() {
      setDept(TEST_DEPT);
      setRequester(randomUserHaveExactPermissionOnDept(
          dept, Permission.BANNED));
      setHistoryRequester(randomUserHaveMorePermissionOnDept(
          dept, Permission.USER));

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(departmentDao.getById(deptId)).thenReturn(dept);
      when(userDao.getById(historyRequesterId)).thenReturn(historyRequester);

      TestHelper.exceptionTest(this::execMethod, PermissionDeniedException.class);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[본인의 `History List`에 대한 `request`가 아닐 시]_[PermissionDeniedException]")
    public void ERROR_getHistoryListOfOthers_PermissionDeniedException() {
      setDept(TEST_DEPT);
      setHistoryRequester(randomUserHaveMorePermissionOnDept(
          dept, Permission.USER));
      setRequester(randomUserHaveExactPermissionOnDeptWithExclude(
          dept, Permission.USER, List.of(historyRequester)));

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(departmentDao.getById(deptId)).thenReturn(dept);
      when(userDao.getById(historyRequesterId)).thenReturn(historyRequester);

      TestHelper.exceptionTest(this::execMethod, PermissionDeniedException.class);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[`department`의 `index`가 유효하지 않을 시]_[InvalidException]")
    public void ERROR_deptInvalidIndex_InvalidException() {
      setDept(TEST_DEPT);
      setRequester(randomUserHaveExactPermissionOnDept(
          dept, Permission.STAFF));
      setHistoryRequester(randomUserHaveExactPermissionOnDept(
          dept, Permission.USER));

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(departmentDao.getById(deptId)).thenThrow(NotFoundException.class);

      TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[`History Requester`의 `index`가 유효하지 않을 시]_[InvalidException]")
    public void ERROR_userInvalidIndex_InvalidException() {
      setDept(TEST_DEPT);
      setRequester(randomUserHaveExactPermissionOnDept(
          dept, Permission.STAFF));
      setHistoryRequester(randomUserHaveExactPermissionOnDept(
          dept, Permission.USER));

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(departmentDao.getById(deptId)).thenReturn(dept);
      when(userDao.getById(historyRequesterId)).thenThrow(NotFoundException.class);

      TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
    }

    private List<HistoryDto> getHistoryListByDeptAndRequester(DepartmentDto dept,
        UserDto requester) {
      return stub.ALL_HISTORIES.stream()
          .filter((history) -> dept.matchId(history.item().stuff().department())
              && requester.matchId(history.requester()))
          .collect(Collectors.toList());
    }
  }

  @Nested
  @DisplayName("getByIndex()")
  public final class TestGetByIndex extends HistoryNestedTest {

    private HistoryDto history;
    private UUID historyId;

    @Override
    protected void setUpDefault() {
      setDept(TEST_DEPT);
      setRequester(randomUserHaveExactPermissionOnDept(
          dept, Permission.USER));
      setHistory(randomHistoryOnDept(dept));
    }

    private void setHistory(HistoryDto history) {
      this.history = history;
      this.historyId = history.id();
    }

    @Override
    protected HistoryDto execMethod() {
      return historyService.getById(userToken, historyId);
    }

    @RepeatedTest(10)
    @DisplayName("[SUCCESS]_[본인의 `History`에 대한 `request`일 시]_[-]")
    public void SUCCESS_getHistoryListHerSelf() {
      setUpDefault();
      setRequester(history.requester());

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(historyDao.getById(historyId)).thenReturn(history);

      System.out.println("Expected : " + history);
      TestHelper.objectCompareTest(this::execMethod, history);
    }

    @RepeatedTest(10)
    @DisplayName("[SUCCESS]_[타인의 `History`에 대한 `request`가 아니지만 `requester`가 `staff` 이상의 권한을 가질 시]_[-]")
    public void SUCCESS_getHistoryListOfOthers() {
      setUpDefault();
      setRequester(randomUserHaveExactPermissionOnDept(
          dept, Permission.STAFF));

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(historyDao.getById(historyId)).thenReturn(history);

      System.out.println("Expected : " + history);
      TestHelper.objectCompareTest(this::execMethod, history);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[권한이 없을 시]_[PermissionDeniedException]")
    @Override
    public void ERROR_accessDenied_PermissionDeniedException() {
      setUpDefault();
      setRequester(randomUserHaveLessPermissionOnDept(
          dept, Permission.USER));

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(historyDao.getById(historyId)).thenReturn(history);

      TestHelper.exceptionTest(this::execMethod, PermissionDeniedException.class);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[본인의 `History List`에 대한 `request`가 아닐 시]_[PermissionDeniedException]")
    public void ERROR_getHistoryListOfOthers_PermissionDeniedException() {
      setUpDefault();
      setRequester(randomUserHaveExactPermissionOnDeptWithExclude(
          dept, Permission.USER, List.of(history.requester())));

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(historyDao.getById(historyId)).thenReturn(history);

      TestHelper.exceptionTest(this::execMethod, PermissionDeniedException.class);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[`index`에 해당하는 `History`가 존재하지 않을 시]_[NotFoundException]")
    public void ERROR_userInvalidIndex_InvalidException() {
      setUpDefault();

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(historyDao.getById(historyId)).thenThrow(NotFoundException.class);

      TestHelper.exceptionTest(this::execMethod, NotFoundException.class);
    }
  }

  @Nested
  @DisplayName("createReservationOnItem()")
  public final class TestCreateReservationOnItem extends HistoryNestedTest {

    private ItemDto item;
    private UUID itemId;

    private HistoryDto createdHistory;

    @Override
    protected void setUpDefault() {
      setDept(TEST_DEPT);
      setRequester(randomUserHaveMorePermissionOnDept(dept, Permission.USER));
      setItem(randomUsableItemOnDept(dept));

      createdHistory = new HistoryDto(
          UUID.randomUUID(), item, item.nextHistoryNum(), requester,
          null, null, null, null,
          currentTime(), 0, 0, 0, 0
      );
    }

    private void setItem(ItemDto item) {
      this.item = item;
      this.itemId = item.id();
    }

    @Override
    protected Object execMethod() {
      return historyService.createReservationOnItem(userToken, itemId);
    }

    @RepeatedTest(10)
    @DisplayName("[SUCCESS]_[-]_[-]")
    public void SUCCESS_() {
      setUpDefault();

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(itemDao.getById(itemId)).thenReturn(item);
      when(historyDao.getListByDepartmentAndRequester(deptId, requesterId)).thenReturn(
          new ArrayList<>());

      when(historyDao.create(
          any(), eq(itemId), eq(item.nextHistoryNum()), eq(requesterId),
          eq(null), eq(null), eq(null), eq(null),
          anyLong(), eq(0L), eq(0L), eq(0L), eq(0L))
      ).thenReturn(createdHistory);
      when(itemDao.update(itemId, item.stuff().id(), item.num(), createdHistory.id())).thenReturn(
          item);

      execMethod();

      verify(itemDao).update(itemId, item.stuff().id(), item.num(), createdHistory.id());
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[`item`이 대여가 불가능 한 상태일 시 1]_[ReservationOnNonUsableItemException]")
    public void ERROR_itemIsUnusable_ReservationOnNonUsableItemException() {
      setUpDefault();
      setItem(randomUnusableItemByDept(dept));

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(itemDao.getById(itemId)).thenReturn(item);

      TestHelper.exceptionTest(this::execMethod,
          ReservationRequestedOnNonUsableItemException.class);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[`item`이 대여가 불가능 한 상태일 시 2]_[ReservationOnNonUsableItemException]")
    public void ERROR_itemIsInactive_ReservationOnNonUsableItemException() {
      setUpDefault();
      setItem(randomInactiveItemByDept(dept));

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(itemDao.getById(itemId)).thenReturn(item);

      TestHelper.exceptionTest(this::execMethod,
          ReservationRequestedOnNonUsableItemException.class);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[`requester`가 이미 해당 물품을 " + Constants.MAX_RENTAL_COUNT_ON_SAME_STUFF
        + "개 사용 중일 시]_[ExceedMaxRentalCountOnSameStuffException]")
    public void ERROR_requesterAlreadyRequestStuff_ExceedMaxRentalCountOnSameStuffException() {
      setUpDefault();
      StuffDto targetStuff = randomUsableStuffHaveItemMoreOnDept(dept,
          Constants.MAX_RENTAL_COUNT_ON_SAME_STUFF);
      setItem(randomUsableItemOnStuff(targetStuff));

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(historyDao.getListByDepartmentAndRequester(deptId, requesterId))
          .thenReturn(makeHistoryListWithSameStuff(item, requester));
      when(itemDao.getById(itemId)).thenReturn(item);

      TestHelper.exceptionTest(this::execMethod,
          RentalCountOnSameStuffLimitExceededException.class);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[`requester`가 이미 " + Constants.MAX_RENTAL_COUNT
        + "개 이상에 대한 대여가 진행 중일 시]_[ExceedMaxRentalCountException]")
    public void ERROR_requesterHasTooManyRequest_ExceedMaxRentalCountException() {
      setUpDefault();

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(historyDao.getListByDepartmentAndRequester(deptId, requesterId))
          .thenReturn(makeHistoryListWithSameRequester(item, requester));
      when(itemDao.getById(itemId)).thenReturn(item);

      TestHelper.exceptionTest(this::execMethod, RentalCountLimitExceededException.class);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[`item`이 존재하지 않을 시]_[InvalidIndexException]")
    public void ERROR_itemInvalidIndex_InvalidIndexException() {
      setUpDefault();

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(itemDao.getById(itemId))
          .thenThrow(NotFoundException.class);

      TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[권한이 없을 시]_[PermissionDeniedException]")
    @Override
    public void ERROR_accessDenied_PermissionDeniedException() {
      setUpDefault();
      setRequester(randomUserHaveLessPermissionOnDept(
          dept, Permission.USER));

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(itemDao.getById(itemId)).thenReturn(item);

      TestHelper.exceptionTest(this::execMethod, PermissionDeniedException.class);
    }
  }

  @Nested
  @DisplayName("createReservationOnStuff()")
  public final class TestCreateReservationOnStuff extends HistoryNestedTest {

    private StuffDto stuff;
    private ItemDto item;

    private HistoryDto createdHistory;

    @Override
    protected void setUpDefault() {
      setDept(TEST_DEPT);
      setRequester(randomUserHaveMorePermissionOnDept(
          dept, Permission.USER));
      setStuff(randomUsableStuffOnDept(dept));
    }

    private void setStuff(StuffDto stuff) {
      this.stuff = stuff;
      this.item = stuff.firstUsableItem();

      if (this.item != null) {
        this.createdHistory = new HistoryDto(
            UUID.randomUUID(), item, item.nextHistoryNum(), requester,
            null, null, null, null,
            currentTime(), 0, 0, 0, 0
        );
      }
    }

    @Override
    protected Object execMethod() {
      return historyService.createReservationOnStuff(userToken, stuff.id());
    }

    @RepeatedTest(10)
    @DisplayName("[SUCCESS]_[-]_[-]")
    public void SUCCESS() {
      setUpDefault();

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(stuffDao.getById(stuff.id())).thenReturn(stuff);
      when(historyDao.getListByDepartmentAndRequester(deptId, requesterId)).thenReturn(
          new ArrayList<>());

      when(historyDao.create(
          any(), eq(item.id()), eq(item.nextHistoryNum()), eq(requesterId),
          eq(null), eq(null), eq(null), eq(null),
          anyLong(), eq(0L), eq(0L), eq(0L), eq(0L))
      ).thenReturn(createdHistory);
      when(
          itemDao.update(item.id(), item.stuff().id(), item.num(), createdHistory.id())).thenReturn(
          item);

      execMethod();

      verify(historyDao).create(
          any(), eq(item.id()), eq(item.nextHistoryNum()), eq(requesterId),
          eq(null), eq(null), eq(null), eq(null),
          anyLong(), eq(0L), eq(0L), eq(0L), eq(0L)
      );
      verify(itemDao).update(item.id(), item.stuff().id(), item.num(), createdHistory.id());
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[`stuff`가 존재하지 않을 시]_[InvalidIndexException]")
    public void ERROR_stuffInvalidIndex_InvalidIndexException() {
      setUpDefault();

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(stuffDao.getById(stuff.id())).thenThrow(NotFoundException.class);

      TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[사용가능한`item`이 없는 상태일 시]_[NoUsableItemExistException]")
    public void ERROR_noUsableItem_NoUsableItemExistException() {
      setUpDefault();

      setStuff(randomUnusableStuffByDept(dept));

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(stuffDao.getById(stuff.id())).thenReturn(stuff);

      TestHelper.exceptionTest(this::execMethod, UsableItemNotExistedException.class);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[`requester`가 이미 해당 물품을 " + Constants.MAX_RENTAL_COUNT_ON_SAME_STUFF
        + "개 사용 중일 시]_[ExceedMaxRentalCountOnSameStuffException]")
    public void ERROR_requesterAlreadyRequestStuff_ExceedMaxRentalCountOnSameStuffException() {
      setUpDefault();

      setStuff(randomUsableStuffHaveItemMoreOnDept(dept, Constants.MAX_RENTAL_COUNT_ON_SAME_STUFF));

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(stuffDao.getById(stuff.id())).thenReturn(stuff);
      when(historyDao.getListByDepartmentAndRequester(deptId, requesterId))
          .thenReturn(makeHistoryListWithSameStuff(item, requester));

      TestHelper.exceptionTest(this::execMethod,
          RentalCountOnSameStuffLimitExceededException.class);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[`requester`가 이미 " + Constants.MAX_RENTAL_COUNT
        + "개 이상에 대한 대여가 진행 중일 시]_[ExceedMaxRentalCountException]")
    public void ERROR_requesterHasTooManyRequest_ExceedMaxRentalCountException() {
      setUpDefault();

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(historyDao.getListByDepartmentAndRequester(deptId, requesterId))
          .thenReturn(makeHistoryListWithSameRequester(item, requester));
      when(stuffDao.getById(stuff.id())).thenReturn(stuff);

      TestHelper.exceptionTest(this::execMethod, RentalCountLimitExceededException.class);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[권한이 없을 시]_[PermissionDeniedException]")
    @Override
    public void ERROR_accessDenied_PermissionDeniedException() {
      setUpDefault();
      setRequester(randomUserHaveLessPermissionOnDept(
          dept, Permission.USER));

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(stuffDao.getById(stuff.id())).thenReturn(stuff);

      TestHelper.exceptionTest(this::execMethod, PermissionDeniedException.class);
    }
  }

  @Nested
  @DisplayName("makeItemLost()")
  public final class TestMakeItemLost extends HistoryNestedTest {

    private ItemDto item;
    private UUID itemId;

    @Override
    protected void setUpDefault() {
      setDept(TEST_DEPT);
      setRequester(randomUserHaveMorePermissionOnDept(dept, Permission.STAFF));
      setItem(randomUsableItemOnDept(dept));
    }

    private void setItem(ItemDto item) {
      this.item = item;
      this.itemId = item.id();
    }

    @Override
    protected void setRequesterAccessDenied() {
      requester = randomUserHaveLessPermissionOnDept(dept, Permission.STAFF);
    }

    @Override
    protected Object execMethod() {
      return historyService.makeItemLost(userToken, itemId);
    }

    @RepeatedTest(10)
    @DisplayName("[SUCCESS]_[해당 `item`이 보관 중 분실되었을 시]_[-]")
    public void SUCCESS_itemIsUsable() {
      setUpDefault();
      setItem(randomUsableItemOnDept(dept));
      HistoryDto createdHistory = new HistoryDto(
          UUID.randomUUID(), item, item.nextHistoryNum(), null,
          null, null, requester, null,
          0L, 0L, 0L, currentTime(), 0L
      );

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(itemDao.getById(itemId)).thenReturn(item);
      when(historyDao.create(
          any(), eq(item.id()), eq(item.nextHistoryNum()), eq(null),
          eq(null), eq(null), eq(requesterId), eq(null),
          eq(0L), eq(0L), eq(0L), anyLong(), eq(0L))
      ).thenReturn(createdHistory);
      when(
          itemDao.update(item.id(), item.stuff().id(), item.num(), createdHistory.id())).thenReturn(
          item);

      execMethod();

      verify(historyDao).create(
          any(), eq(item.id()), eq(item.nextHistoryNum()), eq(null),
          eq(null), eq(null), eq(requesterId), eq(null),
          eq(0L), eq(0L), eq(0L), anyLong(), eq(0L)
      );
      verify(itemDao).update(item.id(), item.stuff().id(), item.num(), createdHistory.id());
    }

    @RepeatedTest(10)
    @DisplayName("[SUCCESS]_[`item`이 대여 요청이 들어 온 상태일 시]_[]")
    public void SUCCESS_itemIsReserved() {
      setUpDefault();
      setItem(randomReservedItemByDept(dept));

      HistoryDto canceledHistory = item.lastHistory()
          .withCancelManager(requester)
          .withCanceledAt(currentTime());

      HistoryDto createdHistory = new HistoryDto(
          UUID.randomUUID(), item, item.nextHistoryNum(), null,
          null, null, requester, null,
          0L, 0L, 0L, currentTime(), 0L
      );

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(itemDao.getById(itemId)).thenReturn(item);
      when(historyDao.update(
          eq(item.lastHistory().id()), eq(item.id()),
          eq(item.lastHistory().num()), eq(item.lastHistory().requester().id()),
          eq(null), eq(null), eq(null), eq(requesterId),
          eq(item.lastHistory().requestedAt()), eq(0L),
          eq(0L), eq(0L), anyLong())
      ).thenReturn(canceledHistory);
      when(historyDao.create(
          any(), eq(item.id()), eq(item.nextHistoryNum()), eq(null),
          eq(null), eq(null), eq(requesterId), eq(null),
          eq(0L), eq(0L), eq(0L), anyLong(), eq(0L))
      ).thenReturn(createdHistory);
      when(
          itemDao.update(item.id(), item.stuff().id(), item.num(), createdHistory.id())).thenReturn(
          item);

      execMethod();

      verify(historyDao).update(
          eq(item.lastHistory().id()), eq(item.id()),
          eq(item.lastHistory().num()), eq(item.lastHistory().requester().id()),
          eq(null), eq(null),
          eq(null), eq(requesterId), eq(item.lastHistory().requestedAt()),
          eq(0L), eq(0L), eq(0L), anyLong()
      );

      verify(historyDao).create(
          any(), eq(item.id()), eq(item.nextHistoryNum()), eq(null),
          eq(null), eq(null), eq(requesterId), eq(null),
          eq(0L), eq(0L), eq(0L), anyLong(), eq(0L)
      );
      verify(itemDao).update(item.id(), item.stuff().id(), item.num(), createdHistory.id());
    }

    @RepeatedTest(10)
    @DisplayName("[SUCCESS]_[해당 `item`이 대여 중 분실 되었을 시]_[-]")
    public void SUCCESS_itemIsUnusable() {
      setUpDefault();
      setItem(randomUsingOrDelayedItemByDept(dept));
      HistoryDto newHistory = item.lastHistory()
          .withLostManager(requester)
          .withLostAt(currentTime());

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(itemDao.getById(itemId)).thenReturn(item);
      when(historyDao.update(
          eq(item.lastHistory().id()), eq(item.id()),
          eq(item.lastHistory().num()), eq(item.lastHistory().requester().id()),
          eq(item.lastHistory().approveManager().id()), eq(null),
          eq(requesterId), eq(null), eq(item.lastHistory().requestedAt()),
          eq(item.lastHistory().approvedAt()), eq(0L), anyLong(), eq(0L))
      ).thenReturn(newHistory);

      execMethod();

      verify(historyDao).update(
          eq(item.lastHistory().id()), eq(item.id()),
          eq(item.lastHistory().num()), eq(item.lastHistory().requester().id()),
          eq(item.lastHistory().approveManager().id()), eq(null),
          eq(requesterId), eq(null), eq(item.lastHistory().requestedAt()),
          eq(item.lastHistory().approvedAt()), eq(0L), anyLong(), eq(0L)
      );
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[`item`이 이미 분실된 상태일 시]_[LostRegistrationOnLostItemException]")
    public void ERROR_itemIsUnusable_LostRegistrationOnLostItemException() {
      setUpDefault();
      setItem(randomInactiveItemByDept(dept));

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(itemDao.getById(itemId)).thenReturn(item);

      TestHelper.exceptionTest(this::execMethod,
          LostRegistrationRequestedOnLostItemException.class);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[`item`이 존재하지 않을 시]_[InvalidIndexException]")
    public void ERROR_itemInvalidIndex_InvalidIndexException() {
      setUpDefault();

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(itemDao.getById(itemId)).thenThrow(NotFoundException.class);

      TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[권한이 없을 시]_[PermissionDeniedException]")
    @Override
    public void ERROR_accessDenied_PermissionDeniedException() {
      setUpDefault();
      setRequesterAccessDenied();

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(itemDao.getById(item.id())).thenReturn(item);

      TestHelper.exceptionTest(this::execMethod, PermissionDeniedException.class);
    }
  }


  @Nested
  @DisplayName("makeItemUsing()")
  public final class TestMakeItemUsing extends HistoryNestedTest {

    private ItemDto item;
    private UUID itemId;

    private HistoryDto newHistory;

    @Override
    protected void setUpDefault() {
      setDept(TEST_DEPT);
      setRequester(randomUserHaveMorePermissionOnDept(dept, Permission.STAFF));
      setItem(randomReservedItemByDept(dept));
    }

    private void setItem(ItemDto item) {
      this.item = item;
      this.itemId = item.id();

      if (item.lastHistory() != null) {
        this.newHistory = item.lastHistory()
            .withApproveManager(requester)
            .withApprovedAt(currentTime());
      }
    }

    @Override
    protected void setRequesterAccessDenied() {
      requester = randomUserHaveLessPermissionOnDept(dept, Permission.STAFF);
    }

    @Override
    protected HistoryDto execMethod() {
      return historyService.makeItemUsing(userToken, itemId);
    }

    @RepeatedTest(10)
    @DisplayName("[SUCCESS]_[-]_[-]")
    public void SUCCESS() {
      setUpDefault();

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(itemDao.getById(itemId)).thenReturn(item);
      when(historyDao.update(
          eq(item.lastHistory().id()), eq(item.id()),
          eq(item.lastHistory().num()), eq(item.lastHistory().requester().id()),
          eq(requesterId), eq(null), eq(null), eq(null),
          eq(item.lastHistory().requestedAt()), anyLong(),
          eq(0L), eq(0L), eq(0L))
      ).thenReturn(newHistory);

      execMethod();

      verify(historyDao).update(
          eq(item.lastHistory().id()), eq(item.id()),
          eq(item.lastHistory().num()), eq(item.lastHistory().requester().id()),
          eq(requesterId), eq(null), eq(null), eq(null),
          eq(item.lastHistory().requestedAt()), anyLong(),
          eq(0L), eq(0L), eq(0L)
      );
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[`item`이 예약된 상태가 아닐 시]_[ResponseOnUnrequestedItemException]")
    public void ERROR_itemIsUnusable_ResponseOnUnrequestedItemException() {
      setUpDefault();
      setItem(randomUsableItemOnDept(dept));

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(itemDao.getById(itemId)).thenReturn(item);

      TestHelper.exceptionTest(this::execMethod, RespondedOnUnrequestedItemException.class);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[`item`이 존재하지 않을 시]_[InvalidIndexException]")
    public void ERROR_itemInvalidIndex_InvalidIndexException() {
      setUpDefault();

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(itemDao.getById(itemId)).thenThrow(NotFoundException.class);

      TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[권한이 없을 시]_[PermissionDeniedException]")
    @Override
    public void ERROR_accessDenied_PermissionDeniedException() {
      setUpDefault();
      setRequesterAccessDenied();

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(itemDao.getById(item.id())).thenReturn(item);

      TestHelper.exceptionTest(this::execMethod, PermissionDeniedException.class);
    }
  }

  @Nested
  @DisplayName("makeItemReturn()")
  public final class TestMakeItemReturn extends HistoryNestedTest {

    private ItemDto item;
    private UUID itemId;

    private HistoryDto newHistory;

    @Override
    protected void setUpDefault() {
      setDept(TEST_DEPT);
      setRequester(randomUserHaveMorePermissionOnDept(dept, Permission.STAFF));
      setItem(randomReturnAbleItemByDept(dept));
    }

    private void setItem(ItemDto item) {
      this.item = item;
      this.itemId = item.id();

      if (item.lastHistory() != null) {
        this.newHistory = item.lastHistory()
            .withLostManager(requester)
            .withLostAt(currentTime());
      }
    }

    @Override
    protected void setRequesterAccessDenied() {
      requester = randomUserHaveLessPermissionOnDept(dept, Permission.STAFF);
    }

    @Override
    protected HistoryDto execMethod() {
      return historyService.makeItemReturn(userToken, itemId);
    }

    @RepeatedTest(10)
    @DisplayName("[SUCCESS]_[-]_[-]")
    public void SUCCESS() {
      setUpDefault();

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(itemDao.getById(itemId)).thenReturn(item);
      when(historyDao.update(
          eq(item.lastHistory().id()), eq(item.id()),
          eq(item.lastHistory().num()),
          eq(getUserIdOrNull(item.lastHistory().requester())),
          eq(getUserIdOrNull(item.lastHistory().approveManager())),
          eq(requesterId),
          eq(getUserIdOrNull(item.lastHistory().lostManager())),
          eq(null),
          eq(item.lastHistory().requestedAt()),
          eq(item.lastHistory().approvedAt()),
          anyLong(),
          eq(item.lastHistory().lostAt()),
          eq(0L))
      ).thenReturn(newHistory);

      execMethod();

      verify(historyDao).update(
          eq(item.lastHistory().id()), eq(item.id()),
          eq(item.lastHistory().num()),
          eq(getUserIdOrNull(item.lastHistory().requester())),
          eq(getUserIdOrNull(item.lastHistory().approveManager())),
          eq(requesterId),
          eq(getUserIdOrNull(item.lastHistory().lostManager())),
          eq(null),
          eq(item.lastHistory().requestedAt()),
          eq(item.lastHistory().approvedAt()),
          anyLong(),
          eq(item.lastHistory().lostAt()),
          eq(0L)
      );
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[`item`이 이미 반납되어있는 상태일 시 1]_[ReturnRegistrationOnReturnedItemException]")
    public void ERROR_itemIsUsable_ReturnRegistrationOnReturnedItemException() {
      setUpDefault();
      setItem(randomUsableItemOnDept(dept));

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(itemDao.getById(itemId)).thenReturn(item);

      TestHelper.exceptionTest(this::execMethod,
          ReturnRegistrationRequestedOnReturnedItemException.class);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[`item`이 이미 반납되어있는 상태일 시 2]_[ReturnRegistrationOnReturnedItemException]")
    public void ERROR_itemIsReserved_ReturnRegistrationOnReturnedItemException() {
      setUpDefault();
      setItem(randomReservedItemByDept(dept));

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(itemDao.getById(itemId)).thenReturn(item);

      TestHelper.exceptionTest(this::execMethod,
          ReturnRegistrationRequestedOnReturnedItemException.class);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[`item`이 존재하지 않을 시]_[InvalidIndexException]")
    public void ERROR_itemInvalidIndex_InvalidIndexException() {
      setUpDefault();

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(itemDao.getById(itemId)).thenThrow(NotFoundException.class);

      TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[권한이 없을 시]_[PermissionDeniedException]")
    @Override
    public void ERROR_accessDenied_PermissionDeniedException() {
      setUpDefault();
      setRequesterAccessDenied();

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(itemDao.getById(item.id())).thenReturn(item);

      TestHelper.exceptionTest(this::execMethod, PermissionDeniedException.class);
    }
  }

  @Nested
  @DisplayName("makeItemCancel()")
  public final class TestMakeItemCancel extends HistoryNestedTest {

    private ItemDto item;
    private UUID itemId;

    private HistoryDto newHistory;

    @Override
    protected void setUpDefault() {
      setDept(TEST_DEPT);
      setRequester(randomUserHaveMorePermissionOnDept(dept, Permission.STAFF));
      setItem(randomReservedItemByDept(dept));
    }

    private void setItem(ItemDto item) {
      this.item = item;
      this.itemId = item.id();

      if (item.lastHistory() != null) {
        this.newHistory = item.lastHistory()
            .withCancelManager(requester)
            .withCanceledAt(currentTime());
      }
    }

    @Override
    protected void setRequesterAccessDenied() {
      requester = randomUserHaveLessPermissionOnDept(dept, Permission.STAFF);
    }

    @Override
    protected HistoryDto execMethod() {
      return historyService.makeItemCancel(userToken, itemId);
    }

    @RepeatedTest(10)
    @DisplayName("[SUCCESS]_[-]_[-]")
    public void SUCCESS() {
      setUpDefault();

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(itemDao.getById(itemId)).thenReturn(item);
      when(historyDao.update(
          eq(item.lastHistory().id()), eq(item.id()),
          eq(item.lastHistory().num()),
          eq(item.lastHistory().requester().id()), eq(null),
          eq(null), eq(null), eq(requesterId),
          eq(item.lastHistory().requestedAt()),
          eq(0L), eq(0L), eq(0L), anyLong())
      ).thenReturn(newHistory);

      execMethod();

      verify(historyDao).update(
          eq(item.lastHistory().id()), eq(item.id()),
          eq(item.lastHistory().num()),
          eq(item.lastHistory().requester().id()), eq(null),
          eq(null), eq(null), eq(requesterId),
          eq(item.lastHistory().requestedAt()),
          eq(0L), eq(0L), eq(0L), anyLong()
      );
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[`item`이 취소할 수 있는 상황이 아닐시]_[ResponseOnUnrequestedItemException]")
    public void ERROR_itemIsNonCancelable_ResponseOnUnrequestedItemException() {
      setUpDefault();
      setItem(randomUsableItemOnDept(dept));

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(itemDao.getById(itemId)).thenReturn(item);

      TestHelper.exceptionTest(this::execMethod, RespondedOnUnrequestedItemException.class);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[`item`이 존재하지 않을 시]_[InvalidIndexException]")
    public void ERROR_itemInvalidIndex_InvalidIndexException() {
      setUpDefault();

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(itemDao.getById(itemId)).thenThrow(NotFoundException.class);

      TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[권한이 없을 시]_[PermissionDeniedException]")
    @Override
    public void ERROR_accessDenied_PermissionDeniedException() {
      setUpDefault();
      setRequesterAccessDenied();

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(itemDao.getById(item.id())).thenReturn(item);

      TestHelper.exceptionTest(this::execMethod, PermissionDeniedException.class);
    }
  }

  private List<HistoryDto> makeHistoryListWithSameStuff(ItemDto item, UserDto requester) {
    List<HistoryDto> output = new ArrayList<>();
    List<ItemDto> exclude = new ArrayList<>(List.of(item));
    for (int i = 0; i < Constants.MAX_RENTAL_COUNT_ON_SAME_STUFF; i++) {
      ItemDto newItem = randomItemOnStuffWithExclude(item.stuff(), exclude);
      exclude.add(newItem);
      output.add(
          new HistoryDto(
              UUID.randomUUID(),
              newItem,
              newItem.nextHistoryNum(),
              requester,
              null,
              null,
              null,
              null,
              System.currentTimeMillis() / 1000,
              0,
              0,
              0,
              0
          )
      );
    }
    return output;
  }

  private List<HistoryDto> makeHistoryListWithSameRequester(ItemDto item, UserDto requester) {
    List<HistoryDto> output = new ArrayList<>();
    List<StuffDto> exclude = new ArrayList<>(List.of(item.stuff()));
    for (int i = 0; i < Constants.MAX_RENTAL_COUNT; i++) {
      StuffDto newStuff = randomStuffOnDeptWithExclude(item.stuff().department(), exclude);
      exclude.add(newStuff);

      ItemDto newItem = randomItemOnStuff(newStuff);
      output.add(
          new HistoryDto(
              UUID.randomUUID(),
              newItem,
              newItem.nextHistoryNum(),
              requester,
              null,
              null,
              null,
              null,
              System.currentTimeMillis() / 1000,
              0,
              0,
              0,
              0
          )
      );
    }
    return output;
  }

  private UUID getUserIdOrNull(UserDto user) {
      if (user == null) {
          return null;
      }
    return user.id();
  }

  private UserDto randomUserHaveExactPermissionOnDeptWithExclude(DepartmentDto dept,
      Permission permission, List<UserDto> exclude) {
    RandomGetter<UserDto> users = usersHaveExactPermissionOnDept(allUsers(), dept, permission);
    users = withExclude(users, exclude);
    return randomSelectAndLog(users);
  }

  private StuffDto randomUsableStuffHaveItemMoreOnDept(DepartmentDto dept, int n) {
    RandomGetter<StuffDto> stuffs = stuffsOnDept(allStuffs(), dept);
    stuffs = usableStuffs(stuffs);
    stuffs = stuffsHaveItemMore(stuffs, n);
    return randomSelectAndLog(stuffs);
  }

  private StuffDto randomUsableStuffOnDept(DepartmentDto dept) {
    RandomGetter<StuffDto> stuffs = stuffsOnDept(allStuffs(), dept);
    stuffs = usableStuffs(stuffs);
    return randomSelectAndLog(stuffs);
  }

  private StuffDto randomUnusableStuffByDept(DepartmentDto dept) {
    RandomGetter<StuffDto> stuffs = stuffsOnDept(allStuffs(), dept);
    stuffs = unusableStuffs(stuffs);
    return randomSelectAndLog(stuffs);
  }

  private StuffDto randomStuffOnDeptWithExclude(DepartmentDto dept, List<StuffDto> exclude) {
    RandomGetter<StuffDto> stuffs = stuffsOnDept(allStuffs(), dept);
    stuffs = withExclude(stuffs, exclude);
    return randomSelectAndLog(stuffs);
  }

  private ItemDto randomUsableItemOnDept(DepartmentDto dept) {
    RandomGetter<ItemDto> items = itemsOnDept(allItems(), dept);
    items = usableItems(items);
    return randomSelectAndLog(items);
  }

  private ItemDto randomItemOnStuff(StuffDto stuff) {
    return randomSelectAndLog(itemsOnStuff(allItems(), stuff));
  }

  private ItemDto randomUsableItemOnStuff(StuffDto stuff) {
    RandomGetter<ItemDto> items = usableItems(itemsOnStuff(allItems(), stuff));
    return randomSelectAndLog(items);
  }

  private ItemDto randomItemOnStuffWithExclude(StuffDto stuff, List<ItemDto> exclude) {
    RandomGetter<ItemDto> items = itemsOnStuff(allItems(), stuff);
    items = withExclude(items, exclude);
    return randomSelectAndLog(items);
  }

  private ItemDto randomUnusableItemByDept(DepartmentDto dept) {
    RandomGetter<ItemDto> items = itemsOnDept(allItems(), dept);
    items = unusableItems(items);
    return randomSelectAndLog(items);
  }

  private ItemDto randomInactiveItemByDept(DepartmentDto dept) {
    RandomGetter<ItemDto> items = itemsOnDept(allItems(), dept);
    items = lostItems(items);
    return randomSelectAndLog(items);
  }

  private ItemDto randomReservedItemByDept(DepartmentDto dept) {
    RandomGetter<ItemDto> items = itemsOnDept(allItems(), dept);
    items = reservedItems(items);
    return randomSelectAndLog(items);
  }

  private ItemDto randomUsingOrDelayedItemByDept(DepartmentDto dept) {
    RandomGetter<ItemDto> items = itemsOnDept(allItems(), dept);
    items = usingOrDelayedItems(items);
    return randomSelectAndLog(items);
  }

  private ItemDto randomReturnAbleItemByDept(DepartmentDto dept) {
    RandomGetter<ItemDto> items = itemsOnDept(allItems(), dept);
    items = returnableItems(items);
    return randomSelectAndLog(items);
  }

  private ItemDto randomFirstUsableItemOnDept(DepartmentDto dept) {
    RandomGetter<StuffDto> stuffs = stuffsOnDept(allStuffs(), dept);
    stuffs = stuffs.filter((stuff) -> stuff.count() > 0);
    StuffDto targetStuff = stuffs.randomSelect();

    RandomGetter<ItemDto> items = itemsOnStuff(allItems(), targetStuff);
    items = usableItems(items);
    items = firstUsableItems(items);

    return randomSelectAndLog(items);
  }

  private ItemDto randomNonFirstUsableItemByDept(DepartmentDto dept) {
    RandomGetter<StuffDto> stuffs = stuffsOnDept(allStuffs(), dept);
    stuffs = stuffs.filter((stuff) -> stuff.count() > 1);
    StuffDto targetStuff = stuffs.randomSelect();

    RandomGetter<ItemDto> items = itemsOnStuff(allItems(), targetStuff);
    items = usableItems(items);
    items = nonFirstUsableItems(items);

    return randomSelectAndLog(items);
  }

  private RandomGetter<StuffDto> stuffsHaveItemMore(RandomGetter<StuffDto> stuffs, int n) {
    return stuffs.filter((stuff) -> stuff.items().size() > n);
  }

  private RandomGetter<StuffDto> usableStuffs(RandomGetter<StuffDto> stuffs) {
    return stuffs.filter((stuff) -> stuff.firstUsableItem() != null);
  }

  private RandomGetter<StuffDto> unusableStuffs(RandomGetter<StuffDto> stuffs) {
    return stuffs.filter((stuff) -> stuff.firstUsableItem() == null);
  }

  private RandomGetter<ItemDto> usableItems(RandomGetter<ItemDto> items) {
    return items.filter(ItemDto::isUsable);
  }

  private RandomGetter<ItemDto> unusableItems(RandomGetter<ItemDto> items) {
    return items.filter(ItemDto::isUnusable);
  }

  private RandomGetter<ItemDto> lostItems(RandomGetter<ItemDto> items) {
    return items.filter((item) -> item.status() == ItemStatus.LOST);
  }

  private RandomGetter<ItemDto> reservedItems(RandomGetter<ItemDto> items) {
    return items.filter((item) -> item.status() == ItemStatus.REQUESTED);
  }

  private RandomGetter<ItemDto> usingOrDelayedItems(RandomGetter<ItemDto> items) {
    return items.filter((item) -> item.status() == ItemStatus.USING);
  }

  private RandomGetter<ItemDto> returnableItems(RandomGetter<ItemDto> items) {
    return items.filter((item) -> item.status() == ItemStatus.USING
        || item.status() == ItemStatus.LOST);
  }

  private RandomGetter<ItemDto> itemsOnStuff(RandomGetter<ItemDto> items, StuffDto stuff) {
    return items.filter((item) -> item.stuff().matchId(stuff));
  }

  private RandomGetter<ItemDto> firstUsableItems(RandomGetter<ItemDto> items) {
    return items.filter((item) -> item.matchId(item.stuff().firstUsableItem()));
  }

  private RandomGetter<ItemDto> nonFirstUsableItems(RandomGetter<ItemDto> items) {
    return items.filter((item) -> !item.matchId(item.stuff().firstUsableItem()));
  }

  private abstract class HistoryNestedTest extends BaseNestedTestWithDept {

  }
}
