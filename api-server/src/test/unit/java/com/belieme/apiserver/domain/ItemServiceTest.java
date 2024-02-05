package com.belieme.apiserver.domain;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.belieme.apiserver.domain.dto.DepartmentDto;
import com.belieme.apiserver.domain.dto.ItemDto;
import com.belieme.apiserver.domain.dto.StuffDto;
import com.belieme.apiserver.domain.dto.enumeration.Permission;
import com.belieme.apiserver.domain.exception.ItemAmountLimitExceededException;
import com.belieme.apiserver.domain.exception.PermissionDeniedException;
import com.belieme.apiserver.domain.service.ItemService;
import com.belieme.apiserver.domain.util.Constants;
import com.belieme.apiserver.error.exception.ConflictException;
import com.belieme.apiserver.error.exception.InvalidIndexException;
import com.belieme.apiserver.error.exception.NotFoundException;
import com.belieme.apiserver.util.TestHelper;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest extends BaseServiceTest {

  private final DepartmentDto TEST_DEPT = stub.HYU_CSE_DEPT;

  @InjectMocks
  private ItemService itemService;

  @Nested
  @DisplayName("getListByStuff()")
  public final class TestGetListByStuff extends ItemNestedTest {

    private StuffDto stuff;
    private UUID stuffId;

    private List<ItemDto> itemList;

    @Override
    protected void setUpDefault() {
      setDept(TEST_DEPT);
      setRequester(randomUserHaveMorePermissionOnDept(dept, Permission.USER));
      setStuff(randomStuffOnDept(dept));

      itemList = getItemListByStuff(stuff);
    }

    private void setStuff(StuffDto stuff) {
      this.stuff = stuff;
      this.stuffId = stuff.id();
    }

    @Override
    protected List<ItemDto> execMethod() {
      return itemService.getListByStuff(userToken, stuffId);
    }

    @RepeatedTest(10)
    @DisplayName("[SUCCESS]_[-]_[-]")
    public void SUCCESS() {
      setUpDefault();

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(stuffDao.getById(stuffId)).thenReturn(stuff);
      when(itemDao.getListByStuff(stuffId)).thenReturn(itemList);

      TestHelper.listCompareTest(this::execMethod, itemList);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[해당 `index`의 `Stuff`가 존재하지 않을 시]_[InvalidIndexException]")
    public void ERROR_stuffInvalidIndex_InvalidIndexException() {
      setUpDefault();

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(stuffDao.getById(stuffId)).thenReturn(stuff);
      when(itemDao.getListByStuff(stuffId)).thenThrow(
          InvalidIndexException.class);

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

      TestHelper.exceptionTest(this::execMethod,
          PermissionDeniedException.class);
    }

    private List<ItemDto> getItemListByStuff(StuffDto stuff) {
      return stub.ALL_ITEMS.stream()
          .filter((item) -> stuff.matchId(item.stuff())).toList();
    }
  }

  @Nested
  @DisplayName("getById()")
  public final class TestGetByIndex extends ItemNestedTest {

    private ItemDto item;
    private UUID itemId;

    @Override
    protected void setUpDefault() {
      setDept(TEST_DEPT);
      setRequester(randomUserHaveMorePermissionOnDept(dept, Permission.USER));
      setItem(randomItemOnDept(dept));
    }

    private void setItem(ItemDto item) {
      this.item = item;
      this.itemId = item.id();
    }

    @Override
    protected ItemDto execMethod() {
      return itemService.getById(userToken, itemId);
    }

    @RepeatedTest(10)
    @DisplayName("[SUCCESS]_[-]_[-]")
    public void SUCCESS() {
      setUpDefault();

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(itemDao.getById(itemId)).thenReturn(item);

      TestHelper.objectCompareTest(this::execMethod, item);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[해당 `index`에 `item`이 존재하지 않을 시]_[NotFoundException]")
    public void ERROR_itemNotFound_NotFoundException() {
      setUpDefault();

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(itemDao.getById(itemId)).thenThrow(NotFoundException.class);

      TestHelper.exceptionTest(this::execMethod, NotFoundException.class);
    }

    @Override
    @RepeatedTest(10)
    @DisplayName("[ERROR]_[권한이 없을 시]_[PermissionDeniedException]")
    public void ERROR_accessDenied_PermissionDeniedException() {
      setUpDefault();
      setRequesterAccessDenied();

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(itemDao.getById(itemId)).thenReturn(item);

      TestHelper.exceptionTest(this::execMethod,
          PermissionDeniedException.class);
    }
  }

  @Nested
  @DisplayName("create()")
  public final class TestCreate extends ItemNestedTest {

    private List<ItemDto> newItems;
    private StuffDto targetStuff;
    private int amount;

    @Override
    protected void setUpDefault() {
      setDept(TEST_DEPT);
      setRequester(randomUserHaveMorePermissionOnDept(dept, Permission.STAFF));
      setAmount((int) (Math.random() * 5) + 1);

      StuffDto stuff = randomStuffOnDept(dept);
      setTargetStuff(stuff);
      setNewItems(stuff.nextItemNums(amount).stream()
          .map(e -> new ItemDto(UUID.randomUUID(), stuff, e, null))
          .toList());
    }

    private void setTargetStuff(StuffDto targetStuff) {
      this.targetStuff = targetStuff;
    }

    private void setNewItems(List<ItemDto> items) {
      this.newItems = List.copyOf(items);
    }

    private void setAmount(int amount) {
      this.amount = amount;
    }

    @Override
    protected void setRequesterAccessDenied() {
      setRequester(randomUserHaveLessPermissionOnDept(dept, Permission.USER));
    }

    @Override
    protected Object execMethod() {
      return itemService.create(userToken, targetStuff.id(), amount);
    }

    @RepeatedTest(10)
    @DisplayName("[SUCCESS]_[-]_[-]")
    public void SUCCESS() {
      setUpDefault();

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(stuffDao.getById(targetStuff.id())).thenReturn(targetStuff);

      for (int i = 0; i < newItems.size(); i++) {
        when(
            itemDao.create(any(), eq(targetStuff.id()),
            eq(targetStuff.nextItemNums(amount).get(i)))
        ).thenReturn(newItems.get(i));
      }

      List<ItemDto> addedItems = targetStuff.items();
      addedItems.addAll(newItems);

      TestHelper.objectCompareTest(this::execMethod, targetStuff.withItems(addedItems));

      verify(itemDao, times(amount)).create(any(), eq(targetStuff.id()), anyInt());
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[해당 `index`의 `Stuff`가 존재하지 않을 시]_[InvalidIndexException]")
    public void ERROR_stuffInvalidIndex_InvalidIndexException() {
      setUpDefault();

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(stuffDao.getById(targetStuff.id())).thenThrow(NotFoundException.class);

      TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[해당 `stuff`의 `item`개수가 " + Constants.MAX_ITEM_NUM
        + "개를 초과할 시]_[ExceedMaxItemNumException]")
    public void ERROR_stuffIsFull_ExceedMaxItemNumException() {
      setUpDefault();

      int oldItemCount = targetStuff.itemsSize();
      setAmount((Constants.MAX_ITEM_NUM - oldItemCount) + (int) (Math.random() * 10) + 1);
      setNewItems(targetStuff.nextItemNums(amount).stream()
          .map(e -> new ItemDto(UUID.randomUUID(), targetStuff, e, null))
          .toList());

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(stuffDao.getById(targetStuff.id())).thenReturn(targetStuff);

      TestHelper.exceptionTest(this::execMethod,
          ItemAmountLimitExceededException.class);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[해당 `index`의 `item`이 이미 존재할 시]_[ConflictException]")
    public void ERROR_stuffConflict_ConflictException() {
      setUpDefault();

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(stuffDao.getById(targetStuff.id())).thenReturn(targetStuff);
      when(itemDao.create(any(), eq(targetStuff.id()), anyInt())).thenThrow(ConflictException.class);

      TestHelper.exceptionTest(this::execMethod, ConflictException.class);
    }

    @Override
    @RepeatedTest(10)
    @DisplayName("[ERROR]_[권한이 없을 시]_[PermissionDeniedException]")
    public void ERROR_accessDenied_PermissionDeniedException() {
      setUpDefault();
      setRequesterAccessDenied();

      when(userDao.getByToken(userToken)).thenReturn(requester);
      when(stuffDao.getById(targetStuff.id())).thenReturn(targetStuff);

      TestHelper.exceptionTest(this::execMethod,
          PermissionDeniedException.class);
    }
  }

  private abstract class ItemNestedTest extends BaseNestedTestWithDept {

  }
}
