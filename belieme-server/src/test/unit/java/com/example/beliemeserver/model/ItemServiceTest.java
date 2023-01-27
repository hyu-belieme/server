package com.example.beliemeserver.model;

import com.example.beliemeserver.exception.ForbiddenException;
import com.example.beliemeserver.exception.InvalidIndexException;
import com.example.beliemeserver.exception.NotFoundException;
import com.example.beliemeserver.exception.UnauthorizedException;
import com.example.beliemeserver.model.dto.ItemDto;
import com.example.beliemeserver.model.dto.StuffDto;
import com.example.beliemeserver.model.dto.UserDto;
import com.example.beliemeserver.model.service.ItemService;
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
import static org.mockito.Mockito.when;

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
