package com.example.beliemeserver.model;

import com.example.beliemeserver.exception.*;
import com.example.beliemeserver.model.dto.ItemDto;
import com.example.beliemeserver.model.dto.StuffDto;
import com.example.beliemeserver.model.dto.UserDto;
import com.example.beliemeserver.model.service.ItemService;
import com.example.beliemeserver.util.StubHelper;
import com.example.beliemeserver.util.TestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static com.example.beliemeserver.util.StubHelper.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest extends BaseServiceTest {
    @InjectMocks
    private ItemService itemService;

    @Nested
    @DisplayName("getListByStuff()")
    public final class  TestGetListByStuff extends BaseNestedTestClass {
        private List<ItemDto> expected;

        @Override
        protected void setUpDefault() {
            setUp(ALL_STUFFS.get(0), HYU_CSE_NORMAL_1_USER);

            expected = getItemListByStuffFromStub(stuff);
        }

        @Override
        protected List<ItemDto> execMethod() {
            return itemService.getListByStuff(userToken, universityCode, departmentCode, stuffName);
        }

        @Test
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(itemDao.getListByStuff(universityCode, departmentCode, stuffName))
                    .thenReturn(expected);

            TestHelper.listCompareTest(this::execMethod, expected);
        }

        @Test
        @DisplayName("[ERROR]_[해당 `index`의 `Stuff`가 존재하지 않을 시]_[InvalidIndexException]")
        public void ERROR_stuffInvalidIndex_InvalidIndexException() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(itemDao.getListByStuff(universityCode, departmentCode, stuffName))
                    .thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
        }


        private List<ItemDto> getItemListByStuffFromStub(StuffDto stuff) {
            List<ItemDto> output = new ArrayList<>();
            for(ItemDto item : ALL_ITEMS) {
                if(item.stuff().matchUniqueKey(stuff)) {
                    output.add(item);
                }
            }
            return output;
        }
    }

    @Nested
    @DisplayName("getByIndex()")
    public final class TestGetByIndex extends BaseNestedTestClass {
        private int itemNum;

        private ItemDto expected;
        @Override
        protected void setUpDefault() {
            setUp(ALL_STUFFS.get(0), HYU_CSE_NORMAL_1_USER);
            itemNum = 1;

            expected = getItemByIndexFromStub();
        }

        @Override
        protected ItemDto execMethod() {
            return itemService.getByIndex(userToken, universityCode, departmentCode, stuffName, itemNum);
        }

        @Test
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(itemDao.getByIndex(universityCode, departmentCode, stuffName, itemNum))
                    .thenReturn(expected);

            TestHelper.objectCompareTest(
                    this::execMethod,
                    expected
            );
        }

        @Test
        @DisplayName("[ERROR]_[해당 `index`에 `item`이 존재하지 않을 시]_[NotFoundException]")
        public void ERROR_itemNotFound_NotFoundException() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(itemDao.getByIndex(universityCode, departmentCode, stuffName, itemNum))
                    .thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(
                    this::execMethod,
                    NotFoundException.class
            );
        }

        private ItemDto getItemByIndexFromStub() {
            for(ItemDto item : ALL_ITEMS) {
                if(item.matchUniqueKey(universityCode, departmentCode, stuffName, itemNum)) {
                    return item;
                }
            }
            return null;
        }
    }

    @Nested
    @DisplayName("create()")
    public final class TestCreate extends BaseNestedTestClass {
        private ItemDto newItem;

        @Override
        protected void setUpDefault() {
            // TODO 한번에 모든 stub 세팅하지 않고 변수 참조시에 setting 하게 하기
            StubHelper.init();
            setUp(ALL_STUFFS.get(0), HYU_CSE_STAFF_USER);

            newItem = ItemDto.init(stuff, getNextItemNumFromStub());
        }

        @Override
        protected void setRequesterAccessDenied() {
            requester = HYU_CSE_NORMAL_1_USER;
        }

        @Override
        protected Object execMethod() {
            return itemService.create(userToken, universityCode, departmentCode, stuffName);
        }

        @Test
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(stuffDao.getByIndex(universityCode, departmentCode, stuffName))
                    .thenReturn(stuff);
            when(itemDao.create(newItem)).thenReturn(newItem);

            TestHelper.objectCompareTest(this::execMethod, newItem);

            verify(itemDao, times(1)).create(newItem);
        }

        @Test
        @DisplayName("[ERROR]_[해당 `index`의 `Stuff`가 존재하지 않을 시]_[InvalidIndexException]")
        public void ERROR_stuffInvalidIndex_InvalidIndexException() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(stuffDao.getByIndex(universityCode, departmentCode, stuffName))
                    .thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
        }

        @Test
        @DisplayName("[ERROR]_[해당 `stuff`의 `item`개수가 이미 " + ItemService.MAX_ITEM_NUM + "개 일 시]_[MethodNotAllowedException]")
        public void ERROR_stuffIsFull_MethodNotAllowedException() {
            setUpDefault();

            List<ItemDto> newItems = stuff.items();
            while(newItems.size() <= ItemService.MAX_ITEM_NUM) {
                ItemDto newItem = ItemDto.init(stuff, newItems.size());
                newItems.add(newItem);
            }
            stuff = stuff.withItems(newItems);
            newItem = ItemDto.init(stuff, stuff.nextItemNum());

            mockDepartmentAndRequester();
            when(stuffDao.getByIndex(universityCode, departmentCode, stuffName))
                    .thenReturn(stuff);

            TestHelper.exceptionTest(this::execMethod, MethodNotAllowedException.class);
        }

        @Test
        @DisplayName("[ERROR]_[해당 `index`의 `item`이 이미 존재할 시]_[ConflictException]")
        public void ERROR_stuffConflict_ConflictException() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(stuffDao.getByIndex(universityCode, departmentCode, stuffName))
                    .thenReturn(stuff);
            when(itemDao.create(newItem)).thenThrow(ConflictException.class);

            TestHelper.exceptionTest(this::execMethod, ConflictException.class);
        }


        private int getNextItemNumFromStub() {
            int lastItemNum = 0;
            for(ItemDto item : ALL_ITEMS) {
                if(item.stuff().matchUniqueKey(universityCode, departmentCode, stuffName)) {
                    if(lastItemNum < item.num()) {
                        lastItemNum = item.num();
                    }
                }
            }
            return lastItemNum + 1;
        }
    }

    private abstract class BaseNestedTestClass {
        protected StuffDto stuff;
        protected String universityCode;
        protected String departmentCode;
        protected String stuffName;

        protected UserDto requester;

        protected abstract void setUpDefault();

        protected abstract Object execMethod();

        protected void setUp(StuffDto stuff, UserDto requester) {
            this.stuff = stuff;
            this.universityCode = stuff.department().university().code();
            this.departmentCode = stuff.department().code();
            this.stuffName = stuff.name();

            this.requester = requester;
        }

        protected void setRequesterAccessDenied() {
            requester = HYU_CSE_BANNED_USER;
        }

        protected void mockDepartmentAndRequester() {
            when(departmentDao.getDepartmentByUniversityCodeAndDepartmentCodeData(universityCode, departmentCode))
                    .thenReturn(stuff.department());
            when(userDao.getByToken(userToken)).thenReturn(requester);
        }

        @Test
        @DisplayName("[ERROR]_[해당 `index`의 `department`가 존재하지 않을 시]_[InvalidIndexException]")
        public void ERROR_getInvalidIndex_InvalidIndexException() {
            setUpDefault();

            when(departmentDao.getDepartmentByUniversityCodeAndDepartmentCodeData(universityCode, departmentCode))
                    .thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
        }

        @Test
        @DisplayName("[ERROR]_[토큰이 검증되지 않을 시]_[UnauthorizedException]")
        public void ERROR_isUnauthorizedToken_UnauthorizedException() {
            setUpDefault();

            when(departmentDao.getDepartmentByUniversityCodeAndDepartmentCodeData(universityCode, departmentCode))
                    .thenReturn(stuff.department());
            when(userDao.getByToken(userToken)).thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(this::execMethod, UnauthorizedException.class);
        }

        @Test
        @DisplayName("[ERROR]_[권한이 없을 시]_[ForbiddenException]")
        public void ERROR_accessDenied_ForbiddenException() {
            setUpDefault();
            setRequesterAccessDenied();

            mockDepartmentAndRequester();

            TestHelper.exceptionTest(this::execMethod, ForbiddenException.class);
        }
    }
}
