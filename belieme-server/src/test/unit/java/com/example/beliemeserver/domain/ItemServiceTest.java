package com.example.beliemeserver.domain;

import com.example.beliemeserver.domain.dto._new.DepartmentDto;
import com.example.beliemeserver.domain.dto._new.ItemDto;
import com.example.beliemeserver.domain.dto._new.StuffDto;
import com.example.beliemeserver.domain.dto.enumeration.Permission;
import com.example.beliemeserver.domain.exception.ItemAmountLimitExceededException;
import com.example.beliemeserver.domain.exception.PermissionDeniedException;
import com.example.beliemeserver.domain.service.ItemService;
import com.example.beliemeserver.domain.util.Constants;
import com.example.beliemeserver.error.exception.ConflictException;
import com.example.beliemeserver.error.exception.InvalidIndexException;
import com.example.beliemeserver.error.exception.NotFoundException;
import com.example.beliemeserver.util.TestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

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
            when(itemDao.getListByStuff(stuffId)).thenThrow(InvalidIndexException.class);

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

        private List<ItemDto> getItemListByStuff(StuffDto stuff) {
            return stub.ALL_ITEMS.stream()
                    .filter((item) -> stuff.matchId(item.stuff()))
                    .toList();
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

            TestHelper.objectCompareTest(
                    this::execMethod,
                    item
            );
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[해당 `index`에 `item`이 존재하지 않을 시]_[NotFoundException]")
        public void ERROR_itemNotFound_NotFoundException() {
            setUpDefault();

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(itemDao.getById(itemId)).thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(
                    this::execMethod,
                    NotFoundException.class
            );
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
    }

    @Nested
    @DisplayName("create()")
    public final class TestCreate extends ItemNestedTest {
        private ItemDto item;
        private UUID stuffId;

        @Override
        protected void setUpDefault() {
            setDept(TEST_DEPT);
            setRequester(randomUserHaveMorePermissionOnDept(dept, Permission.STAFF));

            StuffDto targetStuff = randomStuffOnDept(dept);
            setItem(new ItemDto(UUID.randomUUID(), targetStuff, targetStuff.nextItemNum(), null));
        }

        private void setItem(ItemDto item) {
            this.item = item;
            this.stuffId = item.stuff().id();
        }

        @Override
        protected void setRequesterAccessDenied() {
            setRequester(randomUserHaveLessPermissionOnDept(dept, Permission.USER));
        }

        @Override
        protected Object execMethod() {
            return itemService.create(userToken, stuffId);
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setUpDefault();

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(stuffDao.getById(stuffId)).thenReturn(item.stuff());
            when(itemDao.create(any(), eq(stuffId), eq(item.stuff().nextItemNum()))).thenReturn(item);

            TestHelper.objectCompareTest(this::execMethod, item);

            verify(itemDao, times(1)).create(any(), eq(stuffId), eq(item.stuff().nextItemNum()));
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[해당 `index`의 `Stuff`가 존재하지 않을 시]_[InvalidIndexException]")
        public void ERROR_stuffInvalidIndex_InvalidIndexException() {
            setUpDefault();

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(stuffDao.getById(stuffId)).thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[해당 `stuff`의 `item`개수가 이미 " + Constants.MAX_ITEM_NUM + "개 일 시]_[ExceedMaxItemNumException]")
        public void ERROR_stuffIsFull_ExceedMaxItemNumException() {
            setUpDefault();

            StuffDto newStuff = getFullStuff(item.stuff());
            setItem(ItemDto.init(newStuff, newStuff.nextItemNum()));

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(stuffDao.getById(stuffId)).thenReturn(newStuff);

            TestHelper.exceptionTest(this::execMethod, ItemAmountLimitExceededException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[해당 `index`의 `item`이 이미 존재할 시]_[ConflictException]")
        public void ERROR_stuffConflict_ConflictException() {
            setUpDefault();

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(stuffDao.getById(stuffId)).thenReturn(item.stuff());
            when(itemDao.create(any(), eq(stuffId), eq(item.stuff().nextItemNum())))
                    .thenThrow(ConflictException.class);

            TestHelper.exceptionTest(this::execMethod, ConflictException.class);
        }

        @Override
        @RepeatedTest(10)
        @DisplayName("[ERROR]_[권한이 없을 시]_[PermissionDeniedException]")
        public void ERROR_accessDenied_PermissionDeniedException() {
            setUpDefault();
            setRequesterAccessDenied();

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(stuffDao.getById(stuffId)).thenReturn(item.stuff());

            TestHelper.exceptionTest(this::execMethod, PermissionDeniedException.class);
        }

        private StuffDto getFullStuff(StuffDto stuff) {
            List<ItemDto> newItems = stuff.items();
            while (newItems.size() <= Constants.MAX_ITEM_NUM) {
                ItemDto newItem = ItemDto.init(stuff, newItems.size());
                newItems.add(newItem);
            }
            return stuff.withItems(newItems);
        }
    }

    private abstract class ItemNestedTest extends BaseNestedTestWithDept {
    }
}
