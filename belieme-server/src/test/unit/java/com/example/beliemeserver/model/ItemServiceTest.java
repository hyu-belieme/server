package com.example.beliemeserver.model;

import com.example.beliemeserver.error.exception.ConflictException;
import com.example.beliemeserver.error.exception.NotFoundException;
import com.example.beliemeserver.model.dto.AuthorityDto;
import com.example.beliemeserver.model.dto.DepartmentDto;
import com.example.beliemeserver.model.dto.ItemDto;
import com.example.beliemeserver.model.dto.StuffDto;
import com.example.beliemeserver.model.exception.ItemAmountLimitExceededException;
import com.example.beliemeserver.model.exception.IndexInvalidException;
import com.example.beliemeserver.model.service.ItemService;
import com.example.beliemeserver.model.util.Constants;
import com.example.beliemeserver.util.TestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

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
        private String stuffName;

        private List<ItemDto> itemList;

        @Override
        protected void setUpDefault() {
            setDept(TEST_DEPT);
            setRequester(randomUserHaveMorePermissionOnDept(dept, AuthorityDto.Permission.USER));
            setStuff(randomStuffOnDept(dept));

            itemList = getItemListByStuff(stuff);
        }

        private void setStuff(StuffDto stuff) {
            this.stuff = stuff;
            this.stuffName = stuff.name();
        }

        @Override
        protected List<ItemDto> execMethod() {
            return itemService.getListByStuff(userToken, univCode, deptCode, stuffName);
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(itemDao.getListByStuff(univCode, deptCode, stuffName))
                    .thenReturn(itemList);

            TestHelper.listCompareTest(this::execMethod, itemList);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[해당 `index`의 `Stuff`가 존재하지 않을 시]_[InvalidIndexException]")
        public void ERROR_stuffInvalidIndex_InvalidIndexException() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(itemDao.getListByStuff(univCode, deptCode, stuffName))
                    .thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(this::execMethod, IndexInvalidException.class);
        }

        private List<ItemDto> getItemListByStuff(StuffDto stuff) {
            return stub.ALL_ITEMS.stream()
                    .filter((item) -> stuff.matchUniqueKey(item.stuff()))
                    .toList();
        }
    }

    @Nested
    @DisplayName("getByIndex()")
    public final class TestGetByIndex extends ItemNestedTest {
        private ItemDto item;
        private String stuffName;
        private int itemNum;

        @Override
        protected void setUpDefault() {
            setDept(TEST_DEPT);
            setRequester(randomUserHaveMorePermissionOnDept(dept, AuthorityDto.Permission.USER));
            setItem(randomItemOnDept(dept));
        }

        private void setItem(ItemDto item) {
            this.item = item;
            this.stuffName = item.stuff().name();
            this.itemNum = item.num();
        }

        @Override
        protected ItemDto execMethod() {
            return itemService.getByIndex(userToken, univCode, deptCode, stuffName, itemNum);
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(itemDao.getByIndex(univCode, deptCode, stuffName, itemNum))
                    .thenReturn(item);

            TestHelper.objectCompareTest(
                    this::execMethod,
                    item
            );
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[해당 `index`에 `item`이 존재하지 않을 시]_[NotFoundException]")
        public void ERROR_itemNotFound_NotFoundException() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(itemDao.getByIndex(univCode, deptCode, stuffName, itemNum))
                    .thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(
                    this::execMethod,
                    NotFoundException.class
            );
        }
    }

    @Nested
    @DisplayName("create()")
    public final class TestCreate extends ItemNestedTest {
        private ItemDto item;
        private StuffDto stuff;
        private String stuffName;

        @Override
        protected void setUpDefault() {
            setDept(TEST_DEPT);
            setRequester(randomUserHaveMorePermissionOnDept(dept, AuthorityDto.Permission.STAFF));

            StuffDto targetStuff = randomStuffOnDept(dept);
            setItem(ItemDto.init(targetStuff, targetStuff.nextItemNum()));
        }

        private void setItem(ItemDto item) {
            this.item = item;
            this.stuff = item.stuff();
            this.stuffName = stuff.name();
        }

        @Override
        protected void setRequesterAccessDenied() {
            setRequester(randomUserHaveLessPermissionOnDept(dept, AuthorityDto.Permission.USER));
        }

        @Override
        protected Object execMethod() {
            return itemService.create(userToken, univCode, deptCode, stuffName);
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(stuffDao.getByIndex(univCode, deptCode, stuffName))
                    .thenReturn(stuff);
            when(itemDao.create(item)).thenReturn(item);

            TestHelper.objectCompareTest(this::execMethod, item);

            verify(itemDao, times(1)).create(item);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[해당 `index`의 `Stuff`가 존재하지 않을 시]_[InvalidIndexException]")
        public void ERROR_stuffInvalidIndex_InvalidIndexException() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(stuffDao.getByIndex(univCode, deptCode, stuffName))
                    .thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(this::execMethod, IndexInvalidException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[해당 `stuff`의 `item`개수가 이미 " + Constants.MAX_ITEM_NUM + "개 일 시]_[ExceedMaxItemNumException]")
        public void ERROR_stuffIsFull_ExceedMaxItemNumException() {
            setUpDefault();

            StuffDto newStuff = getFullStuff(stuff);
            setItem(ItemDto.init(newStuff, newStuff.nextItemNum()));

            mockDepartmentAndRequester();
            when(stuffDao.getByIndex(univCode, deptCode, stuffName))
                    .thenReturn(stuff);

            TestHelper.exceptionTest(this::execMethod, ItemAmountLimitExceededException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[해당 `index`의 `item`이 이미 존재할 시]_[ConflictException]")
        public void ERROR_stuffConflict_ConflictException() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(stuffDao.getByIndex(univCode, deptCode, stuffName))
                    .thenReturn(stuff);
            when(itemDao.create(item)).thenThrow(ConflictException.class);

            TestHelper.exceptionTest(this::execMethod, ConflictException.class);
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
